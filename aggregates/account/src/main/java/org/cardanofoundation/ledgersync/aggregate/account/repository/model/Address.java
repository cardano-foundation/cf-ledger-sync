package org.cardanofoundation.ledgersync.aggregate.account.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Address extends BaseEntity {

    @Column(name = "address", nullable = false, length = 65535)
    private String address;

    @Column(name = "tx_count")
    private Long txCount;

    @Column(name = "balance", nullable = false, precision = 39)
//    @Word128Type
    @Digits(integer = 39, fraction = 0)
    private BigInteger balance;

    @Column(name = "address_has_script", nullable = false)
    private Boolean addressHasScript;

    @Column(name = "stake_address", updatable = false)
    private String stakeAddress;

    @Column(name = "verified_contract")
    private Boolean verifiedContract;

    @Column(name = "payment_cred", length = 56)
//    @Hash28Type
    private String paymentCred;

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
        Address address = (Address) o;
        return id != null && Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
