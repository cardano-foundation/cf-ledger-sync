package org.cardanofoundation.ledgersync.consumercommon.entity;

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
import org.hibernate.Hibernate;

@Entity
@Table(name = "pool_relay", uniqueConstraints = {
    @UniqueConstraint(name = "unique_pool_relay",
        columnNames = {"update_id", "ipv4", "ipv6", "dns_name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolRelay extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "update_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private PoolUpdate poolUpdate;

  @Column(name = "ipv4")
  private String ipv4;

  @Column(name = "ipv6")
  private String ipv6;

  @Column(name = "dns_name")
  private String dnsName;

  @Column(name = "dns_srv_name")
  private String dnsSrvName;

  @Column(name = "port")
  private Integer port;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolRelay poolRelay = (PoolRelay) o;
    return id != null && Objects.equals(id, poolRelay.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
