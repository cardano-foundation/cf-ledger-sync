package org.cardanofoundation.ledgersync.common.common.address;

public enum ShelleyAddressType {
  PAYMENT_AND_STAKE,
  SCRIPT_AND_STAKE,
  PAYMENT_AND_SCRIPT,
  SCRIPT_AND_SCRIPT,

  PAYMENT_AND_POINTER,
  SCRIPT_AND_POINTER,
  PAYMENT,

  SCRIPT,

  STAKE_REWARD,
  SCRIPT_REWARD
}
