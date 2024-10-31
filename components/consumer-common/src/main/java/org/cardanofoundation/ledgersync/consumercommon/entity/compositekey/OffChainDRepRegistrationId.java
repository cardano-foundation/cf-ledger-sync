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
public class OffChainDRepRegistrationId implements Serializable {

    @Column(
        name = "drep_reg_tx_hash",
        insertable = false,
        updatable = false
    )
    private String drepRegTxHash;

    @Column(
        name = "drep_reg_cert_index",
        insertable = false,
        updatable = false
    )
    private Long drepRegCertIndex;
}
