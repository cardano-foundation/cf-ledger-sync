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
public class OffChainGovActionId implements Serializable {

    @Column(
        name = "gov_action_tx_hash",
        insertable = false,
        updatable = false
    )
    private String govActionTxHash;

    @Column(
        name = "gov_action_idx",
        insertable = false,
        updatable = false
    )
    private Long govActionIdx;
}
