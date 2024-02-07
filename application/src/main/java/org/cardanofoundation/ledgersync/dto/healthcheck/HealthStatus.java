package org.cardanofoundation.ledgersync.dto.healthcheck;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthStatus {
    Boolean isHealthy;
    String message;
    LocalDateTime latestBlockInsertTime;
    Boolean hasStopSlot;
}
