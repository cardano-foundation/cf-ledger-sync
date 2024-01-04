package org.cardanofoundation.ledgersync.scheduler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoolOfflineDataProperties {
    private long fixedDelay;
    private long initialDelay;
}
