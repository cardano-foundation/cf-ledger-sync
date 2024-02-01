package org.cardanofoundation.ledgersync.streamer.service.impl;

import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.streamer.service.HealthCheckCachingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HealthCheckCachingServiceImpl implements HealthCheckCachingService {
    private Long latestSlotNo;
    private LocalDateTime latestPublishTime = LocalDateTime.now();

    @Override
    public void saveLatestSlotNo(Long slotNo) {
        latestSlotNo = slotNo;
    }

    @Override
    public Long getLatestSlotNo() {
        return latestSlotNo;
    }

    @Override
    public void saveLatestPublishTime(LocalDateTime publishTime) {
        latestPublishTime = publishTime;
    }

    @Override
    public LocalDateTime getLatestPublishTime() {
        return latestPublishTime;
    }
}
