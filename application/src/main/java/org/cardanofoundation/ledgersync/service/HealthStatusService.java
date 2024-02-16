package org.cardanofoundation.ledgersync.service;


import org.cardanofoundation.ledgersync.dto.healthcheck.HealthStatus;

public interface HealthStatusService {

    HealthStatus getHealthStatus();
}
