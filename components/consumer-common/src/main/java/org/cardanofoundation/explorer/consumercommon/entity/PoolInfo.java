package org.cardanofoundation.explorer.consumercommon.entity;

import java.math.BigInteger;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.cardanofoundation.explorer.consumercommon.validation.Lovelace;
import org.hibernate.Hibernate;


@Entity
@Table(name = "pool_info", uniqueConstraints = {
    @UniqueConstraint(name = "unique_pool_info",
        columnNames = {"pool_id", "fetched_at_epoch"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolInfo extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pool_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private PoolHash pool;

  @Column(name = "pool_id", updatable = false, insertable = false)
  private Long poolId;

  @Column(name = "fetched_at_epoch", nullable = false)
  private Integer fetchedAtEpoch;

  @Column(name = "active_stake", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger activeStake;

  @Column(name = "live_stake", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger liveStake;

  @Column(name = "live_saturation", nullable = false)
  private Double liveSaturation;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolInfo poolInfo = (PoolInfo) o;
    return id != null && Objects.equals(id, poolInfo.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
