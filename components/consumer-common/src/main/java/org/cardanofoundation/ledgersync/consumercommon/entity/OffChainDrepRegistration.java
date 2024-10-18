package org.cardanofoundation.ledgersync.consumercommon.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash32Type;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "off_chain_drep_registration", uniqueConstraints = {
    @UniqueConstraint(name = "unique_off_chain_drep_regis",
        columnNames = {"drep_reg_tx_hash", "drep_reg_cert_index"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainDrepRegistration extends BaseEntity {

  @Column(name = "drep_reg_tx_hash", nullable = false)
  @Hash32Type
  private String drepRegTxHash;

  @Column(name = "drep_reg_cert_index", nullable = false)
  private Long drepRegCertIndex;

  @Type(JsonType.class)
  @Column(name = "content")
  private String content;

  @Enumerated(EnumType.STRING)
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
    OffChainDrepRegistration that = (OffChainDrepRegistration) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
