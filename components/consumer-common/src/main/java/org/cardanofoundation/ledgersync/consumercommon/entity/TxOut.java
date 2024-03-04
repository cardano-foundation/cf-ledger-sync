package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.enumeration.TokenType;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash28Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Hash32Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Lovelace;
import org.cardanofoundation.ledgersync.consumercommon.validation.TxIndex;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "tx_out", uniqueConstraints = {
    @UniqueConstraint(name = "unique_txout",
        columnNames = {"tx_id", "index"}
    )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TxOut extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tx_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Tx tx;

  @Column(name = "tx_id", updatable = false, insertable = false)
  private Long txId;

  @Column(name = "index", nullable = false)
  @TxIndex
  private Short index;

  @Column(name = "address", nullable = false, length = 65535)
  private String address;

  @Column(name = "address_raw", nullable = false)
  private byte[] addressRaw;

  @Column(name = "address_has_script", nullable = false)
  private Boolean addressHasScript;

  @Column(name = "payment_cred", length = 56)
  @Hash28Type
  private String paymentCred;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stake_address_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private StakeAddress stakeAddress;

  @Column(name = "value", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger value;

  @Column(name = "token_type", nullable = false)
  private TokenType tokenType;

  @Column(name = "data_hash", length = 64)
  @Hash32Type
  private String dataHash;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "inline_datum_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Datum inlineDatum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reference_script_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Script referenceScript;

  @OneToMany(mappedBy = "txOut")
  private List<MaTxOut> maTxOuts;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    TxOut txOut = (TxOut) o;
    return id != null && Objects.equals(id, txOut.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
