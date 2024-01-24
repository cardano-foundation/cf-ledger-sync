package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.math.BigInteger;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

@Entity
@Table(name = "pool_stake", uniqueConstraints = {
    @UniqueConstraint(name = "uni_pool_id",
        columnNames = {"pool_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolStake extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "pool_id")
  private PoolHash pool;

  @Column(name = "amount")
  private BigInteger amount;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolStake poolStake = (PoolStake) o;
    return id != null && Objects.equals(id, poolStake.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
