package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.sql.Timestamp;
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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.cardanofoundation.ledgersync.consumercommon.validation.Word31Type;
import org.hibernate.Hibernate;

@Entity
@Table(name = "pool_offline_fetch_error", uniqueConstraints = {
    @UniqueConstraint(name = "unique_pool_offline_fetch_error",
        columnNames = {"pool_id", "fetch_time", "retry_count"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolOfflineFetchError extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pool_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private PoolHash poolHash;

  @Column(name = "fetch_time", nullable = false)
  private Timestamp fetchTime;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pmr_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private PoolMetadataRef poolMetadataRef;

  @Column(name = "fetch_error", nullable = false, length = 65535)
  private String fetchError;

  @Word31Type
  @Column(name = "retry_count", nullable = false)
  private Integer retryCount;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolOfflineFetchError that = (PoolOfflineFetchError) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
