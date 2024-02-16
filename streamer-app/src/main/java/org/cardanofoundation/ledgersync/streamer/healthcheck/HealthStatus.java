package org.cardanofoundation.ledgersync.streamer.healthcheck;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HealthStatus {
    Boolean isHealthy;
    Boolean hasStopSlot;
    String messageCode;
    String messageDesc;
}
