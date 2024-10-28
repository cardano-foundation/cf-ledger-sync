package org.cardanofoundation.ledgersync.consumercommon.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "off_chain_constitution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainConstitution extends BaseEntity {

  @Column(name = "constitution_active_epoch", nullable = false)
  private Integer constitutionActiveEpoch;

  @Type(JsonType.class)
  @Column(name = "content")
  private String content;

  @Column(name = "check_valid", nullable = false, length = 64)
  private CheckValid checkValid;

  @Column(name = "valid_at_slot")
  private Long validAtSlot;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    OffChainConstitution that = (OffChainConstitution) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
