package org.cardanofoundation.ledgersync.common.common;

import lombok.*;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)

public class Amount {

  private String unit;
  private String policyId;
  private byte[] assetName;
  private BigInteger quantity;
}
