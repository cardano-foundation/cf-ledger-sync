package org.cardanofoundation.ledgersync.service.impl;

import jakarta.annotation.PostConstruct;
import org.cardanofoundation.ledgersync.service.HealthCheckCachingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class HealthStatusCachingServiceImpl implements HealthCheckCachingService {
    private LocalDateTime latestBlockTime;
    private LocalDateTime latestBlockInsertTime;
    private final AtomicLong latestBlockSlot = new AtomicLong();
    private final AtomicBoolean isSyncMode = new AtomicBoolean();

    @PostConstruct
    void init() {
        latestBlockInsertTime = LocalDateTime.now(ZoneOffset.UTC);
        latestBlockSlot.set(-10L); // dummy value
        isSyncMode.set(Boolean.FALSE);
    }

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
        latestBlockSlot.set(slot);
    }

    @Override
    public Long getLatestBlockSlot() {
        return latestBlockSlot.get();
    }

    @Override
    public void saveIsSyncMode(Boolean value) {
        isSyncMode.set(value);
    }

    @Override
    public Boolean getIsSyncMode() {
        return isSyncMode.get();
    }
}
