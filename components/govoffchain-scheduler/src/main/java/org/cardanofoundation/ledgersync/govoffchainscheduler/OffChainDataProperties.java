package org.cardanofoundation.ledgersync.govoffchainscheduler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OffChainDataProperties {
    private long fixedDelay;
    private long initialDelay;
    private long initialDelayFetchError;
    private long fixedDelayFetchError;
    private int retryCount;
}
