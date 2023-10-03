package org.cardanofoundation.ledgersync.common.common.byron.signature;

import java.math.BigInteger;
import lombok.Builder;

@Builder
public class EpochRange {
  private BigInteger start;
  private BigInteger end;
}
