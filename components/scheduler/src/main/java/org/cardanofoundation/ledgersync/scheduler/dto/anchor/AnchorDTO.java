package org.cardanofoundation.ledgersync.scheduler.dto.anchor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnchorDTO {
    String anchorUrl;
    String anchorHash;
    Long slot;
    Integer retryCount;
}
