package org.cardanofoundation.ledgersync.verifier.data.app.model;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdaPotsComparison {
  AdaPotsComparisonKey adaPotsComparisonKey;
  BigInteger treasury;
  BigInteger reserves;
  BigInteger rewards;
}
