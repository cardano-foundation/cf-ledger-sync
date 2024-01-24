package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.enumeration.ScriptPurposeType;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash28Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Lovelace;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word31Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word63Type;
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
@Table(name = "redeemer", uniqueConstraints = {
    @UniqueConstraint(name = "unique_redeemer",
        columnNames = {"tx_id", "purpose", "index"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Redeemer extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tx_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Tx tx;

  @Column(name = "unit_mem", nullable = false)
  @Word63Type
  private Long unitMem;

  @Column(name = "unit_steps", nullable = false)
  @Word63Type
  private Long unitSteps;

  @Column(name = "fee", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger fee;

  @Column(name = "purpose", nullable = false)
  private ScriptPurposeType purpose;

  @Column(name = "index", nullable = false)
  @Word31Type
  private Integer index;

  @Column(name = "script_hash", length = 56)
  @Hash28Type
  private String scriptHash;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "redeemer_data_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private RedeemerData redeemerData;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Redeemer redeemer = (Redeemer) o;
    return id != null && Objects.equals(id, redeemer.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
