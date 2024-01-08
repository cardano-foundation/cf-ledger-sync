package org.cardanofoundation.ledgersync.aggregate.account.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockInfo {
    long slot;
    long blockNumber;
    long blockTime;
}
