package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.store.common.config.StoreProperties;
import com.bloxbean.cardano.yaci.store.core.service.CursorService;
import com.bloxbean.cardano.yaci.store.core.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.dto.healthcheck.HealthStatus;
import org.cardanofoundation.ledgersync.service.HealthCheckCachingService;
import org.cardanofoundation.ledgersync.service.HealthStatusService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.cardanofoundation.ledgersync.constant.ConsumerConstant.*;

@Service
@RequiredArgsConstructor
public class HealthStatusServiceImpl implements HealthStatusService {

    private final HealthCheckCachingService healthCheckCachingService;
    private final HealthService healthService;
    private final CursorService cursorService;
    private final StoreProperties storeProperties;

    @Value("${ledger-sync.healthcheck.block-time-threshold}")
    private Long blockTimeThresholdInSecond;

    @Value("${ledger-sync.healthcheck.inserted-time-threshold}")
    private Long insertedTimeThresholdInSecond;

    @Value("${ledger-sync.healthcheck.keepalive-time-threshold}")
    private Long keepAliveResponseTimeThresholdInSecond;

    @Override
    public HealthStatus getHealthStatus() {
        final LocalDateTime latestBlockInsertTime = healthCheckCachingService.getLatestBlockInsertTime();
        final LocalDateTime latestBlockTime = healthCheckCachingService.getLatestBlockTime();
        final long stopSlot = storeProperties.getSyncStopSlot();

        boolean isHealthy = true;
        String message = SYNCING_BUT_NOT_READY;

        if (stopSlot > 0) {
            return getHealthStatusWhenStopSlotIsSet(stopSlot);
        }

        if (Objects.isNull(latestBlockTime)) { // this latestBlockTime is only != null after a block is successfully inserted into the database
            if (isOutOfThreshold(insertedTimeThresholdInSecond, latestBlockInsertTime)) {
                if (isConnectionToNodeHealthy()) {
                    message = CONNECTION_HEALTHY_BUT_BLOCK_CONSUMING_NOT_HEALTHY;
                } else {
                    message = DATA_IS_NOT_SYNCING;
                }
                return HealthStatus.builder()
                        .isHealthy(false)
                        .message(message)
                        .latestBlockInsertTime(latestBlockInsertTime)
                        .hasStopSlot(true)
                        .build();
            }
        } else {
            if (!isConnectionToNodeHealthy()) {
                isHealthy = false;
                message = DATA_IS_NOT_SYNCING;
            } else if (isOutOfThreshold(insertedTimeThresholdInSecond, latestBlockInsertTime)) {
                isHealthy = false;
                message = CONNECTION_HEALTHY_BUT_BLOCK_CONSUMING_NOT_HEALTHY;
            }
        }

        if (isHealthy && latestBlockTime != null && !isOutOfThreshold(blockTimeThresholdInSecond, latestBlockTime)) {
            message = READY_TO_SERVE;
        }

        return HealthStatus.builder()
                .isHealthy(isHealthy)
                .message(message)
                .latestBlockInsertTime(latestBlockInsertTime)
                .hasStopSlot(false)
                .build();
    }

    private HealthStatus getHealthStatusWhenStopSlotIsSet(Long stopSlot) {
        final LocalDateTime latestBlockInsertTime = healthCheckCachingService.getLatestBlockInsertTime();
        final Long latestSlotNo = healthCheckCachingService.getLatestBlockSlot();
        final LocalDateTime latestBlockTime = healthCheckCachingService.getLatestBlockTime();

        boolean isHealthy = true;
        String message = SYNCING_BUT_NOT_READY;

        if (Objects.isNull(latestBlockTime)) {
            var recentCursor = cursorService.getCursor();
            if (recentCursor.isPresent() && recentCursor.get().getSlot() >= stopSlot) {
                message = SYNCING_HAS_FINISHED;
            }

            if (isOutOfThreshold(insertedTimeThresholdInSecond, latestBlockInsertTime)) {
                isHealthy = false;
                if (!isConnectionToNodeHealthy()) {
                    message = DATA_IS_NOT_SYNCING;
                } else {
                    message = CONNECTION_HEALTHY_BUT_BLOCK_CONSUMING_NOT_HEALTHY;
                }
            }
        } else {
            if (!isConnectionToNodeHealthy()) {
                isHealthy = false;
                message = DATA_IS_NOT_SYNCING;
            } else if (isOutOfThreshold(insertedTimeThresholdInSecond, latestBlockInsertTime)) {
                isHealthy = false;
                message = CONNECTION_HEALTHY_BUT_BLOCK_CONSUMING_NOT_HEALTHY;
            }
        }

        if (isHealthy && latestSlotNo >= stopSlot) {
            message = SYNCING_HAS_FINISHED;
        }

        return HealthStatus.builder()
                .isHealthy(isHealthy)
                .message(message)
                .latestBlockInsertTime(latestBlockInsertTime)
                .hasStopSlot(true)
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
        return healthService.getHealthStatus().isConnectionAlive() && !isOutOfThreshold(keepAliveResponseTimeThresholdInSecond, getLastKeepAliveResponseTime());
    }
}
