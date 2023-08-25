package org.cardanofoundation.ledgersync.common.common.byron;

import lombok.*;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronEbBlockCons {

  private long epochId;
  private BigInteger difficulty;
}
