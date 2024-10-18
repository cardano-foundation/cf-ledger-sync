package org.cardanofoundation.ledgersync.consumercommon.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import org.cardanofoundation.ledgersync.consumercommon.entity.compositekey.OffChainGovActionCpId;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash32Type;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "off_chain_gov_action", uniqueConstraints = {
    @UniqueConstraint(name = "unique_off_chain_gov_action",
        columnNames = {"gov_action_tx_hash", "gov_action_idx"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainGovAction extends BaseEntity {

  @Column(name = "gov_action_tx_hash", nullable = false)
  @Hash32Type
  private String govActionTxHash;

  @Column(name = "gov_action_idx", nullable = false)
  private Long govActionIdx;

  @Embedded
  @AttributeOverrides({@AttributeOverride(
      name = "gov_action_tx_hash",
      column = @Column(
          name = "gov_action_tx_hash",
          insertable = false,
          updatable = false
      )
  ), @AttributeOverride(
      name = "gov_action_idx",
      column = @Column(
          name = "gov_action_idx",
          insertable = false,
          updatable = false
      )
  )})
  private OffChainGovActionCpId cpId;

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
    OffChainGovAction that = (OffChainGovAction) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
