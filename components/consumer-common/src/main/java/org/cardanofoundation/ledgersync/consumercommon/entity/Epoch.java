package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.enumeration.EraType;
import org.cardanofoundation.ledgersync.consumercommon.validation.Lovelace;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word128Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word31Type;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;
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
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "epoch", uniqueConstraints = {
    @UniqueConstraint(name = "unique_epoch",
        columnNames = {"no"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@DynamicUpdate
public class Epoch extends BaseEntity {

  @Column(name = "out_sum", nullable = false, precision = 39)
  @Word128Type
  @Digits(integer = 39, fraction = 0)
  private BigInteger outSum;

  @Column(name = "fees", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger fees;

  @Column(name = "tx_count", nullable = false)
  @Word31Type
  private Integer txCount;

  @Column(name = "blk_count", nullable = false)
  @Word31Type
  private Integer blkCount;

  @Column(name = "no", nullable = false)
  @Word31Type
  private Integer no;

  @Column(name = "start_time")
  private Timestamp startTime;

  @Column(name = "end_time")
  private Timestamp endTime;

  @Column(name = "max_slot", nullable = false)
  private Integer maxSlot;

  @Column(name = "era", nullable = false)
  private EraType era;

  @Column(name = "rewards_distributed")
  @Digits(integer = 20, fraction = 0)
  @Lovelace
  private BigInteger rewardsDistributed;

/*  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "epoch_no" ,insertable =false, updatable = false)
  private Set<Block> blocks;*/

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }

    Epoch epoch = (Epoch) o;

    if (Objects.nonNull(id)) {
      if (Objects.equals(id, epoch.id)) {
        return Objects.equals(outSum, epoch.getOutSum()) &&
            Objects.equals(fees, epoch.getFees()) &&
            Objects.equals(txCount, epoch.getTxCount()) &&
            Objects.equals(blkCount, epoch.getBlkCount()) &&
            Objects.equals(no, epoch.getNo()) &&
            Objects.equals(startTime, epoch.getStartTime()) &&
            Objects.equals(endTime, epoch.getEndTime()) &&
            Objects.equals(maxSlot, epoch.getMaxSlot()) &&
            Objects.equals(era, epoch.getEra()) &&
            Objects.equals(rewardsDistributed, epoch.getRewardsDistributed());
      }
      return Boolean.FALSE;
    }

    return Boolean.FALSE;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
