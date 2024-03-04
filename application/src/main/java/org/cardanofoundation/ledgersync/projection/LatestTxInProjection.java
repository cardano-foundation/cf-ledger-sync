package org.cardanofoundation.ledgersync.projection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LatestTxInProjection {

    Long txId;
    String fromAddress;
}
