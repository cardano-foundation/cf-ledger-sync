package org.cardanofoundation.ledgersync.streamer.listener;

import com.bloxbean.cardano.yaci.store.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.streamer.service.HealthCheckCachingService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockEventListener {
    private final HealthCheckCachingService healthCheckCachingService;

    @EventListener
    @Transactional
    public void handleRollback(RollbackEvent rollbackEvent) {
        healthCheckCachingService.saveLatestPublishTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(rollbackEvent.getRollbackTo().getSlot());
    }

    @EventListener
    @Transactional
    public void handleBlockHeader(BlockHeaderEvent blockHeaderEvent) {
        healthCheckCachingService.saveLatestPublishTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(blockHeaderEvent.getBlockHeader().getHeaderBody().getSlot());
    }

    @EventListener
    @Transactional
    public void handleGenesisBlock(GenesisBlockEvent genesisBlockEvent) {
        healthCheckCachingService.saveLatestPublishTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(genesisBlockEvent.getSlot());
    }

    @EventListener
    @Transactional
    public void handleByronBlockEvent(ByronMainBlockEvent byronMainBlockEvent) {
        healthCheckCachingService.saveLatestPublishTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(byronMainBlockEvent.getMetadata().getSlot());
    }

    @EventListener
    @Transactional
    public void handleByronEbBlock(ByronEbBlockEvent byronEbBlockEvent) {
        healthCheckCachingService.saveLatestPublishTime(LocalDateTime.now(ZoneOffset.UTC));
        healthCheckCachingService.saveLatestSlotNo(byronEbBlockEvent.getMetadata().getSlot());
    }
}
