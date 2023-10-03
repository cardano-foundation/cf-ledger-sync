package org.cardanofoundation.ledgersync.common.common.byron;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronRedeemWitness implements ByronTxWitnesses {

  public static final String TYPE = "ByronRedeemWitness";

  private String redeemPublicKey;
  private String redeemSignature;

  @Override
  public String getType() {
    return TYPE;
  }
}
