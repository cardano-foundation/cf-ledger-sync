package org.cardanofoundation.ledgersync.common.common.certs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@RequiredArgsConstructor
public enum CertType {
  GENESIS_KEY_DELEGATION("GENESIS_KEY_DELEGATION"),
  MOVE_INSTATANEOUS("MOVE_INSTATANEOUS"),
  POOL_REGISTRATION("POOL_REGISTRATION"),
  POOL_RETIREMENT("POOL_RETIREMENT"),
  STAKE_DELEGATION("STAKE_DELEGATION"),
  STAKE_DEREGISTRATION("STAKE_DEREGISTRATION"),
  STAKE_REGISTRATION("STAKE_REGISTRATION");
  String value;
}
