package org.cardanofoundation.explorer.consumercommon.entity;

import java.math.BigInteger;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.Hibernate;

@Entity
@Table(name = "tx_chart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TxChart extends BaseEntity {

  @Column(name = "minute", nullable = false, precision = 13)
  @Digits(integer = 13, fraction = 0)
  private BigInteger minute;

  @Column(name = "hour", nullable = false, precision = 13)
  @Digits(integer = 13, fraction = 0)
  private BigInteger hour;

  @Column(name = "day", nullable = false, precision = 13)
  @Digits(integer = 13, fraction = 0)
  private BigInteger day;

  @Column(name = "month", nullable = false, precision = 13)
  @Digits(integer = 13, fraction = 0)
  private BigInteger month;

  @Column(name = "year", nullable = false, precision = 13)
  @Digits(integer = 13, fraction = 0)
  private BigInteger year;

  @Column(name = "tx_count", nullable = false)
  private Long txCount;

  @Column(name = "tx_simple", nullable = false)
  private Long txSimple;

  @Column(name = "tx_with_metadata_without_sc", nullable = false)
  private Long txWithMetadataWithoutSc;

  @Column(name = "tx_with_sc", nullable = false)
  private Long txWithSc;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    TxChart txChart = (TxChart) o;
    return id != null && Objects.equals(id, txChart.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
