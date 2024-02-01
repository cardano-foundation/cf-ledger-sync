package org.cardanofoundation.ledgersync.streamer.healthcheck;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HealthStatus {
    Boolean isCrawling;
    Boolean hasReachedToStopSlot;
    String message;
}
