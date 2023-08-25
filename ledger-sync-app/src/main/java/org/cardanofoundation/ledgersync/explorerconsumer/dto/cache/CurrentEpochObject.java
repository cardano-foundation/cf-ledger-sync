package org.cardanofoundation.ledgersync.explorerconsumer.dto.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrentEpochObject {

    Integer epochNo;
    Integer maxEpochSlot;
    String startTime;
    String endTime;
}
