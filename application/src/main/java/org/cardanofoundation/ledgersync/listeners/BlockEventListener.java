package org.cardanofoundation.ledgersync.listeners;

import com.bloxbean.cardano.yaci.store.events.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.repository.BlockRepository;
import org.cardanofoundation.ledgersync.service.*;
import org.cardanofoundation.ledgersync.service.impl.block.BlockAggregatorServiceImpl;
import org.cardanofoundation.ledgersync.service.impl.block.ByronEbbAggregatorServiceImpl;
import org.cardanofoundation.ledgersync.service.impl.block.ByronMainAggregatorServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockEventListener {
    private final BlockAggregatorServiceImpl blockAggregatorService;
    private final ByronEbbAggregatorServiceImpl byronEbbAggregatorService;
    private final ByronMainAggregatorServiceImpl byronMainAggregatorService;

    private final GenesisDataService genesisDataService;

    private final BlockSyncService blockSyncService;
    private final BlockDataService blockDataService;
    private final RollbackService rollbackService;

    private final BlockRepository blockRepository;
    private final MetricCollectorService metricCollectorService;
    private final HealthCheckCachingService healthCheckCachingService;
    private final AtomicInteger blockCount = new AtomicInteger(0);

    @Value("${blocks.batch-size}")
    private Integer batchSize;
    @Value("${blocks.commitThreshold}")
    private Long commitThreshold;

    private final AtomicLong lastMessageReceivedTime = new AtomicLong(System.currentTimeMillis());
    private AtomicLong blockHeight;
    private long lastLog;

    @PostConstruct
    private void initBlockHeight() {
        long blockNo = blockRepository.getBlockHeight().orElse(0L);
        blockHeight = new AtomicLong(blockNo);
        log.info("Block height {}", blockNo);
    }

    @EventListener
    @Transactional
    public void handleBlockEvent(BlockEvent blockEvent) {
        if (checkIfBlockExists(blockEvent.getMetadata())) return;

        AggregatedBlock aggregatedBlock = blockAggregatorService.aggregateBlock(blockEvent);
        handleAggregateBlock(blockEvent.getMetadata(), aggregatedBlock);
    }

    @EventListener
    @Transactional
    public void handleByronBlockEvent(ByronMainBlockEvent byronMainBlockEvent) {
        if (checkIfBlockExists(byronMainBlockEvent.getMetadata())) return;

        AggregatedBlock aggregatedBlock = byronMainAggregatorService.aggregateBlock(byronMainBlockEvent);
        handleAggregateBlock(byronMainBlockEvent.getMetadata(), aggregatedBlock);
    }

    @EventListener
    @Transactional
    public void handleByronEbBlock(ByronEbBlockEvent byronEbBlockEvent) {
        if (checkIfBlockExists(byronEbBlockEvent.getMetadata())) return;

        AggregatedBlock aggregatedBlock = byronEbbAggregatorService.aggregateBlock(byronEbBlockEvent);
        handleAggregateBlock(byronEbBlockEvent.getMetadata(), aggregatedBlock);
    }

    @EventListener
    @Transactional
    public void handleGenesisBlock(GenesisBlockEvent genesisBlockEvent) {
        log.info("BlockEventListener.handleGenesisBlock");
        String genesisHash = genesisBlockEvent.getBlockHash();
        if (genesisHash != null && genesisHash.startsWith("Genesis")) {
            //Yaci store returns genesis hash as "Genesis" when it is not able to find it. It happens for preview/sanchonet network
            genesisHash = null;
        }

        genesisDataService.setupData(genesisHash);
        healthCheckCachingService.saveLatestBlockSlot(genesisBlockEvent.getSlot());
        healthCheckCachingService.saveLatestBlockInsertTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestBlockTime(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(genesisBlockEvent.getBlockTime()), ZoneId.of("UTC")));
    }

    @EventListener
    @Transactional
    public void handleRollback(RollbackEvent rollbackEvent) {
        long rollbackBlockNo = 0;
        var rollBackBlock = blockRepository.findBySlotNo(rollbackEvent.getRollbackTo().getSlot());

        if (rollBackBlock.isPresent()) {
            rollbackBlockNo = rollBackBlock.get().getBlockNo();
        }

        if (rollbackBlockNo == 0) {
            log.warn("Rollback block no {}, hash {} not found", rollbackEvent.getRollbackTo().getSlot(),
                    rollbackEvent.getRollbackTo().getHash());
            return;
        }

        blockSyncService.startBlockSyncing();
        rollbackService.rollBackFrom(rollbackBlockNo);
        metricCollectorService.collectRollbackMetric();
        blockCount.set(0);

        healthCheckCachingService.saveLatestBlockSlot(rollbackEvent.getRollbackTo().getSlot());
        healthCheckCachingService.saveLatestBlockInsertTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestBlockTime(rollBackBlock.get().getTime().toLocalDateTime());
    }

    private boolean checkIfBlockExists(EventMetadata metadata) {
        var optional = blockRepository.findBlockByHash(metadata.getBlockHash());
        if (optional.isPresent()) {
            log.info("Block already exists. Skipping block no {}, hash {}", metadata.getEpochSlot(),
                    metadata.getBlockHash());
            return true;
        }
        return false;
    }

    private void handleAggregateBlock(EventMetadata eventMetadata, AggregatedBlock aggregatedBlock) {
        try {
            long currentTime = System.currentTimeMillis();
            long lastReceivedTimeElapsed = currentTime - lastMessageReceivedTime.getAndSet(currentTime);

            if (currentTime - lastLog >= 500) {//reduce log
                log.info("Block  number {}, slot_no {}, hash {}",
                        eventMetadata.getBlock(), eventMetadata.getSlot(), eventMetadata.getBlockHash());
                lastLog = currentTime;
            }

            if (eventMetadata.getBlock() == 0) {//EBB or genesis block
                boolean isExists = blockRepository.existsBlockByHash(eventMetadata.getBlockHash());
                if (isExists) {
                    log.warn("Skip existed block : number {}, slot_no {}, hash {}",
                            eventMetadata.getBlock(),
                            eventMetadata.getSlot(), eventMetadata.getBlockHash());
                    return;
                }
            } else if (eventMetadata.getBlock() <= blockHeight.get()) {
//                    log.warn("Skip block {}, hash {}, slot {} smaller than current block height {}",
//                            eventMetadata.getBlock(), eventMetadata.getBlockHash(), eventMetadata.getSlot(),
//                            blockHeight.get());
////                    if (Objects.nonNull(acknowledgment)) {
////                        acknowledgment.acknowledge();
////                    }
//                    return;
            }

            if (Boolean.TRUE.equals(blockRepository.existsBlockByHash(aggregatedBlock.getHash()))) {
                log.warn("Skip existed block : number {}, slot_no {}, hash {}",
                        eventMetadata.getBlock(),
                        eventMetadata.getSlot(), eventMetadata.getBlockHash());
                return;
            }

            if (eventMetadata.getBlock() != 0) {// skip block height with ebb or genesis block
                blockHeight.set(eventMetadata.getBlock());
            }

            //AggregatedBlock aggregatedBlock = aggregatorServiceFactory.aggregateBlock(eraBlock);
            blockDataService.saveAggregatedBlock(aggregatedBlock);
            int currentBlockCount = blockCount.incrementAndGet();
            if (currentBlockCount % batchSize == 0 || lastReceivedTimeElapsed >= commitThreshold || eventMetadata.isSyncMode()) {
                blockSyncService.startBlockSyncing();
                healthCheckCachingService.saveIsSyncMode(eventMetadata.isSyncMode());
                blockCount.set(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            System.exit(1);
        }
    }

}
