package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProtocolVersion {
    private long protoMajor;
    private long protoMinor;
}
