package org.cardanofoundation.ledgersync.scheduler.dto.anchor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConstitutionAnchorDTO extends AnchorDTO {

    private Integer constitutionActiveEpoch;

    public ConstitutionAnchorDTO(String anchorUrl, String anchorHash, Long slot, Integer constitutionActiveEpoch,
            Integer retryCount) {

        super(anchorUrl, anchorHash, slot, retryCount);
        this.constitutionActiveEpoch = constitutionActiveEpoch;
    }
}
