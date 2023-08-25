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
@Table(name = "pool_history", uniqueConstraints = {
    @UniqueConstraint(name = "unique_pool_history",
        columnNames = {"pool_id", "epoch_no"}
    )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolHistory extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pool_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private PoolHash pool;

  @Column(name = "pool_id", updatable = false, insertable = false)
  private Long poolId;

  @Column(name = "epoch_no")
  private Integer epochNo;

  @Column(name = "active_stake", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger activeStake;

  @Column(name = "active_stake_pct")
  private Double activeStakePct;

  @Column(name = "saturation_pct")
  private Double saturationPct;

  @Column(name = "block_cnt")
  private Integer blockCnt;

  @Column(name = "delegator_cnt")
  private Integer delegatorCnt;

  @Column(name = "margin")
  private Double margin;

  @Column(name = "fixed_cost", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger fixedCost;

  @Column(name = "pool_fees", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger poolFees;

  @Column(name = "deleg_rewards", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger delegatorRewards;

  @Column(name = "epoch_ros")
  private Double epochRos;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolHistory poolHistory = (PoolHistory) o;
    return id != null && Objects.equals(id, poolHistory.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
