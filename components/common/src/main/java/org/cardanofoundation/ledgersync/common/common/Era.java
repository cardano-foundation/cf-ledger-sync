package org.cardanofoundation.ledgersync.common.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum Era {
  ROLLBACK(-1),
  BYRON(1),
  SHELLEY(2),
  ALLEGRA(3),
  MARY(4),
  ALONZO(5),
  BABBAGE(6);

  int value;
}
