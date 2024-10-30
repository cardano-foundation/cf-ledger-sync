package org.cardanofoundation.ledgersync.consumercommon.entity;

import java.util.Objects;

import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainCommitteeDeregistrationId;
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
@Table(name = "off_chain_committee_deregistration", uniqueConstraints = {
    @UniqueConstraint(name = "unique_off_chain_committee_dereg",
        columnNames = {"committee_dereg_tx_hash", "committee_dereg_cert_index"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainCommitteeDeregistration extends BaseEntity {

  @Column(name = "committee_dereg_tx_hash", nullable = false)
  @Hash32Type
  private String committeeDeregTxHash;

  @Column(name = "committee_dereg_cert_index", nullable = false)
  private Long committeeDeregCertIndex;

  @Embedded
  @AttributeOverrides({@AttributeOverride(
      name = "committee_dereg_tx_hash",
      column = @Column(
          name = "committee_dereg_tx_hash",
          insertable = false,
          updatable = false
      )
  ), @AttributeOverride(
      name = "committee_dereg_cert_index",
      column = @Column(
          name = "committee_dereg_cert_index",
          insertable = false,
          updatable = false
      )
  )})
  private OffChainCommitteeDeregistrationId committeeDeregistrationId;

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
    OffChainCommitteeDeregistration that = (OffChainCommitteeDeregistration) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
