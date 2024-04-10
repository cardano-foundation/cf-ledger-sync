package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "tx_bootstrap_witnesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TxBootstrapWitnesses extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tx_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Tx tx;

  @Column(name = "public_key", nullable = false)
  private String publicKey;
  @Column(name = "signature", nullable = false)
  private String signature;
  @Column(name = "chain_code", nullable = false)
  private String chainCode;
  @Column(name = "attributes", nullable = false)
  private String attributes;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    TxBootstrapWitnesses txOut = (TxBootstrapWitnesses) o;
    return id != null && Objects.equals(id, txOut.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
