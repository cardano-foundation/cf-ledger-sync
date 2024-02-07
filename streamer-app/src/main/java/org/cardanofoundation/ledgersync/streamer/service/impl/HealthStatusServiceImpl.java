package org.cardanofoundation.ledgersync.streamer.service.impl;

import com.bloxbean.cardano.yaci.store.common.config.StoreProperties;
import com.bloxbean.cardano.yaci.store.core.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.streamer.healthcheck.HealthStatus;
import org.cardanofoundation.ledgersync.streamer.service.HealthCheckCachingService;
import org.cardanofoundation.ledgersync.streamer.service.HealthStatusService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.cardanofoundation.ledgersync.streamer.constant.StreamerConstant.*;

@Service
@RequiredArgsConstructor
public class HealthStatusServiceImpl implements HealthStatusService {
    private final HealthCheckCachingService healthCheckCachingService;
    private final StoreProperties storeProperties;
    private final HealthService healthService;

    @Value("${streamer.healthcheck.event-time-threshold}")
    private long publishedTimeThreshold;

    @Value("${streamer.healthcheck.keepalive-time-threshold}")
    private Long keepAliveResponseTimeThresholdInSecond;

    @Override
    public HealthStatus getHealthStatus() {
        final LocalDateTime latestEventTime = healthCheckCachingService.getLatestEventTime();
        final Long latestSlotNo = healthCheckCachingService.getLatestSlotNo();
        boolean hasStopSlot = storeProperties.getSyncStopSlot() != 0;
        boolean isHealthy = true;
        String message = DATA_IS_CRAWLING;

        if (isOutOfThreshold(publishedTimeThreshold, latestEventTime)) {
            isHealthy = false;
            if (isConnectionToNodeHealthy()) {
                message = CONNECTION_HEALTHY_BUT_DATA_CRAWLING_NOT_HEALTHY;
            } else {
                message = DATA_IS_NOT_CRAWLING;
            }
        }

        if (isHealthy && latestSlotNo != null && hasStopSlot && latestSlotNo >= storeProperties.getSyncStopSlot()){
            message = STOP_SLOT_HAS_REACHED;
        }

        return HealthStatus.builder()
                .isHealthy(isHealthy)
                .hasStopSlot(hasStopSlot)
                .message(message)
                .build();
    }

    private boolean isOutOfThreshold(Long threshold, LocalDateTime time) {
        long value = ChronoUnit.SECONDS.between(time, LocalDateTime.now(ZoneOffset.UTC));
        return threshold <= value;
    }

    private LocalDateTime getLastKeepAliveResponseTime() {
        return Instant.ofEpochMilli(healthService.getHealthStatus().getLastKeepAliveResponseTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    private boolean isConnectionToNodeHealthy() {
        return healthService.getHealthStatus().isConnectionAlive() &&
                !isOutOfThreshold(keepAliveResponseTimeThresholdInSecond, getLastKeepAliveResponseTime());
    }
}
