package org.cardanofoundation.ledgersync.aggregate.account.service.impl;

import com.bloxbean.cardano.client.quicktx.Tx;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTokenRepository;
import org.cardanofoundation.ledgersync.aggregate.account.repository.AddressTxBalanceRepository;
import org.cardanofoundation.ledgersync.aggregate.account.service.AddressBalanceService;
import org.cardanofoundation.ledgersync.aggregate.account.service.MultiAssetService;
import org.cardanofoundation.ledgersync.aggregate.account.service.RollbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RollbackServiceImpl implements RollbackService {

    AddressTokenRepository addressTokenRepository;
    AddressTxBalanceRepository addressTxBalanceRepository;
    AddressBalanceService addressBalanceService;
    MultiAssetService multiAssetService;

    @Override
    @Transactional
    public void rollBackFrom(long blockNo) {
        log.info("---------------------------------------------------------------------");
        log.warn("Roll back from block no {}", blockNo);
//        Optional<Block> blockRollBack = blockRepository.findBlockByBlockNo(blockNo);
//        if (blockRollBack.isEmpty()) {
//            log.warn("Block {} for roll back not found", blockNo);
//        } else {
//            startRollback(blockRollBack.get());
//        }
//        startRollback(blockRollBack.get());
        log.info("---------------------------------------------------------------------");
//        aggregatedDataCachingService.saveLatestTxs();
//        aggregatedDataCachingService.commit();
    }

//    private void startRollback(Block rollbackBlock) {
//        var blocksForRollback = blockRepository
//                .findAllByBlockNoGreaterThanOrderByBlockNoDesc(rollbackBlock.getBlockNo());
        //blocksForRollback.add(rollbackBlock);
        //var lastRollbackBlock = blocksForRollback.get(0);

//        log.info("Blocks being rolled back: {}", blocksForRollback.size());
//        blocksForRollback.forEach(block ->
//                log.info("- Block id: {}, block number {}, block hash {}",
//                        block.getId(), block.getBlockNo(), block.getHash()));

        // Rollback epoch stats
//        epochService.rollbackEpochStats(blocksForRollback);

        // Remove all rolled back block data
//        removeAllRollbackBlockData(blocksForRollback);

//        var rollbackHistory = blocksForRollback.stream()
//                .map(this::buildRollbackHistory)
//                .toList();
//        rollbackHistoryRepository.saveAll(rollbackHistory);

        //log.info("Roll back to block no {}", lastRollbackBlock.getBlockNo());
//        int rollbackBlocksCount = blocksForRollback.size();
//        aggregatedDataCachingService.subtractBlockCount(blocksForRollback.size());
//        blockRepository.deleteAll(blocksForRollback);
//        log.info("Rollback finished: {} blocks were rolled back", rollbackBlocksCount);
//    }

//    private RollbackHistory buildRollbackHistory(Block rollbackBlock) {
//        return RollbackHistory.builder()
//                .blockNo(rollbackBlock.getBlockNo())
//                .blockHash(rollbackBlock.getHash())
//                .slotNo(rollbackBlock.getSlotNo())
//                .build();
//    }

//    private void removeAllRollbackBlockData(List<Block> rollbackBlocks) {
//        List<Tx> txsForRollback = txRepository.findAllByBlockIn(rollbackBlocks);
//        log.info("Txs being rolled back: {}", txsForRollback.size());
//        if (CollectionUtils.isEmpty(txsForRollback)) {
//            return;
//        }
//
//        txsForRollback.forEach(tx ->
//                log.info("- Tx id: {}, hash: {}, block id: {}",
//                        tx.getId(), tx.getHash(), tx.getBlockId()));
//
//        addressBalanceService.rollbackAddressBalances(txsForRollback);
//        multiAssetService.rollbackMultiAssets(txsForRollback);
//        txChartService.rollbackTxChart(txsForRollback);
//        aggregatedDataCachingService.subtractTxCount(txsForRollback.size());
//
//        log.info("Deleting records from tables related to txs/blocks being rolled back");
//        addressTokenRepository.deleteAllByTxIn(txsForRollback);
//        addressTxBalanceRepository.deleteAllByTxIn(txsForRollback);
//        datumRepository.deleteAllByTxIn(txsForRollback);
//        delegationRepository.deleteAllByTxIn(txsForRollback);
//        extraKeyWitnessRepository.deleteAllByTxIn(txsForRollback);
//        failedTxOutRepository.deleteAllByTxIn(txsForRollback);
//        maTxMintRepository.deleteAllByTxIn(txsForRollback);
//        multiAssetTxOutRepository.deleteAllByTxOutTxIn(txsForRollback);
//        epochParamRepository.deleteAllByBlockIn(rollbackBlocks);
//        paramProposalRepository.deleteAllByRegisteredTxIn(txsForRollback);
//        poolMetadataRefRepository.deleteAllByRegisteredTxIn(txsForRollback);
//        poolOwnerRepository.deleteAllByPoolUpdateRegisteredTxIn(txsForRollback);
//        poolRelayRepository.deleteAllByPoolUpdateRegisteredTxIn(txsForRollback);
//        poolRetireRepository.deleteAllByAnnouncedTxIn(txsForRollback);
//        poolUpdateRepository.deleteAllByRegisteredTxIn(txsForRollback);
//        potTransferRepository.deleteAllByTxIn(txsForRollback);
//        redeemerRepository.deleteAllByTxIn(txsForRollback);
//        redeemerDataRepository.deleteAllByTxIn(txsForRollback);
//        referenceInputRepository.deleteAllByTxInIn(txsForRollback);
//        reserveRepository.deleteAllByTxIn(txsForRollback);
//        scriptRepository.deleteAllByTxIn(txsForRollback);
//        stakeDeregistrationRepository.deleteAllByTxIn(txsForRollback);
//        stakeRegistrationRepository.deleteAllByTxIn(txsForRollback);
//        treasuryRepository.deleteAllByTxIn(txsForRollback);
//        txInRepository.deleteAllByTxInputIn(txsForRollback);
//        txMetadataRepository.deleteAllByTxIn(txsForRollback);
//        txOutRepository.deleteAllByTxIn(txsForRollback);
//        unconsumeTxInRepository.deleteAllByTxInIn(txsForRollback);
//        withdrawalRepository.deleteAllByTxIn(txsForRollback);
//        txBootstrapWitnessRepository.deleteAllByTxIn(txsForRollback);
//        txWitnessRepository.deleteAllByTxIn(txsForRollback);
//
//        int txsForRollbackCount = txsForRollback.size();
//        log.info("Deleting {} txs", txsForRollbackCount);
//        txRepository.deleteAll(txsForRollback);
//        log.info("Deleted {} txs", txsForRollbackCount);
//    }
}
