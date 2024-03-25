package org.cardanofoundation.ledgersync.healthcheck.service;


import org.cardanofoundation.ledgersync.healthcheck.model.HealthStatus;

public interface HealthStatusService {
    HealthStatus getHealthStatus();
}
