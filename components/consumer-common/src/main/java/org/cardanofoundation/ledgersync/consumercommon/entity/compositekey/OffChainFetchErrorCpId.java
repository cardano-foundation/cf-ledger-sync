package org.cardanofoundation.ledgersync.consumercommon.entity.compositekey;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OffChainFetchErrorCpId implements Serializable {

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
    private String anchorHash;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "type_vote",
        insertable = false,
        updatable = false
    )
    private TypeVote typeVote;
}
