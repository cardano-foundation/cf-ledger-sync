package org.cardanofoundation.ledgersync.service.impl;

import com.bloxbean.cardano.yaci.store.common.config.StoreProperties;
import com.bloxbean.cardano.yaci.store.core.service.CursorService;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.dto.healthcheck.HealthStatus;
import org.cardanofoundation.ledgersync.service.HealthCheckCachingService;
import org.cardanofoundation.ledgersync.service.HealthStatusService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.cardanofoundation.ledgersync.constant.ConsumerConstant.*;

@Service
@RequiredArgsConstructor
public class HealthStatusServiceImpl implements HealthStatusService {

    private final HealthCheckCachingService healthCheckCachingService;
    private final CursorService cursorService;
    private final StoreProperties storeProperties;

    @Value("${ledger-sync.healthcheck.block-time-threshold}")
    private Long blockTimeThresholdInSecond;

    @Value("${ledger-sync.healthcheck.inserted-time-threshold}")
    private Long insertedTimeThresholdInSecond;

    @Override
    public HealthStatus getHealthStatus() {
        final LocalDateTime latestBlockInsertTime = healthCheckCachingService.getLatestBlockInsertTime();
        final Long latestSlotNo = healthCheckCachingService.getLatestBlockSlot();
        final LocalDateTime latestBlockTime = healthCheckCachingService.getLatestBlockTime();

        boolean isSyncing;
        String message;
        boolean hasReachedToStopSlot = false;

        if (Objects.isNull(latestBlockTime)) {
            if (storeProperties.getSyncStopSlot() != 0) {
                var recentCursor = cursorService.getCursor();
                if (recentCursor.isPresent() && recentCursor.get().getSlot() >= latestSlotNo) {
                    isSyncing = false;
                    hasReachedToStopSlot = true;
                    message = SYNCING_HAS_FINISHED;
                } else if (isOutOfThreshold(insertedTimeThresholdInSecond, latestBlockInsertTime)) {
                    isSyncing = false;
                    message = DATA_IS_NOT_SYNCING;
                } else {
                    isSyncing = true;
                    message = SYNCING_BUT_NOT_READY;
                }
            } else if (isOutOfThreshold(insertedTimeThresholdInSecond, latestBlockInsertTime)) {
                isSyncing = false;
                message = DATA_IS_NOT_SYNCING;
            } else {
                isSyncing = true;
                message = SYNCING_BUT_NOT_READY;
            }
        } else {
            if (isOutOfThreshold(insertedTimeThresholdInSecond, latestBlockInsertTime)) {
                isSyncing = false;
                message = DATA_IS_NOT_SYNCING;
            } else {
                isSyncing = true;
                message = isOutOfThreshold(blockTimeThresholdInSecond, latestBlockTime) ? SYNCING_BUT_NOT_READY : READY_TO_SERVE;
            }

            if (storeProperties.getSyncStopSlot() != 0 && latestSlotNo >= storeProperties.getSyncStopSlot()) {
                isSyncing = false;
                message = SYNCING_HAS_FINISHED;
                hasReachedToStopSlot = true;
            }
        }

        return HealthStatus.builder()
                .isSyncing(isSyncing)
                .message(message)
                .latestBlockInsertTime(latestBlockInsertTime)
                .hasReachedToStopSlot(hasReachedToStopSlot)
                .build();
    }

    private boolean isOutOfThreshold(Long threshold, LocalDateTime time) {
        long value = ChronoUnit.SECONDS.between(time, LocalDateTime.now(ZoneOffset.UTC));
        return threshold <= value;
    }
}
