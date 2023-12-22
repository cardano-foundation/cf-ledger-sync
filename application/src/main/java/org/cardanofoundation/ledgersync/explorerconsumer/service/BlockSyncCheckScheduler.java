package org.cardanofoundation.ledgersync.explorerconsumer.service;

import com.bloxbean.cardano.yaci.core.protocol.chainsync.messages.Point;
import com.bloxbean.cardano.yaci.store.core.StoreProperties;
import com.bloxbean.cardano.yaci.store.core.domain.Cursor;
import com.bloxbean.cardano.yaci.store.core.service.BlockFetchService;
import com.bloxbean.cardano.yaci.store.core.service.CursorService;
import com.bloxbean.cardano.yaci.store.core.service.TipFinderService;
import com.bloxbean.cardano.yaci.store.events.RollbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "blocks.jobs.check-sync.enabled",
        matchIfMissing = true,
        havingValue = "true")
@Slf4j
public class BlockSyncCheckScheduler {
    private final BlockFetchService blockFetchService;
    private final CursorService cursorService;
    private final TipFinderService tipFinderService;
    private final StoreProperties storeProperties;
    private final BlockSyncingManager blockSyncingManager;
    private final ApplicationEventPublisher publisher;

    @Value("${blocks.resyncThreshold.notInSyncMode}")
    private Long resyncThresholdNotInSyncMode;
    @Value("${blocks.resyncThreshold.inSyncMode}")
    private Long resyncThresholdInSyncMode;

    /**
     * Scheduled method to check and perform a manual resynchronization if needed.
     * The scheduler checks the synchronization status and initiates a manual
     * resynchronization if the conditions for resync are met. This includes closing
     * the current connection to the node and establishing a new connection
     */
    @Scheduled(fixedDelayString = "${blocks.jobs.check-sync.fixed-delay}",
            initialDelayString = "${blocks.jobs.check-sync.init-delay}")
    @SneakyThrows
    public void checkAndPerformResyncIfNeeded() {
        log.debug("Check sync status");
        long lastProcessedTimeElapsed = System.currentTimeMillis() - blockSyncingManager.getLastEventProcessedTime();
        long reconnectThreshold = blockSyncingManager.getIsSyncMode() ? resyncThresholdInSyncMode : resyncThresholdNotInSyncMode;

        if (lastProcessedTimeElapsed < reconnectThreshold || blockSyncingManager.getIsEventBeingProcessed()) {
            return;
        }

        Cursor cursor = cursorService.getCursor().orElse(null);
        if (Objects.isNull(cursor)) {
            return;
        }

        log.info("Shutdown and reconnect manually");

        if (blockSyncingManager.getIsSyncMode()) {
            blockFetchService.shutdownSync();
        } else {
            blockFetchService.shutdown();
        }

        Thread.sleep(4000);
        // Double-checking to ensure that no event is being processed before initiating resynchronization
        while (true) {
            if (!blockSyncingManager.getIsEventBeingProcessed()) {
                break;
            }
        }

        Cursor newestCursor = cursorService.getCursor().orElse(null);
        Point from = new Point(newestCursor.getSlot(), newestCursor.getBlockHash());

        Point to = (storeProperties.getSyncStopSlot() != 0L) ?
                new Point(storeProperties.getSyncStopSlot(), storeProperties.getSyncStopBlockhash()) :
                tipFinderService.getTip().block().getPoint();

        log.info("From >> " + from);
        log.info("TO >> " + to);

        //Send a rollback event to rollback data at and after this slot.
        //This is because, during start up the from block will be processed again (For BlockFetch).
        if (from.getSlot() > 0) {
            RollbackEvent rollbackEvent = RollbackEvent.builder()
                    .rollbackTo(new Point(from.getSlot(), from.getHash()))
                    .build();
            log.info("Publishing rollback event to clean data after restart >>> " + rollbackEvent);
            publisher.publishEvent(rollbackEvent);
        }

        if (blockSyncingManager.getIsSyncMode()) {
            blockFetchService.startSync(from);
        } else {
            blockFetchService.startFetch(from, to);
        }
    }
}
