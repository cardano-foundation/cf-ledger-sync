package org.cardanofoundation.ledgersync.account.storage.impl.model;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class AddressTxAmountId implements Serializable {
    @Column(name = "address")
    private String address;
    @Column(name = "unit")
    private String unit;
    @Column(name = "tx_hash")
    private String txHash;
}
