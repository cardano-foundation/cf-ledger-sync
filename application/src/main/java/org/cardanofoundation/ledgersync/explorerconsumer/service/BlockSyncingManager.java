package org.cardanofoundation.ledgersync.explorerconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages synchronization state and timing information for block events or rollback events.
 * This class is responsible for keeping track of various synchronization-related
 * details, such as the last event received time, the last event processed time,
 * synchronization mode status, and whether an event is currently being processed.
 */
@Component
@Slf4j
public class BlockSyncingManager {

    /** The timestamp of the last received block event. */
    private final AtomicLong lastEventReceivedTime = new AtomicLong(System.currentTimeMillis());

    /**
     * The timestamp of the last processed block event.
     * Represents the moment when the processing of a block event is completed.
     * It is updated each time a block event is successfully processed by the system.
     */
    private final AtomicLong lastEventProcessedTime = new AtomicLong(System.currentTimeMillis());

    /** Indicates whether the yaci is crawling with sync mode (use Chain-Sync protocol)*/
    private final AtomicBoolean isSyncMode = new AtomicBoolean(false);

    /** Indicates whether a block event is currently being processed. */
    private final AtomicBoolean isEventBeingProcessed = new AtomicBoolean(false);

    public void setIsEventBeingProcessed(boolean isProcessing) {
        isEventBeingProcessed.set(isProcessing);
    }

    public void setIsSyncMode(boolean value) {
        isSyncMode.set(value);
    }

    public void setLastEventReceivedTime(long time) {
        lastEventReceivedTime.set(time);
    }

    public void setLastEventProcessedTime(long time) {
        lastEventProcessedTime.set(time);
    }

    public boolean getIsSyncMode() {
        return isSyncMode.get();
    }

    public boolean getIsEventBeingProcessed() {
        return isEventBeingProcessed.get();
    }

    public long getLastEventReceivedTime() {
        return lastEventReceivedTime.get();
    }

    public long getLastEventProcessedTime() {
        return lastEventProcessedTime.get();
    }

}
