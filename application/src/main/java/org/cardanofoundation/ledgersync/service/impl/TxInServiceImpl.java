package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.RedeemerTag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxIn.TxInBuilder;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.aggregate.AggregatedAddressBalance;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.repository.TxInRepository;
import org.cardanofoundation.ledgersync.repository.UnconsumeTxInRepository;
import org.cardanofoundation.ledgersync.service.BlockDataService;
import org.cardanofoundation.ledgersync.service.MultiAssetService;
import org.cardanofoundation.ledgersync.service.TxInService;
import org.cardanofoundation.ledgersync.service.TxOutService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TxInServiceImpl implements TxInService {

    BlockDataService blockDataService;
    TxOutService txOutService;
    MultiAssetService multiAssetService;

    TxInRepository txInRepository;
    UnconsumeTxInRepository unconsumeTxInRepository;

    @Override
    public void handleTxIns(Collection<AggregatedTx> txs,
                            Map<String, Set<AggregatedTxIn>> txInMap,
                            Map<String, Tx> txMap, Map<Pair<String, Short>, TxOut> newTxOutMap,
                            Map<Long, List<MaTxOut>> newMaTxOutMap,
                            Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap) {
        if (CollectionUtils.isEmpty(txInMap)) {
            return;
        }

        AtomicBoolean shouldFindAssets = new AtomicBoolean(false);
        Map<Pair<String, Short>, TxOut> txOutMap = getTxOutFromTxInsMap(txInMap);

        // Calculate tx fee or deposit value then update epoch if needed and
        // check if assets should be taken into account in transaction inputs
        txs.parallelStream().forEach(aggregatedTx -> {
            Set<AggregatedTxIn> txIns = txInMap.get(aggregatedTx.getHash());
            if (CollectionUtils.isEmpty(txIns)) {
                return;
            }

            AggregatedBlock aggregatedBlock = blockDataService
                    .getAggregatedBlock(aggregatedTx.getBlockHash());
            Tx tx = txMap.get(aggregatedTx.getHash());
            if (aggregatedBlock.getEra() == Era.BYRON) {
                calculateByronFee(aggregatedTx, tx, txIns, txOutMap, newTxOutMap);
            } else {
                shouldFindAssets.set(true);
                calculateShelleyDeposit(tx, txIns, txOutMap, newTxOutMap, aggregatedTx.getWithdrawals());
            }
        });

        Queue<TxIn> txInQueue = new ConcurrentLinkedQueue<>();
        txInMap.entrySet().parallelStream().forEach(entry -> {
            Set<AggregatedTxIn> txInSet = entry.getValue();
            String txHash = entry.getKey();
            Tx tx = txMap.get(txHash);
            Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap.get(txHash);

            txInSet.parallelStream().forEach(txIn -> {
                Redeemer redeemer = null;
                Integer redeemerPointerIdx = txIn.getRedeemerPointerIdx();
                if (!CollectionUtils.isEmpty(redeemerInTxMap) && Objects.nonNull(redeemerPointerIdx)) {
                    redeemer = redeemerInTxMap.get(Pair.of(RedeemerTag.Spend, redeemerPointerIdx));
                }
                txInQueue.add(handleTxIn(tx, txIn, txOutMap, newTxOutMap, redeemer));
            });
        });

        handleTxInBalances(txInMap, txOutMap, newTxOutMap, newMaTxOutMap, shouldFindAssets.get());
        txInRepository.saveAll(txInQueue);
    }

    private Map<Pair<String, Short>, TxOut> getTxOutFromTxInsMap(
            Map<String, Set<AggregatedTxIn>> txInMap) {
        Set<AggregatedTxIn> txIns = txInMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        // Key is a pair of tx hash and tx out index, value is the target tx out
        return txOutService
                .getTxOutCanUseByAggregatedTxIns(txIns)
                .parallelStream()
                .collect(Collectors.toConcurrentMap(
                        txOut -> Pair.of(txOut.getTx().getHash(), txOut.getIndex()),
                        Function.identity()
                ));
    }

    private void calculateShelleyDeposit(Tx tx, Set<AggregatedTxIn> txIns,
                                         Map<Pair<String, Short>, TxOut> txOutMap,
                                         Map<Pair<String, Short>, TxOut> newTxOutMap,
                                         Map<String, BigInteger> withdrawalsMap) {
        if (Boolean.FALSE.equals(tx.getValidContract())) {
            return;
        }

        Collection<BigInteger> withdrawalsCoin =
                CollectionUtils.isEmpty(withdrawalsMap) ?
                        Collections.emptyList() : withdrawalsMap.values();
        var withdrawalSum = withdrawalsCoin.stream()
                .reduce(BigInteger.ZERO, BigInteger::add)
                .longValue();
        var inSum = getTxInSum(txOutMap, newTxOutMap, tx, txIns).longValue();
        var outSum = tx.getOutSum().longValue();
        var fees = tx.getFee().longValue();
        var deposit = inSum + withdrawalSum - outSum - fees;
        tx.setDeposit(deposit);
    }

    private void calculateByronFee(AggregatedTx aggregatedTx, Tx tx, Set<AggregatedTxIn> txIns,
                                   Map<Pair<String, Short>, TxOut> txOutMap,
                                   Map<Pair<String, Short>, TxOut> newTxOutMap) {
        if (CollectionUtils.isEmpty(txIns)) {
            return;
        }

        BigInteger inSum = getTxInSum(txOutMap, newTxOutMap, tx, txIns);
        var fee = inSum.subtract(tx.getOutSum());
        tx.setFee(fee);
        aggregatedTx.setFee(fee);
    }

    private static BigInteger getTxInSum(
            Map<Pair<String, Short>, TxOut> txOutMap,
            Map<Pair<String, Short>, TxOut> newTxOutMap,
            Tx tx, Set<AggregatedTxIn> txIns) {
        return txIns.stream()
                .map(txIn -> {
                    Pair<String, Short> txOutKey = Pair.of(txIn.getTxId(), (short) txIn.getIndex());
                    TxOut txOut = txOutMap.get(txOutKey);
                    if (Objects.isNull(txOut)) {
                        txOut = newTxOutMap.get(txOutKey);
                    }

                    if (Objects.isNull(txOut)) {
                        throw new IllegalStateException(String.format(
                                "Tx in %s, index %d, of tx %s has no tx_out before",
                                txIn.getTxId(), txIn.getIndex(), tx.getHash()));
                    }
                    return txOut;
                })
                .map(TxOut::getValue)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private TxIn handleTxIn(Tx tx, AggregatedTxIn txInput,
                            Map<Pair<String, Short>, TxOut> txOutMap,
                            Map<Pair<String, Short>, TxOut> newTxOutMap, Redeemer redeemer) {
        TxInBuilder<?, ?> txInBuilder = TxIn.builder();

        txInBuilder.txInput(tx);
        txInBuilder.redeemer(redeemer);

        Pair<String, Short> txOutKey = Pair.of(txInput.getTxId(), (short) txInput.getIndex());
        TxOut txOut = txOutMap.get(txOutKey);
        if (Objects.isNull(txOut)) {
            txOut = newTxOutMap.get(txOutKey);
            if (Objects.isNull(txOut)) {
                log.error("Tx in {}, index {}, of tx {}  has no tx_out before",
                        txInput.getTxId(), txInput.getIndex(), tx.getHash());
                throw new IllegalStateException();
            }
        }

        txInBuilder.txOut(txOut.getTx());
        txInBuilder.txOutIndex(txOut.getIndex());

        return txInBuilder.build();
    }

    private void handleTxInBalances(Map<String, Set<AggregatedTxIn>> txInMap,
                                    Map<Pair<String, Short>, TxOut> txOutMap,
                                    Map<Pair<String, Short>, TxOut> newTxOutMap,
                                    Map<Long, List<MaTxOut>> newMaTxOutMap,
                                    boolean shouldFindAssets) {
        // Byron does not have assets, hence this step is skipped
        Map<Long, List<MaTxOut>> maTxOutMap = !shouldFindAssets
                ? Collections.emptyMap()
                : multiAssetService
                .findAllByTxOutIn(txOutMap.values())
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        maTxOut -> maTxOut.getTxOut().getId(),
                        Collectors.toList()));

        txInMap.entrySet().parallelStream().forEach(entry ->
                entry.getValue().parallelStream().forEach(txIn -> {
                    Pair<String, Short> txOutKey = Pair.of(txIn.getTxId(), (short) txIn.getIndex());
                    TxOut txOut = txOutMap.get(txOutKey);
                    if (Objects.isNull(txOut)) {
                        txOut = newTxOutMap.get(txOutKey);
                    }
                    String txHash = entry.getKey();
                    String address = txOut.getAddress();
                    AggregatedAddressBalance addressBalance = blockDataService
                            .getAggregatedAddressBalanceFromAddress(address);

                    // Subtract native balance
                    addressBalance.subtractNativeBalance(txHash, txOut.getValue());

                    // Find associated multi asset output records
                    List<MaTxOut> maTxOuts = maTxOutMap.get(txOut.getId());
                    if (CollectionUtils.isEmpty(maTxOuts)) {
                        maTxOuts = newMaTxOutMap.get(txOut.getId());
                    }

                    // Subtract asset balances if there's any
                    if (!CollectionUtils.isEmpty(maTxOuts)) {
                        maTxOuts.parallelStream().forEach(maTxOut -> addressBalance.subtractAssetBalance(
                                txHash, maTxOut.getIdent().getFingerprint(), maTxOut.getQuantity())
                        );
                    }
                }));
    }

    @Override
    public List<UnconsumeTxIn> handleUnconsumeTxIn(
            Map<String, Set<AggregatedTxIn>> unconsumedTxInMap,
            Map<Pair<String, Short>, TxOut> newTxOutMap, Map<String, Tx> txMap,
            Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap) {
        if (CollectionUtils.isEmpty(unconsumedTxInMap)) {
            return Collections.emptyList();
        }

        // Key is a pair of tx hash and tx out index, value is the target tx out
        Map<Pair<String, Short>, TxOut> txOutMap = getTxOutFromTxInsMap(unconsumedTxInMap);

        List<UnconsumeTxIn> unconsumeTxIns = new ArrayList<>();
        unconsumedTxInMap.forEach((txHash, unconsumedTxInSet) -> {
            Tx tx = txMap.get(txHash);
            Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap.get(txHash);

            unconsumedTxInSet.forEach(unconsumedTxIn -> {
                Redeemer redeemer = null;
                Integer redeemerPointerIdx = unconsumedTxIn.getRedeemerPointerIdx();
                if (!CollectionUtils.isEmpty(redeemerInTxMap) && Objects.nonNull(redeemerPointerIdx)) {
                    redeemer = redeemerInTxMap.get(Pair.of(RedeemerTag.Spend, redeemerPointerIdx));
                }
                TxIn txIn = handleTxIn(tx, unconsumedTxIn, txOutMap, newTxOutMap, null);
                unconsumeTxIns.add(UnconsumeTxIn.builder()
                        .txIn(txIn.getTxInput())
                        .txOut(txIn.getTxOut())
                        .txOutIndex(txIn.getTxOutIndex())
                        .redeemer(redeemer)
                        .build());
            });
        });

        return unconsumeTxInRepository.saveAll(unconsumeTxIns);
    }
}
