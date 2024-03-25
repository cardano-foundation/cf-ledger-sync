package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.converter.ByteConverter;
import org.cardanofoundation.ledgersync.consumercommon.validation.Asset32Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash28Type;
import org.hibernate.Hibernate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "multi_asset", uniqueConstraints = {
    @UniqueConstraint(name = "unique_multi_asset",
        columnNames = {"policy", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MultiAsset extends BaseEntity {

  @Column(name = "policy", nullable = false, length = 56)
  @Hash28Type
  private String policy;

  @Column(name = "name", nullable = false, length = 64)
  @Asset32Type
  @Convert(converter = ByteConverter.class)
  private String name;

  @Column(name = "name_view", length = 64)
  private String nameView;

  @Column(name = "fingerprint", nullable = false)
  private String fingerprint;

  @Column(name = "supply", precision = 23)
  @Digits(integer = 23, fraction = 0)
  private BigInteger supply;

  @Column(name = "time")
  private Timestamp time;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    MultiAsset that = (MultiAsset) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
