package org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnchorDTO {
    private String anchorUrl;
    private String anchorHash;
    private Long slot;
    private Integer retryCount;
}
