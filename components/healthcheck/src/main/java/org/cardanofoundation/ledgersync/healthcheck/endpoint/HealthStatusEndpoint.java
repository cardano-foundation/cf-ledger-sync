package org.cardanofoundation.ledgersync.healthcheck.endpoint;

import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.healthcheck.model.HealthStatus;
import org.cardanofoundation.ledgersync.healthcheck.service.HealthStatusService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Endpoint(id = "health-status")
@RequiredArgsConstructor
@ConditionalOnExpression("${ledgersync.healthcheck.enabled:true}")
@Component
public class HealthStatusEndpoint {
    private final HealthStatusService healthStatusService;

    @ReadOperation
    @Bean
    public ResponseEntity<HealthStatus> checkHealthStatus() {
        var healthStatus = healthStatusService.getHealthStatus();

        if (Boolean.TRUE.equals(healthStatus.getIsHealthy())) {
            return ResponseEntity.ok().body(healthStatus);
        }

        return ResponseEntity.internalServerError().body(healthStatus);
    }
}
