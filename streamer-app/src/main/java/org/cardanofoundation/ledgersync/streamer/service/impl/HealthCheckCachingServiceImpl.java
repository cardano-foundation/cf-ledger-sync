package org.cardanofoundation.ledgersync.streamer.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.streamer.service.HealthCheckCachingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class HealthCheckCachingServiceImpl implements HealthCheckCachingService {
    private final AtomicLong latestSlotNo = new AtomicLong();
    private LocalDateTime latestEventTime;

    @PostConstruct
    void init() {
        latestSlotNo.set(-10); // dummy value
        latestEventTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public void saveLatestSlotNo(Long slotNo) {
        latestSlotNo.set(slotNo);
    }

    @Override
    public Long getLatestSlotNo() {
        return latestSlotNo.get();
    }

    @Override
    public void saveLatestEventTime(LocalDateTime eventTime) {
        latestEventTime = eventTime;
    }

    @Override
    public LocalDateTime getLatestEventTime() {
        return latestEventTime;
    }
}
