package org.cardanofoundation.ledgersync.dto.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LatestTxObject {

    String txHash;
    Long blockNo;
    Integer epochNo;
    Long slotNo;
    Set<String> fromAddresses;
    Set<String> toAddresses;
    String timestamp;
}
