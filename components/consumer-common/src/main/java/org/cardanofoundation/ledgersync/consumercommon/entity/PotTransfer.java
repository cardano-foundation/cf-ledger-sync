package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.validation.Int65Type;
import java.math.BigInteger;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

@Entity
@Table(name = "pot_transfer", uniqueConstraints = {
    @UniqueConstraint(name = "unique_pot_transfer",
        columnNames = {"tx_id", "cert_index"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PotTransfer extends BaseEntity {

  @Column(name = "cert_index", nullable = false)
  private Integer certIndex;

  @Column(name = "treasury", nullable = false, precision = 20)
  @Int65Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger treasury;

  @Column(name = "reserves", nullable = false, precision = 20)
  @Int65Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger reserves;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tx_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Tx tx;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PotTransfer that = (PotTransfer) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
