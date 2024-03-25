package org.cardanofoundation.ledgersync.healthcheck.listener;

import com.bloxbean.cardano.yaci.store.events.*;
import com.bloxbean.cardano.yaci.store.events.internal.BatchBlocksProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.healthcheck.service.HealthCheckCachingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
@Slf4j
@Component
@ConditionalOnExpression("${ledgersync.healthcheck.enabled:true}")
public class BlockListener {
    private final HealthCheckCachingService healthCheckCachingService;

    @EventListener
    public void handleRollback(RollbackEvent rollbackEvent) {
        healthCheckCachingService.saveLatestEventTime(LocalDateTime.now(ZoneOffset.UTC));
    }

    @EventListener
    public void handleBlockHeader(BlockHeaderEvent blockHeaderEvent) {
        healthCheckCachingService.saveLatestEventTime(LocalDateTime.now(ZoneOffset.UTC));

        if (!blockHeaderEvent.getMetadata().isParallelMode()) {
            healthCheckCachingService.saveLatestSlotNo(blockHeaderEvent.getMetadata().getBlock());
            healthCheckCachingService.saveLatestBlockTime(LocalDateTime.ofEpochSecond(blockHeaderEvent.getMetadata().getBlockTime(),
                    0, ZoneOffset.ofHours(0)));
        }
    }

    @EventListener
    public void handleGenesisBlock(GenesisBlockEvent genesisBlockEvent) {
        healthCheckCachingService.saveLatestEventTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(genesisBlockEvent.getSlot());
        healthCheckCachingService.saveLatestBlockTime(LocalDateTime.ofEpochSecond(genesisBlockEvent.getBlockTime(),
                0, ZoneOffset.ofHours(0)));
    }

    @EventListener
    public void handleByronBlockEvent(ByronMainBlockEvent byronMainBlockEvent) {
        healthCheckCachingService.saveLatestEventTime(LocalDateTime.now(ZoneOffset.UTC));

        if (!byronMainBlockEvent.getMetadata().isParallelMode()) {
            healthCheckCachingService.saveLatestSlotNo(byronMainBlockEvent.getMetadata().getSlot());
            healthCheckCachingService.saveLatestBlockTime(LocalDateTime.ofEpochSecond(byronMainBlockEvent.getMetadata().getBlockTime(),
                    0, ZoneOffset.ofHours(0)));
        }
    }

    @EventListener
    public void handleByronEbBlock(ByronEbBlockEvent byronEbBlockEvent) {
        healthCheckCachingService.saveLatestEventTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(byronEbBlockEvent.getMetadata().getSlot());
        healthCheckCachingService.saveLatestBlockTime(LocalDateTime.ofEpochSecond(byronEbBlockEvent.getMetadata().getBlockTime(),
                0, ZoneOffset.ofHours(0)));
    }

    @EventListener
    // for parallel mode
    public void handleBatchBlocksProcessedEvent(BatchBlocksProcessedEvent batchBlocksProcessedEvent) {
        healthCheckCachingService.saveLatestEventTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(batchBlocksProcessedEvent.getMetadata().getSlot());
        healthCheckCachingService.saveLatestBlockTime(LocalDateTime.ofEpochSecond(batchBlocksProcessedEvent.getMetadata().getBlockTime(),
                0, ZoneOffset.ofHours(0)));
    }
}
