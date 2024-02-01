package org.cardanofoundation.ledgersync.streamer.service.impl;

import com.bloxbean.cardano.yaci.store.common.config.StoreProperties;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.streamer.healthcheck.HealthStatus;
import org.cardanofoundation.ledgersync.streamer.service.HealthStatusService;
import org.cardanofoundation.ledgersync.streamer.service.HealthCheckCachingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.cardanofoundation.ledgersync.streamer.constant.StreamerConstant.*;

@Service
@RequiredArgsConstructor
public class HealthStatusServiceImpl implements HealthStatusService {
    private final HealthCheckCachingService healthCheckCachingService;
    private final StoreProperties storeProperties;

    @Value("${streamer.healthcheck.published-time-threshold}")
    private long publishedTimeThreshold;

    @Override
    public HealthStatus getHealthStatus() {
        final LocalDateTime latestPublishTime = healthCheckCachingService.getLatestPublishTime();
        final Long latestSlotNo = healthCheckCachingService.getLatestSlotNo();

        boolean isCrawling = true;
        boolean hasReachedToStopSlot = false;
        String message = DATA_IS_CRAWLING;

        if (isOutOfThreshold(publishedTimeThreshold, latestPublishTime)) {
            isCrawling = false;
            message = DATA_IS_NOT_CRAWLING;
        }

        if (latestSlotNo != null && (storeProperties.getSyncStopSlot() != 0 && latestSlotNo >= storeProperties.getSyncStopSlot())) {
            isCrawling = false;
            hasReachedToStopSlot = true;
            message = STOP_SLOT_HAS_REACHED;
        }

        return HealthStatus.builder()
                .isCrawling(isCrawling)
                .hasReachedToStopSlot(hasReachedToStopSlot)
                .message(message)
                .build();
    }

    private boolean isOutOfThreshold(Long threshold, LocalDateTime time) {
        long value = ChronoUnit.SECONDS.between(time, LocalDateTime.now(ZoneOffset.UTC));
        return threshold <= value;
    }
}
