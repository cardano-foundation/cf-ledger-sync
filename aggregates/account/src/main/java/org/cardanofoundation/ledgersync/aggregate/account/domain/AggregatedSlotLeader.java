package org.cardanofoundation.ledgersync.aggregate.account.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AggregatedSlotLeader {

    String hashRaw;
    String prefix;
}
