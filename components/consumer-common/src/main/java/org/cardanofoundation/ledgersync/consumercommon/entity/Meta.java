package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.sql.Timestamp;
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
@Table(name = "meta", uniqueConstraints = {
    @UniqueConstraint(name = "unique_meta",
        columnNames = {"start_time"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Meta extends BaseEntity {

  @Column(name = "start_time", nullable = false)
  private Timestamp startTime;

  @Column(name = "network_name", nullable = false)
  private String networkName;

  @Column(name = "version", nullable = false)
  private String version;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Meta meta = (Meta) o;
    return id != null && Objects.equals(id, meta.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
