package org.cardanofoundation.ledgersync.service;

import com.bloxbean.cardano.yaci.core.model.BlockHeader;

import java.time.LocalDateTime;

public interface HealthCheckCachingService {

    /**
     * Cache latest block time
     */
    void saveLatestBlockTime(LocalDateTime blockTime);

    /**
     * Get the latest block
     */
    LocalDateTime getLatestBlockTime();

    /**
     * Cache the time when the most recent block was inserted
     */
    void saveLatestBlockInsertTime(LocalDateTime insertTime);

    LocalDateTime getLatestBlockInsertTime();

    /**
     * Cache latest slot no
     */
    void saveLatestBlockSlot(Long slot);

    /**
     * Get latest slot no
     */
    Long getLatestBlockSlot();
}

