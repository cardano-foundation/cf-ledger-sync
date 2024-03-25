package org.cardanofoundation.ledgersync.healthcheck.service.impl;

import org.cardanofoundation.ledgersync.healthcheck.service.HealthCheckCachingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class HealthCheckCachingServiceImpl implements HealthCheckCachingService {
    private LocalDateTime latestBlockTime;
    private LocalDateTime latestEventTime = LocalDateTime.now(ZoneOffset.UTC);
    private final AtomicLong latestSlot = new AtomicLong();

    @Override
    public void saveLatestSlotNo(Long slotNo) {
        latestSlot.set(slotNo);
    }

    @Override
    public Long getLatestSlotNo() {
        return latestSlot.get();
    }

    @Override
    public void saveLatestEventTime(LocalDateTime eventTime) {
        latestEventTime = eventTime;
    }

    @Override
    public LocalDateTime getLatestEventTime() {
        return latestEventTime;
    }

    @Override
    public void saveLatestBlockTime(LocalDateTime blockTime) {
        latestBlockTime = blockTime;
    }

    @Override
    public LocalDateTime getLatestBlockTime() {
        return latestBlockTime;
    }
}
