package org.cardanofoundation.ledgersync.aggregate.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.Hibernate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "address_tx_balance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AddressTxBalance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    @EqualsAndHashCode.Exclude
    private Address address;

    @Column(name = "address_id", updatable = false, insertable = false)
    private Long addressId;

    @Column(name = "tx_hash")
    private String txHash;

    @Column(name = "stakeAddress")
    private String stakeAddress;

    @Column(name = "balance", nullable = false, precision = 39)
    @Digits(integer = 39, fraction = 0)
    private BigInteger balance;

    @Column(name = "block")
    private Long blockNumber;

    @Column(name = "time")
    private Timestamp time;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AddressTxBalance that = (AddressTxBalance) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
