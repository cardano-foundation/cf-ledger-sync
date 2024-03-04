package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.enumeration.converter.ByteConverter;
import org.cardanofoundation.ledgersync.consumercommon.validation.Asset32Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash28Type;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

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

  @Column(name = "tx_count")
  private Long txCount;

  @Column(name = "supply", precision = 23)
  @Digits(integer = 23, fraction = 0)
  private BigInteger supply;

  @Column(name = "total_volume", precision = 40)
  @Digits(integer = 40, fraction = 0)
  private BigInteger totalVolume;

  @Column(name = "time")
  private Timestamp time;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "multiAsset")
  private List<AddressToken> addressToken;


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
