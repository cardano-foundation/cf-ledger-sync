package org.cardanofoundation.ledgersync.streamer.service;

import org.cardanofoundation.ledgersync.streamer.healthcheck.HealthStatus;

public interface HealthStatusService {
    HealthStatus getHealthStatus();
}
