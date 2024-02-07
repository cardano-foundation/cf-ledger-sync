package org.cardanofoundation.ledgersync.healthcheck;

import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.dto.healthcheck.HealthStatus;
import org.cardanofoundation.ledgersync.service.HealthStatusService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Endpoint(id = "health-status")
@RequiredArgsConstructor
@Component
public class LedgerSyncHealthEndpoint {

    private final HealthStatusService healthStatusService;

    @ReadOperation
    @Bean
    public ResponseEntity<HealthStatus> checkHealthStatus() {
        var healthStatus = healthStatusService.getHealthStatus();

        if (Boolean.FALSE.equals(healthStatus.getIsHealthy())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(healthStatus);
        }

        return ResponseEntity.ok().body(healthStatus);
    }
}
