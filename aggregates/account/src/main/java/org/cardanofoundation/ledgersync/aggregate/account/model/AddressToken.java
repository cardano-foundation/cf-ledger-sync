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
@Table(name = "address_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AddressToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false,
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @EqualsAndHashCode.Exclude
    private Address address;

    @Column(name = "policy")
    private String policy;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "fingerprint")
    private String fingerprint;

    @Column(name = "tx_hash")
    private String txHash;

    @Column(name = "address_id", updatable = false, insertable = false)
    private Long addressId;

    @Column(name = "balance", nullable = false, precision = 39)
//    @Word128Type
    @Digits(integer = 39, fraction = 0)
    private BigInteger balance;

    @Column(name = "block")
    private Long blockNumber;

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
        AddressToken that = (AddressToken) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
