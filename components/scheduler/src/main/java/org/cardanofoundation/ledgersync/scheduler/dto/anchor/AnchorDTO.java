package org.cardanofoundation.ledgersync.scheduler.dto.anchor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
}
