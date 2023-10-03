package org.cardanofoundation.explorer.consumercommon.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.Hibernate;

@Entity
@Table(name = "pool_history_checkpoint", uniqueConstraints = {
    @UniqueConstraint(name = "unique_pool_history_checkpoint",
        columnNames = {"view"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolHistoryCheckpoint extends BaseEntity {
  @Column(name = "view", nullable = false)
  private String view;

  @Column(name = "epoch_checkpoint", nullable = false)
  private Integer epochCheckpoint;

  @Column(name = "is_spendable_reward")
  private Boolean isSpendableReward;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolHistoryCheckpoint poolHistoryCheckpoint = (PoolHistoryCheckpoint) o;
    return id != null && Objects.equals(id, poolHistoryCheckpoint.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
