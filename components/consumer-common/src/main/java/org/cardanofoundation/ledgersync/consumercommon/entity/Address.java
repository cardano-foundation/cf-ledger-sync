package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.validation.Hash28Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word128Type;
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
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Address extends BaseEntity {

  @Column(name = "address", nullable = false, length = 65535)
  private String address;

  @Column(name = "tx_count")
  private Long txCount;

  @Column(name = "balance", nullable = false, precision = 39)
  @Word128Type
  @Digits(integer = 39, fraction = 0)
  private BigInteger balance;

  @Column(name = "address_has_script", nullable = false)
  private Boolean addressHasScript;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stake_address_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  private StakeAddress stakeAddress;

  @Column(name = "stake_address_id", updatable = false, insertable = false)
  private Long stakeAddressId;

  @Column(name = "verified_contract")
  private Boolean verifiedContract;

  @Column(name = "payment_cred", length = 56)
  @Hash28Type
  private String paymentCred;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Address address = (Address) o;
    return id != null && Objects.equals(id, address.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
