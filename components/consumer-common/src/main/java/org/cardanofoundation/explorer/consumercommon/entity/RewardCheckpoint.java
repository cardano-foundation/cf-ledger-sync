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
@Table(name = "reward_checkpoint", uniqueConstraints = {
    @UniqueConstraint(name = "unique_stake_address_checkpoint",
        columnNames = {"view"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RewardCheckpoint extends BaseEntity {

  @Column(name = "view", nullable = false)
  private String stakeAddress;

  @Column(name = "epoch_checkpoint", nullable = false)
  private Integer epochCheckpoint;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    RewardCheckpoint rewardCheckpoint = (RewardCheckpoint) o;
    return id != null && Objects.equals(id, rewardCheckpoint.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
