package org.cardanofoundation.ledgersync.aggregation.app.healthcheck;

import com.bloxbean.cardano.yaci.store.core.service.BlockFetchService;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.healthcheck.HealthCheckProperties;
import org.cardanofoundation.ledgersync.healthcheck.model.HealthStatus;
import org.cardanofoundation.ledgersync.healthcheck.model.Message;
import org.cardanofoundation.ledgersync.healthcheck.service.HealthCheckCachingService;
import org.cardanofoundation.ledgersync.healthcheck.service.HealthStatusService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class CustomHealthStatusServiceImpl implements HealthStatusService {
    private final HealthCheckCachingService healthCheckCachingService;
    private final HealthCheckProperties healthCheckProperties;
    private final BlockFetchService blockFetchService;

    @Override
    public HealthStatus getHealthStatus() {
        final var latestEventTime = healthCheckCachingService.getLatestEventTime();
        final var latestSlotNo = healthCheckCachingService.getLatestSlotNo();
        final var latestBlockTime = healthCheckCachingService.getLatestBlockTime();
        final var stopSlot = healthCheckProperties.getStopSlot();
        if (blockFetchService.isScheduledToStop()) {
            return new HealthStatus(Boolean.TRUE, CustomMessage.IS_SCHEDULED_TO_STOP.getCode(),
                    CustomMessage.IS_SCHEDULED_TO_STOP.getDesc());
        }

        if (!isInThreshold(healthCheckProperties.getEventTimeThreshold(), latestEventTime)) {
            if (stopSlot > 0 && latestSlotNo >= stopSlot) {
                return new HealthStatus(Boolean.TRUE, Message.STOP_SLOT_HAS_REACHED);
            }
            return new HealthStatus(Boolean.FALSE, Message.IS_BAD);
        }

        if (healthCheckProperties.isBlockTimeCheckEnabled() && latestBlockTime != null &&
                isInThreshold(healthCheckProperties.getBlockTimeThreshold(), latestBlockTime)) {
            return new HealthStatus(Boolean.TRUE, Message.BLOCK_HAS_REACHED_TIP);
        }

        return new HealthStatus(Boolean.TRUE, Message.IS_GOOD);
    }

    private boolean isInThreshold(Long threshold, LocalDateTime time) {
        long value = ChronoUnit.SECONDS.between(time, LocalDateTime.now(ZoneOffset.UTC));
        return value <= threshold;
    }
}
