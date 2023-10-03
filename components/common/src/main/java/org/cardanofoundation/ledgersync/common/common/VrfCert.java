package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class VrfCert {
    private String v1;
    private String v2;
}
