package org.cardanofoundation.ledgersync.aggregate.account.service.impl.block;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedBlock;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockDataService;
import org.cardanofoundation.ledgersync.aggregate.account.service.BlockSyncService;
import org.cardanofoundation.ledgersync.aggregate.account.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockSyncServiceImpl implements BlockSyncService {

    TransactionService transactionService;
    BlockDataService blockDataService;

    @Override
    @Transactional
//    @Timed(value = "consumer.block.processing.timer", description = "Time spent syncing blocks")
    public void startBlockSyncing() {
        if (blockDataService.getBlockSize() == 0) {
            return;
        }
        this.handleBlockSync();
    }

    public void handleBlockSync() {
//        Map<String, Block> blockMap = new LinkedHashMap<>();
        var firstAndLastBlock = blockDataService.getFirstAndLastBlock();

        AggregatedBlock firstBlock = firstAndLastBlock.getFirst();
        AggregatedBlock lastBlock = firstAndLastBlock.getSecond();

        log.trace("Commit from block {} to block {} ",
                firstBlock.getBlockNo(),
                lastBlock.getBlockNo());

        // Initialize block entities
        Collection<AggregatedBlock> allAggregatedBlocks = blockDataService.getAllAggregatedBlocks();
//        allAggregatedBlocks.forEach(aggregatedBlock -> handleBlock(aggregatedBlock, blockMap));
//        blockRepository.saveAll(blockMap.values());
//        aggregatedDataCachingService.addBlockCount(allAggregatedBlocks.size());

        // Prepare and handle transaction contents
//        Tx latestSavedTx = txRepository.findFirstByOrderByIdDesc();
        transactionService.prepareAndHandleTxs(allAggregatedBlocks);

//        // Handle epoch data
//        epochService.handleEpoch(allAggregatedBlocks);
//
//        // Handle epoch param
//        epochParamService.handleEpochParams();
//
//        // Cache latest txs
//        aggregatedDataCachingService.saveLatestTxs();
//
//        // Handle tx charts
//        txChartService.handleTxChart(latestSavedTx);

        // Finally, commit and clear the aggregated data
        blockDataService.clearBatchBlockData();
//        aggregatedDataCachingService.commit();
    }

//    private void handleBlock(AggregatedBlock aggregatedBlock, Map<String, Block> blockMap) {
//        Block block = new Block();
//
//        block.setHash(aggregatedBlock.getHash());
//        block.setEpochNo(aggregatedBlock.getEpochNo());
//        block.setEpochSlotNo(aggregatedBlock.getEpochSlotNo());
//        block.setSlotNo(aggregatedBlock.getSlotNo());
//        block.setBlockNo(aggregatedBlock.getBlockNo());
//
//        if (Boolean.FALSE.equals(aggregatedBlock.getIsGenesis())
//                && aggregatedBlock.getBlockNo() != null && aggregatedBlock.getBlockNo() != 0) {
////            Optional.ofNullable(blockMap.get(aggregatedBlock.getPrevBlockHash())) //TODO refactor
////                    .or(() -> blockRepository.findBlockByHash(aggregatedBlock.getPrevBlockHash()))
////                    .ifPresentOrElse(block::setPrevious, () -> {
////                        if (aggregatedBlock.getBlockNo().equals(BigInteger.ZERO.longValue()) &&
////                                getNetworkNotStartWithByron().contains(aggregatedBlock.getNetwork())) {
////                            return;
////                        }
////
////                        log.error(
////                                "Prev block not found. Block number: {}, block hash: {}, prev hash: {}",
////                                aggregatedBlock.getBlockNo(), aggregatedBlock.getHash(),
////                                aggregatedBlock.getPrevBlockHash());
////                        throw new IllegalStateException();
////                    });
//        }
//
//        AggregatedSlotLeader aggregatedSlotLeader = aggregatedBlock.getSlotLeader();
////        SlotLeader slotLeader = getSlotLeader(aggregatedSlotLeader);
////        block.setSlotLeader(slotLeader);
//
//        block.setSize(aggregatedBlock.getBlockSize());
//        block.setTime(aggregatedBlock.getBlockTime());
//        block.setTxCount(aggregatedBlock.getTxCount());
//        block.setProtoMajor(aggregatedBlock.getProtoMajor());
//        block.setProtoMinor(aggregatedBlock.getProtoMinor());
//        block.setVrfKey(aggregatedBlock.getVrfKey());
//        block.setOpCert(aggregatedBlock.getOpCert());
//        block.setOpCertCounter(aggregatedBlock.getOpCertCounter());
//
//        blockMap.put(block.getHash(), block);
//    }

}
