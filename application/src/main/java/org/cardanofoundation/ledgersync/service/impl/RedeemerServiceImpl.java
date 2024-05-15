package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.Amount;
import com.bloxbean.cardano.yaci.core.model.Datum;
import com.bloxbean.cardano.yaci.core.model.ExUnits;
import com.bloxbean.cardano.yaci.core.model.RedeemerTag;
import com.bloxbean.cardano.yaci.core.model.certs.Certificate;
import com.bloxbean.cardano.yaci.core.model.certs.CertificateType;
import com.bloxbean.cardano.yaci.core.model.certs.StakeDelegation;
import com.bloxbean.cardano.yaci.core.model.certs.StakeDeregistration;
import com.bloxbean.cardano.yaci.core.model.governance.ProposalProcedure;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer;
import org.cardanofoundation.ledgersync.consumercommon.entity.Redeemer.RedeemerBuilder;
import org.cardanofoundation.ledgersync.consumercommon.entity.RedeemerData;
import org.cardanofoundation.ledgersync.consumercommon.entity.RedeemerData.RedeemerDataBuilder;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.consumercommon.entity.TxOut;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.ScriptPurposeType;
import org.cardanofoundation.ledgersync.projection.TxOutProjection;
import org.cardanofoundation.ledgersync.repository.RedeemerDataRepository;
import org.cardanofoundation.ledgersync.repository.RedeemerRepository;
import org.cardanofoundation.ledgersync.repository.TxOutRepository;
import org.cardanofoundation.ledgersync.service.RedeemerService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                .map(redeemer -> redeemer.getData().getHash())
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

        // Track redeemer data hash usage frequency
        Map<String, Integer> redeemerDataHashUsageCountMap = new HashMap<>();

        txsWithRedeemers.forEach(aggregatedTx -> {
            Tx tx = txMap.get(aggregatedTx.getHash());
            var redeemers = aggregatedTx.getWitnesses().getRedeemers();

            redeemers.forEach(redeemerObj -> {
                Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap
                        .computeIfAbsent(aggregatedTx.getHash(), unused -> new ConcurrentHashMap<>());
                Datum plutusData = redeemerObj.getData();
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
                // Increment redeemer data hash use count
                Integer redeemerDataHashUsageCount = redeemerDataHashUsageCountMap
                        .computeIfAbsent(plutusData.getHash(), unused -> 0);
                redeemerDataHashUsageCountMap.put(plutusData.getHash(), ++redeemerDataHashUsageCount);

                // Get existing redeemer record. If it exists, update its data
                int pointerIndex = redeemerObj.getIndex();
                RedeemerTag redeemerTag = redeemerObj.getTag();
                Pair<RedeemerTag, Integer> redeemerPointer = Pair.of(redeemerTag, pointerIndex);
                Redeemer redeemerEntity = redeemerInTxMap.get(redeemerPointer);
                if (Objects.nonNull(redeemerEntity)) {
                    RedeemerData existingRedeemerData = redeemerEntity.getRedeemerData();
                    if (Objects.equals(plutusData.getHash(), existingRedeemerData.getHash())) {
                        return;
                    }
                    // Re-assign redeemer's data to new record
                    redeemerEntity.setRedeemerData(redeemerData);
                    // Decrement redeemer data hash use count
                    Integer existingRedeemerDataHashUsageCount = redeemerDataHashUsageCountMap
                            .computeIfAbsent(existingRedeemerData.getHash(), unused -> 0);
                    redeemerDataHashUsageCountMap.put(existingRedeemerData.getHash(), --existingRedeemerDataHashUsageCount);
                    return;
                }
                // Otherwise, create new redeemer record and commit
                Redeemer redeemer = buildRedeemer(redeemerObj, redeemerData, tx, aggregatedTx, newTxOutMap);
                redeemerInTxMap.put(redeemerPointer, redeemer);
                redeemersToSave.add(redeemer);
            });
        });
        redeemerDataRepository.saveAll(redeemerDataMap.values()
                .stream()
                .filter(redeemerData -> redeemerDataHashUsageCountMap.get(redeemerData.getHash()) > 0)
                .toList());
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
            com.bloxbean.cardano.yaci.core.model.Redeemer redeemerObj,
            RedeemerData redeemerData, Tx tx, AggregatedTx aggregatedTx,
            Map<Pair<String, Short>, TxOut> newTxOutMap) {
        RedeemerBuilder<?, ?> redeemerBuilder = Redeemer.builder();

        String scriptHash = getRedeemerPointer(aggregatedTx, newTxOutMap, redeemerObj);
        ExUnits exUnits = redeemerObj.getExUnits();
        redeemerBuilder.unitMem(exUnits.getMem().longValue());
        redeemerBuilder.unitSteps(exUnits.getSteps().longValue());
        redeemerBuilder.purpose(ScriptPurposeType.fromValue(redeemerObj.getTag().name().toLowerCase()));
        redeemerBuilder.index(redeemerObj.getIndex());
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
            com.bloxbean.cardano.yaci.core.model.Redeemer redeemerObj) {
        RedeemerTag tag = redeemerObj.getTag();
        int pointerIndex = redeemerObj.getIndex();

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
            case Reward -> handleRewardPtr(new ArrayList<>(aggregatedTx.getWithdrawals().keySet()),
                            pointerIndex);
            case Voting, Proposing -> null; //TODO check later
            default -> {
                log.error("Unsupported redeemer tag {}", tag);
                yield null;
            }
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
        redeemerDataBuilder.value(JsonUtil.getPrettyJson(plutusData.getJson()));
        redeemerDataBuilder.bytes(HexUtil.decodeHexString(plutusData.getCbor()));
        redeemerDataBuilder.tx(tx);

        return redeemerDataBuilder.build();
    }

}
