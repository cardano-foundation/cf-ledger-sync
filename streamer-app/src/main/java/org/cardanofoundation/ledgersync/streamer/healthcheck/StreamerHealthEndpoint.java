package org.cardanofoundation.ledgersync.streamer.healthcheck;

import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.streamer.service.HealthStatusService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Endpoint(id = "health-status")
@RequiredArgsConstructor
@Component
public class StreamerHealthEndpoint {
    private final HealthStatusService healthStatusService;

    @ReadOperation
    @Bean
    public ResponseEntity<HealthStatus> checkHealthStatus() {
        var healthStatus = healthStatusService.getHealthStatus();

        if (Boolean.TRUE.equals(healthStatus.isHealthy)) {
            return ResponseEntity.ok().body(healthStatus);
        }

        return ResponseEntity.internalServerError().body(healthStatus);
    }
}
