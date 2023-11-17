package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.*;
import org.cardanofoundation.explorer.consumercommon.entity.FailedTxOut.FailedTxOutBuilder;
import org.cardanofoundation.explorer.consumercommon.entity.TxOut.TxOutBuilder;
import org.cardanofoundation.explorer.consumercommon.enumeration.TokenType;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedAddress;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.EUTXOWrapper;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.TransactionOutMultiAssets;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.FailedTxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MultiAssetTxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MultiAssetService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.ScriptService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.TxOutService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.bloxbean.cardano.yaci.core.util.Constants.LOVELACE;
import static org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant.TX_OUT_BATCH_QUERY_SIZE;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TxOutServiceImpl implements TxOutService {

    ScriptService scriptService;
    MultiAssetService multiAssetService;

    TxOutRepository txOutRepository;
    FailedTxOutRepository failedTxOutRepository;
    MultiAssetTxOutRepository multiAssetTxOutRepository;

    @Override
    public Collection<TxOut> getTxOutCanUseByAggregatedTxIns(Collection<AggregatedTxIn> txIns) {
        if (CollectionUtils.isEmpty(txIns)) {
            return Collections.emptySet();
        }
        Queue<TxOut> txOuts = new ConcurrentLinkedQueue<>();

        var txHashIndexPairs = txIns.stream()
                .map(txIn -> {
                    String txHash = txIn.getTxId().trim();
                    short index = (short) txIn.getIndex();
                    return Pair.of(txHash, index);
                }).sorted(Comparator.comparing(Pair::getFirst)).collect(Collectors.toList());

        Lists.partition(txHashIndexPairs, TX_OUT_BATCH_QUERY_SIZE)
                .parallelStream()
                .forEach(txHashIndexPairBatch -> txOutRepository
                        .findTxOutsByTxHashInAndTxIndexIn(txHashIndexPairBatch)
                        .parallelStream()
                        .forEach(txOutProjection -> {
                            Tx tx = Tx.builder()
                                    .id(txOutProjection.getTxId())
                                    .hash(txOutProjection.getTxHash())
                                    .build();

                            TxOut txOut = TxOut.builder()
                                    .id(txOutProjection.getId())
                                    .index(txOutProjection.getIndex())
                                    .address(txOutProjection.getAddress())
                                    .addressHasScript(txOutProjection.getAddressHasScript())
                                    .paymentCred(txOutProjection.getPaymentCred())
                                    .stakeAddress(
                                            StakeAddress.builder().id(txOutProjection.getStakeAddressId()).build())
                                    .value(txOutProjection.getValue())
                                    .tx(tx)
                                    .build();
                            txOuts.add(txOut);
                        }));
        return txOuts;
    }

    @Override
    public EUTXOWrapper prepareTxOuts(
            Map<String, List<AggregatedTxOut>> aggregatedTxOutMap,
            Map<String, Tx> txMap, Map<String, StakeAddress> stakeAddressMap, Map<String, Datum> datumMap) {
        if (CollectionUtils.isEmpty(aggregatedTxOutMap)) {
            return new EUTXOWrapper(new ArrayList<>(), new ArrayList<>());
        }

        Set<String> scriptHashes = new ConcurrentSkipListSet<>();
        Set<String> datumHashes = new ConcurrentSkipListSet<>();

        Queue<TransactionOutMultiAssets> txOutAndMas = new ConcurrentLinkedQueue<>();
        aggregatedTxOutMap.entrySet().parallelStream().forEach(entry -> {
            String txHash = entry.getKey();
            Tx tx = txMap.get(txHash);
            var txOutputs = entry.getValue();

            txOutputs.parallelStream().forEach(aggregatedTxOut -> {
                TransactionOutMultiAssets txOutAndMa =
                        handleTxOutAndMultiAsset(tx, aggregatedTxOut, stakeAddressMap);
                if (StringUtils.hasText(aggregatedTxOut.getScriptRef())) {
                    scriptHashes.add(scriptService.getHashOfReferenceScript(aggregatedTxOut.getScriptRef()));
                }
                //TODO -- yaci
                if (Objects.nonNull(aggregatedTxOut.getInlineDatum())
                        && StringUtils.hasText(aggregatedTxOut.getInlineDatum().getHash())) {
                    datumHashes.add(aggregatedTxOut.getInlineDatum().getHash());
                }
                txOutAndMas.add(txOutAndMa);
            });
        });

        //update Tx_out
        Map<String, Script> scriptMap = scriptService.getScriptsByHashes(scriptHashes);

        txOutAndMas.parallelStream().forEach(txOutAndMa -> {
            updateReferScript(scriptMap, txOutAndMa.getTxOut(), txOutAndMa.getScriptRefer());
            updateInlineDatum(datumMap, txOutAndMa.getTxOut(), txOutAndMa.getDatumHash());
        });

        //Save to tx_out and multi asset
        List<TxOut> txOutList = txOutAndMas.stream()
                .map(TransactionOutMultiAssets::getTxOut)
                .collect(Collectors.toList());

        // Convert MaTxOut maps to a single map
        MultiValueMap<String, MaTxOut> pMaTxOuts = txOutAndMas.stream()
                .map(TransactionOutMultiAssets::getPMaTxOuts)
                .reduce(new LinkedMultiValueMap<>(), (m1, m2) -> {
                    m1.addAll(m2);
                    return m1;
                });

        txOutRepository.saveAll(txOutList);
        var maTxOuts = multiAssetService.updateIdentMaTxOuts(pMaTxOuts);
        multiAssetTxOutRepository.saveAll(maTxOuts);
        return new EUTXOWrapper(txOutList, maTxOuts);
    }

    //update reference_script_id
    private void updateReferScript(Map<String, Script> scriptMap, TxOut txOut, String scriptHash) {
        if (Objects.isNull(scriptHash)) {
            return;
        }

        Script script = scriptMap.get(scriptHash);
        if (Objects.nonNull(script)) {
            txOut.setReferenceScript(script);
        } else {
            Tx tx = txOut.getTx();
            log.debug("Script not found tx {}, valid {}, index {}, script hash {}", tx.getHash(),
                    tx.getValidContract(), txOut.getIndex(), scriptHash);
            txOut.setReferenceScript(null);
        }
    }

    private void updateInlineDatum(Map<String, Datum> datumMap, TxOut txOut, String datumHash) {
        if (Objects.isNull(datumHash)) {
            return;
        }

        Datum datum = datumMap.get(datumHash);
        if (Objects.nonNull(datum)) {
            txOut.setInlineDatum(datum);
        } else {
            Tx tx = txOut.getTx();
            log.trace("Datum not found tx {}, valid {}, index {}, datum hash {}", tx.getHash(),
                    tx.getValidContract(), txOut.getIndex(), datumHash);
            txOut.setReferenceScript(null);
        }
    }

    private TransactionOutMultiAssets handleTxOutAndMultiAsset(
            Tx tx, AggregatedTxOut txOutput, Map<String, StakeAddress> stakeAddressMap) {
        TxOutBuilder<?, ?> txOutBuilder = TxOut.builder();

        txOutBuilder.tx(tx);
        txOutBuilder.index(txOutput.getIndex().shortValue());
        txOutBuilder.dataHash(txOutput.getDatumHash());
        txOutBuilder.tokenType(TokenType.NATIVE_TOKEN);
        txOutBuilder.addressHasScript(false);
        // stake address
        AggregatedAddress aggregatedAddress = txOutput.getAddress();
        String rawStakeAddress = aggregatedAddress.getStakeAddress();
        if (StringUtils.hasText(rawStakeAddress)) {
            StakeAddress stakeAddress = stakeAddressMap.get(rawStakeAddress);
            txOutBuilder.stakeAddress(stakeAddress);
        }

        // payment cred
        txOutBuilder.paymentCred(aggregatedAddress.getPaymentCred());

        // address has script
        txOutBuilder.addressHasScript(aggregatedAddress.isAddressHasScript());

        txOutBuilder.address(aggregatedAddress.getAddress());
        txOutBuilder.addressRaw(aggregatedAddress.getAddressRaw());

        AtomicBoolean hasMultiAsset = new AtomicBoolean(false);
        txOutput.getAmounts().stream()
                .filter(amount -> !LOVELACE.equals(amount.getAssetName()))
                .findAny()
                .ifPresent(amount -> {
                    txOutBuilder.tokenType(TokenType.TOKEN);
                    hasMultiAsset.set(true);
                });

        BigInteger nativeAmount = txOutput.getNativeAmount();
        txOutBuilder.value(nativeAmount);
        if (nativeAmount.compareTo(BigInteger.valueOf(0)) > 0 && hasMultiAsset.get()) {
            txOutBuilder.tokenType(TokenType.ALL_TOKEN_TYPE);
        }

        //Script
        String scriptHash = null;
        if (StringUtils.hasText(txOutput.getScriptRef())) {
            scriptHash = scriptService.getHashOfReferenceScript(txOutput.getScriptRef());
        }

        //Datum
        String datumHash = null;
        if (StringUtils.hasText(txOutput.getDatumHash())) {
            datumHash = txOutput.getDatumHash();
        }
        
        if (Objects.nonNull(txOutput.getInlineDatum())) {
            datumHash = txOutput.getInlineDatum().getHash();
        }

        TxOut txOut = txOutBuilder.build();
        MultiValueMap<String, MaTxOut> pMaTxOuts = multiAssetService.buildMaTxOut(txOutput, txOut);
        return TransactionOutMultiAssets.builder()
                .pMaTxOuts(pMaTxOuts)
                .scriptRefer(scriptHash)
                .datumHash(datumHash)
                .txOut(txOut)
                .build();
    }

    @Override
    public void handleFailedTxOuts(Collection<AggregatedTx> successTxs,
                                   Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap,
                                   Map<String, StakeAddress> stakeAddressMap,
                                   Map<String, Datum> datumMap) {
        List<FailedTxOut> failedTxOuts = new ArrayList<>();

        successTxs.stream()
                .filter(successTx -> Objects.nonNull(successTx.getCollateralReturn()))
                .forEach(successTx -> {
                    Tx tx = txMap.get(successTx.getHash());
                    successTx.getCollateralReturn().setIndex(successTx.getTxOutputs().size());
                    failedTxOuts.add(handleFailedTxOut(tx, successTx.getCollateralReturn(), stakeAddressMap));
                });

        failedTxOuts.addAll(failedTxs.stream()
                .filter(failedTx -> !CollectionUtils.isEmpty(failedTx.getTxOutputs()))
                .flatMap(failedTx -> {
                    String txHash = failedTx.getHash();
                    Tx tx = txMap.get(txHash);
                    return failedTx.getTxOutputs().stream().map(txOut ->
                            handleFailedTxOut(tx, txOut, stakeAddressMap));
                }).toList()
        );

        failedTxOutRepository.saveAll(failedTxOuts);
    }

    @SneakyThrows
    private FailedTxOut handleFailedTxOut(
            Tx tx, AggregatedTxOut txOut, Map<String, StakeAddress> stakeAddressMap) {
        FailedTxOutBuilder<?, ?> failedTxOutBuilder = FailedTxOut.builder();
        failedTxOutBuilder.tx(tx);
        failedTxOutBuilder.index(txOut.getIndex().shortValue());
        failedTxOutBuilder.dataHash(txOut.getDatumHash());
        AggregatedAddress aggregatedAddress = txOut.getAddress();
        String rawStakeAddress = aggregatedAddress.getStakeAddress();
        if (StringUtils.hasText(rawStakeAddress)) {
            StakeAddress stakeAddress = stakeAddressMap.get(rawStakeAddress);
            failedTxOutBuilder.stakeAddress(stakeAddress);
        }

        // payment cred
        failedTxOutBuilder.paymentCred(aggregatedAddress.getPaymentCred());

        // address has script
        failedTxOutBuilder.addressHasScript(aggregatedAddress.isAddressHasScript());

        failedTxOutBuilder.address(aggregatedAddress.getAddress());
        failedTxOutBuilder.addressRaw(aggregatedAddress.getAddressRaw());

        failedTxOutBuilder.value(txOut.getNativeAmount());
        failedTxOutBuilder.multiAssetsDescr(JsonUtil.getPrettyJson(txOut.getAmounts()));
        return failedTxOutBuilder.build();
    }
}
