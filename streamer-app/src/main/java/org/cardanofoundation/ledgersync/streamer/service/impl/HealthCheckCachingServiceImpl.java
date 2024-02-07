package org.cardanofoundation.ledgersync.streamer.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.streamer.service.HealthCheckCachingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class HealthCheckCachingServiceImpl implements HealthCheckCachingService {
    private Long latestSlotNo;
    private LocalDateTime latestEventTime;

    @PostConstruct
    void init() {
        latestSlotNo = -10L;
        latestEventTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public void saveLatestSlotNo(Long slotNo) {
        latestSlotNo = slotNo;
    }

    @Override
    public Long getLatestSlotNo() {
        return latestSlotNo;
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
