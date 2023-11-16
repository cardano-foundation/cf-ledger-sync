package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import com.bloxbean.cardano.client.plutus.spec.ExUnits;
import com.bloxbean.cardano.client.plutus.spec.RedeemerTag;
import com.bloxbean.cardano.yaci.core.model.Amount;
import com.bloxbean.cardano.yaci.core.model.certs.Certificate;
import com.bloxbean.cardano.yaci.core.model.certs.CertificateType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeDelegation;
import com.bloxbean.cardano.yaci.core.model.certs.StakeDeregistration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer;
import org.cardanofoundation.explorer.consumercommon.entity.Redeemer.RedeemerBuilder;
import org.cardanofoundation.explorer.consumercommon.entity.RedeemerData;
import org.cardanofoundation.explorer.consumercommon.entity.RedeemerData.RedeemerDataBuilder;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.explorer.consumercommon.entity.TxOut;
import org.cardanofoundation.explorer.consumercommon.enumeration.ScriptPurposeType;
import org.cardanofoundation.ledgersync.common.common.Datum;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.TxOutProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.RedeemerDataRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.RedeemerRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.RedeemerService;
import org.cardanofoundation.ledgersync.explorerconsumer.util.RedeemerWrapper;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.cardanofoundation.ledgersync.explorerconsumer.util.DatumFormatUtil.datumJsonRemoveSpace;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RedeemerServiceImpl implements RedeemerService {

    TxOutRepository txOutRepository;
    RedeemerRepository redeemerRepository;
    RedeemerDataRepository redeemerDataRepository;

    @Override
    public Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> handleRedeemers(
            Collection<AggregatedTx> txs, Map<String, Tx> txMap,
            Map<Pair<String, Short>, TxOut> newTxOutMap) {
        List<AggregatedTx> txsWithRedeemers = txs.stream()
                .filter(tx -> Objects.nonNull(tx.getWitnesses()))
                .filter(tx -> !CollectionUtils.isEmpty(tx.getWitnesses().getRedeemers()))
                .toList();
        if (CollectionUtils.isEmpty(txsWithRedeemers)) {
            return new ConcurrentHashMap<>();
        }

        Set<String> redeemerDataHash = txsWithRedeemers.stream()
                .flatMap(tx -> tx.getWitnesses().getRedeemers().stream())
                .map(redeemer -> new RedeemerWrapper(redeemer).getDataHash())
                .collect(Collectors.toSet());
        Map<String, RedeemerData> existingRedeemerDataMap = redeemerDataRepository
                .findAllByHashIn(redeemerDataHash)
                .stream()
                .collect(Collectors.toMap(RedeemerData::getHash, Function.identity()));
        Map<String, RedeemerData> redeemerDataMap = new LinkedHashMap<>();

        // Create a map of redeemers, with key is tx hash, and value is another map of redeemers
        // executed within a transaction (key is redeemer pointer, value is target redeemer record)
        Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap = new ConcurrentHashMap<>();
        List<Redeemer> redeemersToSave = new ArrayList<>();

        txsWithRedeemers.forEach(aggregatedTx -> {
            Tx tx = txMap.get(aggregatedTx.getHash());
            var redeemers = aggregatedTx.getWitnesses().getRedeemers();

            redeemers.forEach(redeemerObj -> {
                Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap
                        .computeIfAbsent(aggregatedTx.getHash(), unused -> new ConcurrentHashMap<>());
                com.bloxbean.cardano.client.plutus.spec.Redeemer cclRedeemer = new RedeemerWrapper(redeemerObj).getRedeemer();
                Datum plutusData = new RedeemerWrapper(redeemerObj).getDatum();
                RedeemerData redeemerData = Optional
                        .ofNullable(existingRedeemerDataMap.get(plutusData.getHash()))
                        .orElseGet(() -> {
                            // Redeemer data might have the same hash. Since we do batch insert,
                            // we save previously created redeemer data from redeemer list into a map for
                            // re-usability (the data hash is unique so saving new redeemer data entities
                            // with the same hash will violate that constraint)
                            RedeemerData previousRedeemerData = redeemerDataMap.get(plutusData.getHash());
                            if (Objects.isNull(previousRedeemerData)) {
                                previousRedeemerData = buildRedeemerData(plutusData, tx);
                                redeemerDataMap.put(plutusData.getHash(), previousRedeemerData);
                            }
                            return previousRedeemerData;
                        });

                // Get existing redeemer record. If it exists, update its data
                int pointerIndex = cclRedeemer.getIndex().intValue();
                RedeemerTag redeemerTag = cclRedeemer.getTag();
                Pair<RedeemerTag, Integer> redeemerPointer = Pair.of(redeemerTag, pointerIndex);
                Redeemer redeemerEntity = redeemerInTxMap.get(redeemerPointer);
                if (Objects.nonNull(redeemerEntity)) {
                    RedeemerData existingRedeemerData = redeemerEntity.getRedeemerData();
                    if (Objects.equals(plutusData.getHash(), existingRedeemerData.getHash())) {
                        return;
                    }

                    // Re-assign redeemer's data to new record and delete old one
                    redeemerDataMap.remove(existingRedeemerData.getHash());
                    redeemerEntity.setRedeemerData(redeemerData);
                    return;
                }

                // Otherwise, create new redeemer record and commit
                Redeemer redeemer = buildRedeemer(cclRedeemer, redeemerData, tx, aggregatedTx, newTxOutMap);
                redeemerInTxMap.put(redeemerPointer, redeemer);
                redeemersToSave.add(redeemer);
            });
        });

        redeemerDataRepository.saveAll(redeemerDataMap.values());
        redeemerRepository.saveAll(redeemersToSave);

        return redeemersMap;
    }

    /**
     * Create new redeemer entity
     *
     * @param redeemerObj  redeemer object
     * @param redeemerData redeemer data
     * @param tx           transaction entity
     * @param aggregatedTx current redeemer's transaction body
     * @param newTxOutMap  a map of newly created txOut entities that are not inserted yet
     * @return newly created redeemer entity
     */
    private Redeemer buildRedeemer(
            com.bloxbean.cardano.client.plutus.spec.Redeemer redeemerObj,
            RedeemerData redeemerData, Tx tx, AggregatedTx aggregatedTx,
            Map<Pair<String, Short>, TxOut> newTxOutMap) {
        RedeemerBuilder<?, ?> redeemerBuilder = Redeemer.builder();

        String scriptHash = getRedeemerPointer(aggregatedTx, newTxOutMap, redeemerObj);
        ExUnits exUnits = redeemerObj.getExUnits();
        redeemerBuilder.unitMem(exUnits.getMem().longValue());
        redeemerBuilder.unitSteps(exUnits.getSteps().longValue());
        redeemerBuilder.purpose(ScriptPurposeType.fromValue(redeemerObj.getTag().name().toLowerCase()));
        redeemerBuilder.index(redeemerObj.getIndex().intValue());
        redeemerBuilder.scriptHash(scriptHash);
        redeemerBuilder.redeemerData(redeemerData);
        redeemerBuilder.tx(tx);
        // TODO - fee (requires getting script fee data from node)

        return redeemerBuilder.build();
    }

    /**
     * Get redeemer's script hash from referenced object
     *
     * @param aggregatedTx transaction body
     * @param newTxOutMap  a map of newly created txOut entities that are not inserted yet
     * @param redeemerObj  redeemer object
     * @return referenced script hash
     */
    private String getRedeemerPointer(
            AggregatedTx aggregatedTx, Map<Pair<String, Short>, TxOut> newTxOutMap,
            com.bloxbean.cardano.client.plutus.spec.Redeemer redeemerObj) {
        RedeemerTag tag = redeemerObj.getTag();
        int pointerIndex = redeemerObj.getIndex().intValue();

        return switch (tag) {
            case Spend -> {
                List<AggregatedTxIn> sortedTxInputs = aggregatedTx.getTxInputs().stream()
                        .sorted((txIn1, txIn2) -> {
                            String txHash1 = txIn1.getTxId();
                            String txHash2 = txIn2.getTxId();
                            int txHashComparison = txHash1.compareTo(txHash2);

                            // Sort TxIn by its Tx hash. If equals, sort by TxOut index
                            return txHashComparison != 0
                                    ? txHashComparison
                                    : Integer.compare(txIn1.getIndex(), txIn2.getIndex());
                        }).toList();
                yield getTxInScriptHash(sortedTxInputs, pointerIndex, newTxOutMap);
            }
            case Mint -> handleMintingScriptHash(aggregatedTx.getMint(), pointerIndex);
            case Cert -> handleCertPtr(aggregatedTx.getCertificates(), pointerIndex);
            default -> // Reward pointer
                    handleRewardPtr(new ArrayList<>(aggregatedTx.getWithdrawals().keySet()),
                            pointerIndex);
        };
    }

    private String getTxInScriptHash(
            List<AggregatedTxIn> txInputs, int pointerIndex,
            Map<Pair<String, Short>, TxOut> newTxOutMap) {
        AggregatedTxIn txIn = txInputs.get(pointerIndex);

        // Find target TxOut from provided TxOuts
        Pair<String, Short> txOutKey = Pair.of(txIn.getTxId(), (short) txIn.getIndex());
        TxOut txOut = newTxOutMap.get(txOutKey);
        if (Objects.nonNull(txOut) && txOut.getAddressHasScript().equals(Boolean.TRUE)) {
            txIn.setRedeemerPointerIdx(pointerIndex);
            return txOut.getPaymentCred();
        }

        // Fallback to TxOut from DB
        Optional<TxOutProjection> txOutOptional = txOutRepository
                .findTxOutByTxHashAndTxOutIndex(txIn.getTxId(), (short) txIn.getIndex());
        if (txOutOptional.isPresent()) {
            TxOutProjection txOutProjection = txOutOptional.get();
            if (txOutProjection.getAddressHasScript().equals(Boolean.TRUE)) {
                txIn.setRedeemerPointerIdx(pointerIndex);
                return txOutProjection.getPaymentCred();
            }
        }

        log.error("Can not find payment cred tx id {}, index {}",
                txIn.getTxId(), txIn.getIndex());
        throw new IllegalStateException();
    }

    private String handleMintingScriptHash(List<Amount> mints, int pointerIndex) {
        Amount minting = mints.get(pointerIndex);
        return minting.getPolicyId();
    }

    private String handleCertPtr(
            List<Certificate> certificates, int pointerIndex) {
        Certificate certificate = certificates.get(pointerIndex);

        // Stake delegation
        if (certificate.getType() == CertificateType.STAKE_DELEGATION) {
            StakeDelegation delegation = (StakeDelegation) certificate;
            return delegation.getStakeCredential().getHash();
        }

        // Stake de-registration
        StakeDeregistration stakeDeregistration = (StakeDeregistration) certificate;
        return stakeDeregistration.getStakeCredential().getHash();
    }

    private String handleRewardPtr(List<String> rewardAccounts, int pointerIndex) {
        String rewardAccount = rewardAccounts.get(pointerIndex);
        // Trim network tag
        return rewardAccount.substring(2);
    }

    private RedeemerData buildRedeemerData(Datum plutusData, Tx tx) {
        RedeemerDataBuilder<?, ?> redeemerDataBuilder = RedeemerData.builder();

        redeemerDataBuilder.hash(plutusData.getHash());
        redeemerDataBuilder.value(datumJsonRemoveSpace(plutusData.getJson()));
        redeemerDataBuilder.bytes(HexUtil.decodeHexString(plutusData.getCbor()));
        redeemerDataBuilder.tx(tx);

        return redeemerDataBuilder.build();
    }

}
