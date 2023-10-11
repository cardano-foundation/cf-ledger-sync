package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block;

import io.micrometer.core.annotation.Timed;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.explorer.consumercommon.entity.Block;
import org.cardanofoundation.explorer.consumercommon.entity.SlotLeader;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedSlotLeader;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

import static org.cardanofoundation.ledgersync.explorerconsumer.constant.ConsumerConstant.getNetworkNotStartWithByron;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockSyncServiceImpl implements BlockSyncService {

    BlockRepository blockRepository;
    TxRepository txRepository;

    TransactionService transactionService;
    BlockDataService blockDataService;
    SlotLeaderService slotLeaderService;
    EpochService epochService;
    EpochParamService epochParamService;
    TxChartService txChartService;
    MetricCollectorService metricCollectorService;
    AggregatedDataCachingService aggregatedDataCachingService;


    @Override
    @Transactional
    @Timed(value = "consumer.block.processing.timer", description = "Time spent syncing blocks")
    public void startBlockSyncing() {
        if (blockDataService.getBlockSize() == 0) {
            return;
        }
        this.handleBlockSync();
    }

    public void handleBlockSync() {
        Map<String, Block> blockMap = new LinkedHashMap<>();
        var firstAndLastBlock = blockDataService.getFirstAndLastBlock();

        AggregatedBlock firstBlock = firstAndLastBlock.getFirst();
        AggregatedBlock lastBlock = firstAndLastBlock.getSecond();

//    log.trace("Commit from block {} to block {} ",
//        firstBlock.getBlockNo(),
//        lastBlock.getBlockNo());

        metricCollectorService.collectCountBlockProcessingMetric(firstBlock, lastBlock);
        metricCollectorService.collectCurrentBlockMetric(lastBlock);
        metricCollectorService.collectCurrentEraMetric(lastBlock);
        metricCollectorService.collectNetworkSyncPercentMetric(lastBlock);
        metricCollectorService.collectEraAndEpochProcessingMetric(lastBlock);

        // Initialize block entities
        Collection<AggregatedBlock> allAggregatedBlocks = blockDataService.getAllAggregatedBlocks();
        allAggregatedBlocks.forEach(aggregatedBlock -> handleBlock(aggregatedBlock, blockMap));
        blockRepository.saveAll(blockMap.values());
        aggregatedDataCachingService.addBlockCount(allAggregatedBlocks.size());

        // Prepare and handle transaction contents
        Tx latestSavedTx = txRepository.findFirstByOrderByIdDesc();
        transactionService.prepareAndHandleTxs(blockMap, allAggregatedBlocks);

        // Handle epoch data
        epochService.handleEpoch(allAggregatedBlocks);

        // Handle epoch param
        epochParamService.handleEpochParams(firstBlock.getNetwork());

        // Cache latest txs
        aggregatedDataCachingService.saveLatestTxs();

        // Handle tx charts
        txChartService.handleTxChart(latestSavedTx);

        // Finally, commit and clear the aggregated data
        blockDataService.clearBatchBlockData();
        aggregatedDataCachingService.commit();
    }

    private void handleBlock(AggregatedBlock aggregatedBlock, Map<String, Block> blockMap) {
        Block block = new Block();

        block.setHash(aggregatedBlock.getHash());
        block.setEpochNo(aggregatedBlock.getEpochNo());
        block.setEpochSlotNo(aggregatedBlock.getEpochSlotNo());
        block.setSlotNo(aggregatedBlock.getSlotNo());
        block.setBlockNo(aggregatedBlock.getBlockNo());

        if (Boolean.FALSE.equals(aggregatedBlock.getIsGenesis())
                && aggregatedBlock.getBlockNo() != null && aggregatedBlock.getBlockNo() != 0) {
            Optional.ofNullable(blockMap.get(aggregatedBlock.getPrevBlockHash())) //TODO refactor
                    .or(() -> blockRepository.findBlockByHash(aggregatedBlock.getPrevBlockHash()))
                    .ifPresentOrElse(block::setPrevious, () -> {
                        if (aggregatedBlock.getBlockNo().equals(BigInteger.ZERO.longValue()) &&
                                getNetworkNotStartWithByron().contains(aggregatedBlock.getNetwork())) {
                            return;
                        }

                        log.error(
                                "Prev block not found. Block number: {}, block hash: {}, prev hash: {}",
                                aggregatedBlock.getBlockNo(), aggregatedBlock.getHash(),
                                aggregatedBlock.getPrevBlockHash());
                        throw new IllegalStateException();
                    });
        }

        AggregatedSlotLeader aggregatedSlotLeader = aggregatedBlock.getSlotLeader();
        SlotLeader slotLeader = getSlotLeader(aggregatedSlotLeader);
        block.setSlotLeader(slotLeader);

        block.setSize(aggregatedBlock.getBlockSize());
        block.setTime(aggregatedBlock.getBlockTime());
        block.setTxCount(aggregatedBlock.getTxCount());
        block.setProtoMajor(aggregatedBlock.getProtoMajor());
        block.setProtoMinor(aggregatedBlock.getProtoMinor());
        block.setVrfKey(aggregatedBlock.getVrfKey());
        block.setOpCert(aggregatedBlock.getOpCert());
        block.setOpCertCounter(aggregatedBlock.getOpCertCounter());

        blockMap.put(block.getHash(), block);
    }

    private SlotLeader getSlotLeader(AggregatedSlotLeader aggregatedSlotLeader) {
        if (Objects.isNull(aggregatedSlotLeader)) {
            // If aggregated slot leader is null, current block is Byron EB block
            return SlotLeader.builder().id(1L).build(); // hard-code for now
        }

        return slotLeaderService.getSlotLeader(
                aggregatedSlotLeader.getHashRaw(), aggregatedSlotLeader.getPrefix());
    }
}
