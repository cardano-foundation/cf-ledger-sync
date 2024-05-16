package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.core.model.RedeemerTag;
import com.bloxbean.cardano.yaci.core.model.certs.Certificate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.configuration.StoreProperties;
import org.cardanofoundation.ledgersync.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.dto.EUTXOWrapper;
import org.cardanofoundation.ledgersync.factory.CertificateSyncServiceFactory;
import org.cardanofoundation.ledgersync.repository.ExtraKeyWitnessRepository;
import org.cardanofoundation.ledgersync.repository.TxRepository;
import org.cardanofoundation.ledgersync.service.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    TxRepository txRepository;
    ExtraKeyWitnessRepository extraKeyWitnessRepository;

    MultiAssetService multiAssetService;
    StakeAddressService stakeAddressService;
    ParamProposalService paramProposalService;
    WithdrawalsService withdrawalsService;
    TxMetaDataService txMetaDataService;
    RedeemerService redeemerService;
    ScriptService scriptService;
    DatumService datumService;

    BlockDataService blockDataService;
    TxOutService txOutService;
    TxInService txInService;

    ReferenceInputService referenceInputService;
    AggregatedDataCachingService aggregatedDataCachingService;

    CertificateSyncServiceFactory certificateSyncServiceFactory;
    BatchCertificateDataService batchCertificateDataService;
    TxMetaDataHashService txMetaDataHashService;
    TxWitnessService txWitnessService;
    TxBootstrapWitnessService txBootstrapWitnessService;

    StoreProperties storeProperties;

    @Override
    public void prepareAndHandleTxs(Map<String, Block> blockMap,
                                    Collection<AggregatedBlock> aggregatedBlocks) {
        List<AggregatedTx> aggregatedTxList = aggregatedBlocks.stream()
                .map(AggregatedBlock::getTxList)
                .flatMap(Collection::stream)
                .toList();
        Collection<AggregatedTx> successTxs = new ConcurrentLinkedQueue<>();
        Collection<AggregatedTx> failedTxs = new ConcurrentLinkedQueue<>();

        if (CollectionUtils.isEmpty(aggregatedTxList)) {
            return;
        }

        /*
         * Handle tx metadata hash
         * key: metadata hash value: TxMetadataHash
         */
        Map<String, TxMetadataHash> metaDataHashes =
                txMetaDataHashService.handleMetaDataHash(aggregatedTxList);

        /*
         * For each aggregated tx, map it to a new tx entity, and check if the currently
         * processing aggregated tx's validity and push it to either a queue of success
         * txs or failed txs
         */
        var txMap = aggregatedTxList.stream().map(aggregatedTx -> {
            Tx tx = new Tx();
            tx.setHash(aggregatedTx.getHash());
            tx.setBlock(blockMap.get(aggregatedTx.getBlockHash()));
            tx.setBlockIndex(aggregatedTx.getBlockIndex());
            tx.setOutSum(aggregatedTx.getOutSum());
            tx.setFee(aggregatedTx.getFee());
            tx.setValidContract(aggregatedTx.isValidContract());
            tx.setDeposit(aggregatedTx.getDeposit());
            tx.setTxMetadataHash(metaDataHashes.get(aggregatedTx.getAuxiliaryDataHash()));
            if (aggregatedTx.isValidContract()) {
                successTxs.add(aggregatedTx);
            } else {
                failedTxs.add(aggregatedTx);
            }
            return tx;
        }).collect(Collectors.toConcurrentMap(Tx::getHash, Function.identity()));

        // Transaction records need to be saved in sequential order to ease out future queries
        txRepository.saveAll(txMap.values()
                .stream()
                .sorted((tx1, tx2) -> {
                    Long tx1BlockNo = null;
                    Long tx2BlockNo = null;
                    if (Objects.nonNull(tx1.getBlock()) && Objects.nonNull(tx2.getBlock())) {
                        tx1BlockNo = tx1.getBlock().getBlockNo();
                        tx2BlockNo = tx2.getBlock().getBlockNo();
                    }

                    Long tx1BlockIndex = tx1.getBlockIndex();
                    Long tx2BlockIndex = tx2.getBlockIndex();

                    if (Objects.equals(tx1BlockNo, tx2BlockNo)) {
                        return tx1BlockIndex.compareTo(tx2BlockIndex);
                    }

                    return tx1BlockNo.compareTo(tx2BlockNo);
                })
                .toList());
        aggregatedDataCachingService.addTxCount(txMap.size());

        scriptService.handleScripts(aggregatedTxList, txMap);
        handleTxWitnesses(aggregatedTxList, txMap);
        Map<String, Datum> datumMap = datumService.handleDatum(aggregatedTxList, txMap); //TODO refactor
        handleTxs(successTxs, failedTxs, txMap, datumMap);
    }

    private void handleTxs(Collection<AggregatedTx> successTxs,
                           Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap,
                           Map<String, Datum> datumMap) {

        // Handle extra key witnesses from required signers
        handleExtraKeyWitnesses(successTxs, failedTxs, txMap);

        // Handle Tx contents
        handleTxContents(successTxs, failedTxs, txMap, datumMap);
    }

    private void handleTxContents(Collection<AggregatedTx> successTxs,
                                  Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap,
                                  Map<String, Datum> datumMap) {
        if (CollectionUtils.isEmpty(successTxs) && CollectionUtils.isEmpty(failedTxs)) {
            return;
        }

        // MUST SET FIRST
        // multi asset mint
        if (!storeProperties.getAssets().isEnabled()) {
            multiAssetService.handleMultiAssetMint(successTxs, txMap);
        }

        // Handle stake address and its first appeared tx
        Map<String, StakeAddress> stakeAddressMap = stakeAddressService
                .handleStakeAddressesFromTxs(blockDataService.getStakeAddressTxHashMap(), txMap);

        // tx out
        EUTXOWrapper wrappedTxOut =
                txOutService.prepareTxOuts(buildAggregatedTxOutMap(successTxs), txMap, stakeAddressMap, datumMap);

        // handle collateral out as tx out for failed txs
        if (!CollectionUtils.isEmpty(failedTxs)) {
            wrappedTxOut.addAll(txOutService.prepareTxOuts(
                    buildCollateralTxOutMap(failedTxs), txMap, stakeAddressMap, datumMap));
        }

        // Create an uncommitted tx out map and multi asset tx out map to allow other methods to use
        Map<Pair<String, Short>, TxOut> newTxOutMap = wrappedTxOut.getTxOuts()
                .stream()
                .collect(Collectors.toConcurrentMap(
                        txOut -> Pair.of(txOut.getTx().getHash(), txOut.getIndex()),
                        Function.identity()
                ));
        Map<Long, List<MaTxOut>> newMaTxOutMap = wrappedTxOut.getMaTxOuts()
                .stream()
                .collect(Collectors.groupingByConcurrent(
                        maTxOut -> maTxOut.getTxOut().getId(),
                        Collectors.toList()
                ));

        // redeemer
        Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap =
                redeemerService.handleRedeemers(successTxs, txMap, newTxOutMap);
        redeemersMap.putAll(redeemerService.handleRedeemers(failedTxs, txMap, newTxOutMap));

        // tx in
        txInService.handleTxIns(successTxs,
                buildTxInsMap(successTxs), txMap, newTxOutMap, newMaTxOutMap, redeemersMap);

        // handle collateral input as tx in
        txInService.handleTxIns(failedTxs,
                buildCollateralTxInsMap(failedTxs), txMap, newTxOutMap,
                newMaTxOutMap, Collections.emptyMap());


        // auxiliary
        if (!storeProperties.getMetadata().isEnabled()) {
            txMetaDataService.handleAuxiliaryDataMaps(txMap);
        }

        //param proposal
        paramProposalService.handleParamProposals(successTxs, txMap);

        // reference inputs
        referenceInputService.handleReferenceInputs(
                buildReferenceTxInsMap(successTxs), txMap, newTxOutMap);

        // certificates
        handleCertificates(successTxs, txMap, redeemersMap, stakeAddressMap);

        // Withdrawals
        withdrawalsService.handleWithdrawal(successTxs, txMap, stakeAddressMap, redeemersMap);

        // Unconsumed tx in
        txInService.handleUnconsumeTxIn(
                buildCollateralTxInsMap(successTxs), newTxOutMap, txMap, redeemersMap);

        txInService.handleUnconsumeTxIn(buildTxInsMap(failedTxs), newTxOutMap, txMap, redeemersMap);

        // Failed tx outs:
        // - Collateral return if tx success
        // - Tx out if tx failed
        txOutService.handleFailedTxOuts(successTxs, failedTxs, txMap, stakeAddressMap, datumMap);
    }

    private Map<String, Set<AggregatedTxIn>> buildTxInsMap(Collection<AggregatedTx> txList) {
        return txList.stream()
                .collect(Collectors.toConcurrentMap(
                        AggregatedTx::getHash, AggregatedTx::getTxInputs, (a, b) -> a));
    }

    private Map<String, Set<AggregatedTxIn>> buildCollateralTxInsMap(
            Collection<AggregatedTx> txList) {
        return txList.stream()
                .filter(tx -> !CollectionUtils.isEmpty(tx.getCollateralInputs()))
                .collect(Collectors.toConcurrentMap(
                        AggregatedTx::getHash, AggregatedTx::getCollateralInputs, (a, b) -> a));
    }

    private Map<String, Set<AggregatedTxIn>> buildReferenceTxInsMap(
            Collection<AggregatedTx> txList) {
        return txList.stream()
                .filter(tx -> !CollectionUtils.isEmpty(tx.getReferenceInputs()))
                .collect(Collectors.toConcurrentMap(
                        AggregatedTx::getHash, AggregatedTx::getReferenceInputs, (a, b) -> a));
    }

    private Map<String, List<AggregatedTxOut>> buildAggregatedTxOutMap(
            Collection<AggregatedTx> txList) {
        return txList.stream()
                .filter(tx -> !CollectionUtils.isEmpty(tx.getTxOutputs()))
                .collect(Collectors.toConcurrentMap(
                        AggregatedTx::getHash, AggregatedTx::getTxOutputs, (a, b) -> a));
    }

    private Map<String, List<AggregatedTxOut>> buildCollateralTxOutMap(
            Collection<AggregatedTx> txList) {
        return txList.stream()
                .filter(tx -> Objects.nonNull(tx.getCollateralReturn()))
                .collect(Collectors.toConcurrentMap(
                        AggregatedTx::getHash,
                        tx -> List.of(tx.getCollateralReturn()),
                        (a, b) -> a));
    }

    private void handleCertificates(Collection<AggregatedTx> successTxs, Map<String, Tx> txMap,
                                    Map<String, Map<Pair<RedeemerTag, Integer>, Redeemer>> redeemersMap,
                                    Map<String, StakeAddress> stakeAddressMap) {
        successTxs.forEach(aggregatedTx -> {
            String txHash = aggregatedTx.getHash();
            Tx tx = txMap.get(txHash);
            if (CollectionUtils.isEmpty(aggregatedTx.getCertificates())) {
                return;
            }

            IntStream.range(0, aggregatedTx.getCertificates().size()).forEach(idx -> {
                Certificate certificate = aggregatedTx.getCertificates().get(idx);

                // Only stake de-registration and stake delegation have redeemers
                Map<Pair<RedeemerTag, Integer>, Redeemer> redeemerInTxMap = redeemersMap.get(txHash);

                Redeemer redeemer = null;
                if (!CollectionUtils.isEmpty(redeemerInTxMap)) {
                    redeemer = redeemerInTxMap.get(Pair.of(RedeemerTag.Cert, idx));
                }

                AggregatedBlock aggregatedBlock = blockDataService
                        .getAggregatedBlock(aggregatedTx.getBlockHash());
                certificateSyncServiceFactory.handle(
                        aggregatedBlock, certificate, idx, tx, redeemer, stakeAddressMap);
            });
        });

        batchCertificateDataService.saveAllAndClearBatchData();
    }

    public void handleExtraKeyWitnesses(Collection<AggregatedTx> successTxs,
                                        Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap) {

        Map<String, Tx> mWitnessTx = new ConcurrentHashMap<>();
        Set<String> hashCollection = new ConcurrentSkipListSet<>();

        /*
         * Map all extra key witnesses hashes to its respective tx and add them to a set
         * which will be used to find all existing hashes from database. The existing hashes
         * will be opted out
         *
         * This process will be done asynchronously
         */
        Stream.concat(successTxs.parallelStream(), failedTxs.parallelStream())
                .filter(aggregatedTx -> !CollectionUtils.isEmpty(aggregatedTx.getRequiredSigners()))
                .forEach(aggregatedTx -> {
                    Tx tx = txMap.get(aggregatedTx.getHash());
                    aggregatedTx.getRequiredSigners().parallelStream().forEach(hash -> {
                        mWitnessTx.put(hash, tx);
                        hashCollection.add(hash);
                    });
                });

        if (CollectionUtils.isEmpty(hashCollection)) {
            return;
        }

        Set<String> existsWitnessKeys = extraKeyWitnessRepository.findByHashIn(hashCollection);

        // Opt out all existing hashes
        hashCollection.removeAll(existsWitnessKeys);

        // Create new extra key witnesses records
        List<ExtraKeyWitness> extraKeyWitnesses = hashCollection.stream()
                .map(hash -> ExtraKeyWitness.builder()
                        .hash(hash)
                        .tx(mWitnessTx.get(hash))
                        .build())
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(extraKeyWitnesses)) {
            extraKeyWitnessRepository.saveAll(extraKeyWitnesses);
        }
    }

    public void handleTxWitnesses(Collection<AggregatedTx> aggregatedTxList, Map<String, Tx> txMap) {
        txWitnessService.handleTxWitness(aggregatedTxList, txMap);
        txBootstrapWitnessService.handleBootstrapWitnesses(aggregatedTxList, txMap);
    }
}
