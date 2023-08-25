package org.cardanofoundation.ledgersync.explorerconsumer.listeners;

import com.bloxbean.cardano.yaci.store.events.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.common.common.Era;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedBlock;
import org.cardanofoundation.ledgersync.explorerconsumer.factory.BlockAggregatorServiceFactory;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockDataService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.BlockSyncService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MetricCollectorService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.RollbackService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block.BlockAggregatorServiceImpl;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block.ByronEbbAggregatorServiceImpl;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.block.ByronMainAggregatorServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockEventListener {
    private final BlockAggregatorServiceImpl blockAggregatorService;
    private final ByronEbbAggregatorServiceImpl byronEbbAggregatorService;
    private final ByronMainAggregatorServiceImpl byronMainAggregatorService;

    private final BlockAggregatorServiceFactory aggregatorServiceFactory;
    private final BlockSyncService blockSyncService;
    private final BlockDataService blockDataService;
    private final RollbackService rollbackService;

    private final BlockRepository blockRepository;
    private final MetricCollectorService metricCollectorService;

    private final AtomicInteger blockCount = new AtomicInteger(0);
    private final AtomicLong lastMessageReceivedTime = new AtomicLong(System.currentTimeMillis());
    private AtomicLong blockHeight;

    //@Value("${blocks.batch-size}")
    private Integer batchSize = 1;

    //@Value("${blocks.commitThreshold}")
    private Long commitThreshold = 1L;

    private long lastLog;

    @Value("${store.cardano.protocol-magic}")
    private int protocolMagic;

    @PostConstruct
    private void initBlockHeight() {
        long blockNo = blockRepository.getBlockHeight().orElse(0L);
        blockHeight = new AtomicLong(blockNo);
        log.info("Block height {}", blockNo);
    }


    @EventListener
    @Transactional
    public void handleBlockEvent(BlockEvent blockEvent) {
        AggregatedBlock aggregatedBlock = blockAggregatorService.aggregateBlock(blockEvent);
        handleAggregateBlock(blockEvent.getMetadata(), aggregatedBlock);
    }


    @EventListener
    @Transactional
    public void handleByronBlockEvent(ByronMainBlockEvent byronMainBlockEvent) {
        System.out.println("BlockEventListener.handleByronBlockEvent");
        AggregatedBlock aggregatedBlock = byronMainAggregatorService.aggregateBlock(byronMainBlockEvent);
        handleAggregateBlock(byronMainBlockEvent.getEventMetadata(), aggregatedBlock);
    }

    @EventListener
    @Transactional
    public void handleByronEbBlock(ByronEbBlockEvent byronEbBlockEvent) {
        System.out.println("BlockEventListener.handleByronEbBlock");
        AggregatedBlock aggregatedBlock = byronEbbAggregatorService.aggregateBlock(byronEbBlockEvent);
        handleAggregateBlock(byronEbBlockEvent.getEventMetadata(), aggregatedBlock);
    }

    @EventListener
    @Transactional
    public void handleGenesisBlock(GenesisBlockEvent genesisBlockEvent) {
        System.out.println("BlockEventListener.handleGenesisBlock");
        AggregatedBlock aggregatedBlock = AggregatedBlock.builder()
                .hash(genesisBlockEvent.getBlockHash())
                .blockNo(0L)
                .era(Era.BYRON)
                .isGenesis(true)
                .txList(Collections.emptyList())
                .build();

        EventMetadata eventMetadata = EventMetadata.builder()
                .epochSlot(0)
                .blockTime(0)
                .blockHash(genesisBlockEvent.getBlockHash())
                .build();

        handleAggregateBlock(eventMetadata, aggregatedBlock);
    }

    @EventListener
    @Transactional
    public void handleRollback(RollbackEvent rollbackEvent) {

//            if (Boolean.TRUE.equals(blockRepository.existsBlockByHash(rollbackEvent.getRollbackTo().getHash()))) {
//                //Skip this block as It can be safe block that crawler use to fetch when get rollback message
////                log.warn("Skip rollback block no {}, hash {}", eraBlock.getBlockNumber(),
////                        eraBlock.getBlockHash());
////                if (Objects.nonNull(acknowledgment)) {
////                    acknowledgment.acknowledge();
////                }
//                return;
//            } else {// The real block that need to rollback
////                if (rollbackEvent.getRollbackTo().getHash() == null)
//                    return;

        Long rollbackBlockNo = blockRepository.findBySlotNo(rollbackEvent.getRollbackTo().getSlot()) //TODO -- change in yaci store for + 1
                .map(block -> block.getBlockNo())
                .orElse(0L);

        if (rollbackBlockNo == 0) {
            log.warn("Rollback block no {}, hash {} not found", rollbackEvent.getRollbackTo().getSlot(),
                    rollbackEvent.getRollbackTo().getHash());
            return;
        }

        blockSyncService.startBlockSyncing();
        rollbackService.rollBackFrom(rollbackBlockNo);
        metricCollectorService.collectRollbackMetric();
        blockCount.set(0);
//            }

//        if (eventMetadata.getBlock() != 0) {// skip block height with ebb or genesis block
//            blockHeight.set(eventMetadata.getBlock());
//        }
//
//        //AggregatedBlock aggregatedBlock = aggregatorServiceFactory.aggregateBlock(eraBlock);
//        blockDataService.saveAggregatedBlock(aggregatedBlock);
//
//        int currentBlockCount = blockCount.incrementAndGet();
//        if ((currentBlockCount % batchSize == 0) ||
//                (lastReceivedTimeElapsed >= commitThreshold)) {
//            blockSyncService.startBlockSyncing();
////                if (Objects.nonNull(acknowledgment)) {
////                    acknowledgment.acknowledge();
////                }
//            blockCount.set(0);
//        }

    }

    private void handleAggregateBlock(EventMetadata eventMetadata, AggregatedBlock aggregatedBlock) {
        try {
            long currentTime = System.currentTimeMillis();
            long lastReceivedTimeElapsed = currentTime - lastMessageReceivedTime.getAndSet(currentTime);
            //  var eraBlock = consumerRecord.value();
            if (currentTime - lastLog >= 500) {//reduce log
                log.info("Block  number {}, slot_no {}, hash {}",
                        eventMetadata.getBlock(), eventMetadata.getSlot(), eventMetadata.getBlockHash());
                lastLog = currentTime;
            }

//            if (!eraBlock.isRollback()) {
            if (eventMetadata.getBlock() == 0) {//EBB or genesis block
                boolean isExists = blockRepository.existsBlockByHash(eventMetadata.getBlockHash());
                if (isExists) {
                    log.warn("Skip existed block : number {}, slot_no {}, hash {}",
                            eventMetadata.getBlock(),
                            eventMetadata.getSlot(), eventMetadata.getBlockHash());
//                        if (Objects.nonNull(acknowledgment)) {
//                            acknowledgment.acknowledge();
//                        }
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
//            }

            if (Boolean.TRUE.equals(blockRepository.existsBlockByHash(aggregatedBlock.getHash()))) {
                log.warn("Skip existed block : number {}, slot_no {}, hash {}",
                        eventMetadata.getBlock(),
                        eventMetadata.getSlot(), eventMetadata.getBlockHash());
                return;
            }

//            if (eraBlock.isRollback()) {
//                if (Boolean.TRUE.equals(blockRepository.existsBlockByHash(eraBlock.getBlockHash()))) {
//                    //Skip this block as It can be safe block that crawler use to fetch when get rollback message
//                    log.warn("Skip rollback block no {}, hash {}", eraBlock.getBlockNumber(),
//                            eraBlock.getBlockHash());
//                    if (Objects.nonNull(acknowledgment)) {
//                        acknowledgment.acknowledge();
//                    }
//                    return;
//                } else {// The real block that need to rollback
//                    blockSyncService.startBlockSyncing();
//                    rollbackService.rollBackFrom(eraBlock.getBlockNumber());
//                    metricCollectorService.collectRollbackMetric();
//                    blockCount.set(0);
//                }
//            }

            if (eventMetadata.getBlock() != 0) {// skip block height with ebb or genesis block
                blockHeight.set(eventMetadata.getBlock());
            }

            //AggregatedBlock aggregatedBlock = aggregatorServiceFactory.aggregateBlock(eraBlock);
            blockDataService.saveAggregatedBlock(aggregatedBlock);

//            int currentBlockCount = blockCount.incrementAndGet();
//            if ((currentBlockCount % batchSize == 0) ||
//                    (lastReceivedTimeElapsed >= commitThreshold)) {
            blockSyncService.startBlockSyncing();
//                if (Objects.nonNull(acknowledgment)) {
//                    acknowledgment.acknowledge();
//                }
            blockCount.set(0);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            System.exit(1);
        }
    }


}
