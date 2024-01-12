package org.cardanofoundation.ledgersync.aggregate.account.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockInfo {
    long slot;
    long blockNumber;
    long blockTime;
}
