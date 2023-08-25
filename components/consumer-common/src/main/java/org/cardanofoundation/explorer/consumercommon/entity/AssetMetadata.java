package org.cardanofoundation.explorer.consumercommon.entity;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

@Entity
@Table(name = "asset_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AssetMetadata extends BaseEntity {

  @Column(name = "subject", nullable = false)
  private String subject;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "policy", nullable = false)
  private String policy;

  @Column(name = "ticker", nullable = false)
  private String ticker;

  @Column(name = "url", nullable = false)
  private String url;

  @Column(name = "logo", nullable = false, length = 100000)
  private String logo;

  @Column(name = "decimals", nullable = false)
  private Integer decimals;

  @Column(name = "logo_hash", nullable = false)
  private String logoHash;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    AssetMetadata that = (AssetMetadata) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
