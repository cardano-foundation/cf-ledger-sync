package org.cardanofoundation.ledgersync.consumercommon.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.cardanofoundation.ledgersync.consumercommon.validation.Hash32Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Lovelace;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word31Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word64Type;
import org.hibernate.Hibernate;

@Entity
@Table(name = "tx", uniqueConstraints = {
    @UniqueConstraint(name = "unique_tx",
        columnNames = {"hash"}
    )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder(toBuilder = true)
public class Tx extends BaseEntity {

  @Column(name = "hash", nullable = false, length = 64)
  @Hash32Type
  private String hash;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false,
      foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  @EqualsAndHashCode.Exclude
  private Block block;

  @Column(name = "block_id", updatable = false, insertable = false)
  private Long blockId;

  @Column(name = "block_index")
  @Word31Type
  private Long blockIndex;

  @Column(name = "out_sum", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger outSum;

  @Column(name = "fee", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger fee;

  @Column(name = "deposit")
  private Long deposit;

  @Column(name = "size")
  @Word31Type
  private Integer size;

  @Column(name = "invalid_before", precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger invalidBefore;

  @Column(name = "invalid_hereafter", precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger invalidHereafter;

  @Column(name = "valid_contract")
  private Boolean validContract;

  @Column(name = "script_size")
  @Word31Type
  private Integer scriptSize;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tx_metadata_hash_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private TxMetadataHash txMetadataHash;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Tx tx = (Tx) o;
    return id != null && Objects.equals(id, tx.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public void addScriptSize(int size) {
    if (this.size == null) {
      this.size = 0;
    }
    this.size += size;
  }

}
