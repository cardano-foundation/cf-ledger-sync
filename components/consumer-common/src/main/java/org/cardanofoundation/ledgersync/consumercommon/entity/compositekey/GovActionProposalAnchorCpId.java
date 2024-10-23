package org.cardanofoundation.ledgersync.consumercommon.entity.compositekey;

import jakarta.persistence.Column;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GovActionProposalAnchorCpId implements Serializable {

    @Column(
        name = "anchor_url",
        insertable = false,
        updatable = false
    )
    private String anchorUrl;

    @Column(
        name = "anchor_hash",
        insertable = false,
        updatable = false
    )
    private String anchor_hash;
}
