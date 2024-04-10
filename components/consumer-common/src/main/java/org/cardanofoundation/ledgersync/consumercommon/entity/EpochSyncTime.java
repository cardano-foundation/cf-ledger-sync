package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.enumeration.SyncStateType;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word63Type;
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
@Table(name = "epoch_sync_time", uniqueConstraints = {
    @UniqueConstraint(name = "unique_epoch_sync_time",
        columnNames = {"no"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class EpochSyncTime extends BaseEntity {

  @Column(name = "no", nullable = false)
  private Long no;

  @Column(name = "seconds", nullable = false)
  @Word63Type(message = "seconds must be word63type")
  private Long seconds;

  @Column(name = "state", nullable = false)
  private SyncStateType state;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    EpochSyncTime that = (EpochSyncTime) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
