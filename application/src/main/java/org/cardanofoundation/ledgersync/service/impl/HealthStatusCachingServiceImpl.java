package org.cardanofoundation.ledgersync.service.impl;

import org.cardanofoundation.ledgersync.service.HealthCheckCachingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class HealthStatusCachingServiceImpl implements HealthCheckCachingService {
    private LocalDateTime latestBlockTime;
    private LocalDateTime latestBlockInsertTime = LocalDateTime.now(ZoneOffset.UTC);
    private Long latestBlockSlot;

    @Override
    public void saveLatestBlockTime(LocalDateTime blockTime) {
        latestBlockTime = blockTime;
    }

    @Override
    public LocalDateTime getLatestBlockTime() {
        return latestBlockTime;
    }

    @Override
    public void saveLatestBlockInsertTime(LocalDateTime insertTime) {
        latestBlockInsertTime = insertTime;
    }

    @Override
    public LocalDateTime getLatestBlockInsertTime() {
        return latestBlockInsertTime;
    }

    @Override
    public void saveLatestBlockSlot(Long slot) {
        latestBlockSlot = slot;
    }

    @Override
    public Long getLatestBlockSlot() {
        return latestBlockSlot;
    }
}
