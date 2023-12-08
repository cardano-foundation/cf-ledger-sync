package org.cardanofoundation.ledgersync.aggregate.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.Hibernate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "address_token_balance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AddressTokenBalance extends BaseEntity {

    @Column(name = "slot")
    private Long slot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    @EqualsAndHashCode.Exclude
    private Address address;

    @Column(name = "address_id", updatable = false, insertable = false)
    private Long addressId;

    @Column(name = "policy", updatable = false)
    private String policy;

    @Column(name = "asset_name", updatable = false)
    private String assetName;

    @Column(name = "fingerprint", updatable = false)
    private String fingerprint;

    @Column(name = "stake_address", updatable = false)
    private String stakeAddress;

    @Column(name = "balance", nullable = false, precision = 39)
    @Digits(integer = 39, fraction = 0)
    private BigInteger balance;

    @Column(name = "block")
    private Long blockNumber;

    @Column(name = "block_time")
    private Long blockTime;

    @UpdateTimestamp
    @Column(name = "update_datetime")
    private LocalDateTime updateDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AddressTokenBalance that = (AddressTokenBalance) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
