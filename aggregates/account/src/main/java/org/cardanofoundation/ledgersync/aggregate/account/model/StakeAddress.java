package org.cardanofoundation.ledgersync.aggregate.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "stake_address", uniqueConstraints = {
        @UniqueConstraint(name = "unique_stake_address",
                columnNames = {"hash_raw"}
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class StakeAddress extends BaseEntity {

    @Column(name = "hash_raw", nullable = false)
//    @Addr29Type
    private String hashRaw;

    @Column(name = "view", nullable = false, length = 65535)
    private String view;

    @Column(name = "script_hash", length = 56)
//    @Hash28Type
    private String scriptHash;

    @Column(name = "balance", nullable = false, precision = 39)
//    @Word128Type
    @Digits(integer = 39, fraction = 0)
    private BigInteger balance;

    @Column(name = "available_reward", nullable = false, precision = 39)
//    @Word128Type
    @Digits(integer = 39, fraction = 0)
    private BigInteger availableReward;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StakeAddress that = (StakeAddress) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
