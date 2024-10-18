package org.cardanofoundation.ledgersync.consumercommon.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.CheckValid;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "off_chain_voting_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OffChainVotingData extends BaseEntity {

  @Column(name = "voting_procedure_id", nullable = false)
  private UUID votingProcedureId;

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
    OffChainVotingData that = (OffChainVotingData) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
