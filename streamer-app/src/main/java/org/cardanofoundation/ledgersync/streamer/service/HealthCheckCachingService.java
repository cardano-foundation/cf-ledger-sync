package org.cardanofoundation.ledgersync.streamer.service;

import java.time.LocalDateTime;

public interface HealthCheckCachingService {

    /**
     * Cache most recent slot no
     */
    void saveLatestSlotNo(Long slotNo);

    Long getLatestSlotNo();

    /**
     * Cache the time when the most recent data was published
     */
    void saveLatestEventTime(LocalDateTime insertTime);

    LocalDateTime getLatestEventTime();
}
