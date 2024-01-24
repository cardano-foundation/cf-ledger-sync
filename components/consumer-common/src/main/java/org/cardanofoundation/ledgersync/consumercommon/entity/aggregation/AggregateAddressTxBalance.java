package org.cardanofoundation.ledgersync.consumercommon.entity.aggregation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import org.cardanofoundation.ledgersync.consumercommon.entity.BaseEntity;
import org.cardanofoundation.ledgersync.consumercommon.validation.Word128Type;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "agg_address_tx_balance")
public class AggregateAddressTxBalance extends BaseEntity {

  @Column(name = "stake_address_id")
  protected Long stakeAddressId;

  @Column(name = "address_id")
  protected Long addressId;

  @Column(name = "balance", nullable = false, precision = 39)
  @Word128Type
  private @Digits(integer = 39, fraction = 0) BigInteger balance;

  @Column(name = "day")
  private LocalDate day;

}