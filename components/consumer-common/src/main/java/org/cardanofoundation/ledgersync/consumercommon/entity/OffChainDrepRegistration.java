package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.util.Objects;

import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainDRepRegistrationId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash32Type;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "off_chain_drep_registration", uniqueConstraints = {
    @UniqueConstraint(name = "unique_off_chain_drep_registration",
        columnNames = {"drep_reg_tx_hash", "drep_reg_cert_index"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainDRepRegistration extends BaseEntity {

  @Column(name = "drep_reg_tx_hash", nullable = false)
  @Hash32Type
  private String drepRegTxHash;

  @Column(name = "drep_reg_cert_index", nullable = false)
  private Long drepRegCertIndex;

  @Embedded
  @AttributeOverrides({@AttributeOverride(
      name = "drep_reg_tx_hash",
      column = @Column(
          name = "drep_reg_tx_hash",
          insertable = false,
          updatable = false
      )
  ), @AttributeOverride(
      name = "drep_reg_cert_index",
      column = @Column(
          name = "drep_reg_cert_index",
          insertable = false,
          updatable = false
      )
  )})
  private OffChainDRepRegistrationId drepRegistrationId;

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
    OffChainDRepRegistration that = (OffChainDRepRegistration) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
