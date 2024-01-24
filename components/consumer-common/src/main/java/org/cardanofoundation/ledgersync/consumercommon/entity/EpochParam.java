package org.cardanofoundation.ledgersync.consumercommon.entity;

import org.cardanofoundation.ledgersync.consumercommon.validation.Hash32Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Lovelace;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word31Type;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word64Type;

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
@Table(name = "epoch_param", uniqueConstraints = {
    @UniqueConstraint(name = "unique_epoch_param",
        columnNames = {"epoch_no", "block_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class EpochParam extends BaseEntity {

  @Column(name = "epoch_no", nullable = false)
  @Word31Type
  private Integer epochNo;

  @Column(name = "min_fee_a", nullable = false)
  @Word31Type
  private Integer minFeeA;

  @Column(name = "min_fee_b", nullable = false)
  @Word31Type
  private Integer minFeeB;

  @Column(name = "max_block_size", nullable = false)
  @Word31Type
  private Integer maxBlockSize;

  @Column(name = "max_tx_size", nullable = false)
  @Word31Type
  private Integer maxTxSize;

  @Column(name = "max_bh_size", nullable = false)
  @Word31Type
  private Integer maxBhSize;

  @Column(name = "key_deposit", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger keyDeposit;

  @Column(name = "pool_deposit", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger poolDeposit;

  @Column(name = "max_epoch", nullable = false)
  @Word31Type
  private Integer maxEpoch;

  @Column(name = "optimal_pool_count", nullable = false)
  @Word31Type
  private Integer optimalPoolCount;

  @Column(name = "influence", nullable = false)
  private Double influence;

  @Column(name = "monetary_expand_rate", nullable = false)
  private Double monetaryExpandRate;

  @Column(name = "treasury_growth_rate", nullable = false)
  private Double treasuryGrowthRate;

  @Column(name = "decentralisation", nullable = false)
  private Double decentralisation;

  @Column(name = "extra_entropy", length = 64)
  @Hash32Type
  private String extraEntropy;

  @Column(name = "protocol_major", nullable = false)
  @Word31Type
  private Integer protocolMajor;

  @Column(name = "protocol_minor", nullable = false)
  @Word31Type
  private Integer protocolMinor;

  @Column(name = "min_utxo_value", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger minUtxoValue;

  @Column(name = "min_pool_cost", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger minPoolCost;

  @Column(name = "nonce", length = 64)
  @Hash32Type
  private String nonce;

  @Column(name = "coins_per_utxo_size", precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger coinsPerUtxoSize;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cost_model_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private CostModel costModel;

  @Column(name = "price_mem")
  private Double priceMem;

  @Column(name = "price_step")
  private Double priceStep;

  @Column(name = "max_tx_ex_mem", precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger maxTxExMem;

  @Column(name = "max_tx_ex_steps", precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger maxTxExSteps;

  @Column(name = "max_block_ex_mem", precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger maxBlockExMem;

  @Column(name = "max_block_ex_steps", precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger maxBlockExSteps;

  @Column(name = "max_val_size", precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger maxValSize;

  @Column(name = "collateral_percent")
  @Word31Type
  private Integer collateralPercent;

  @Column(name = "max_collateral_inputs")
  @Word31Type
  private Integer maxCollateralInputs;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "block_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Block block;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    EpochParam that = (EpochParam) o;

    if (Objects.isNull(id)) {
      return this.hashCode() == that.hashCode();
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(epochNo, minFeeA, minFeeB, maxBlockSize, maxTxSize,
        maxBhSize, keyDeposit, poolDeposit, maxEpoch, optimalPoolCount, influence,
        monetaryExpandRate,
        treasuryGrowthRate, decentralisation, extraEntropy, protocolMajor, protocolMinor,
        minUtxoValue, minPoolCost, nonce, coinsPerUtxoSize, costModel, priceMem, priceStep,
        maxTxExMem, maxTxExSteps, maxBlockExMem, maxBlockExSteps, maxValSize, collateralPercent,
        maxCollateralInputs, block);
  }
}
