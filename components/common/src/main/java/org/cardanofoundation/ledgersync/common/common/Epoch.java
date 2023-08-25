package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Epoch {
    private long value;
    private long slotOfEpoch;

    private long slotId;

    public Epoch(long value, long slotOfEpoch) {
        this.value = value;
        this.slotOfEpoch = slotOfEpoch;
        this.slotId = value * 21600 + slotOfEpoch;
    }
}
