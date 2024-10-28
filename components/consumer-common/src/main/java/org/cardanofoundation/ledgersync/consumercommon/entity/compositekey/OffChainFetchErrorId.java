package org.cardanofoundation.ledgersync.consumercommon.entity.compositekey;

import java.io.Serializable;

import jakarta.persistence.Column;
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
public class OffChainFetchErrorId implements Serializable {

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

    @Column(
        name = "type",
        insertable = false,
        updatable = false
    )
    private Integer type;
}
