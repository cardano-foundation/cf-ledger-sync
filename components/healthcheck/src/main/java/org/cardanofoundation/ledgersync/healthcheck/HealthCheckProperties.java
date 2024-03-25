package org.cardanofoundation.ledgersync.healthcheck;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ledger-sync.healthcheck", ignoreUnknownFields = true)
public class HealthCheckProperties {
    private boolean enabled = false;
    private long eventTimeThreshold = 240;
    private boolean blockTimeCheckEnabled = false;
    private long blockTimeThreshold = 180;
    private long stopSlot;
}
