package org.cardanofoundation.ledgersync.healthcheck.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HealthStatus {
    Boolean isHealthy;
    String messageCode;
    String messageDesc;

    public HealthStatus(Boolean isHealthy, Message message) {
        this.isHealthy = isHealthy;
        messageCode = message.getCode();
        messageDesc = message.getDesc();
    }
}
