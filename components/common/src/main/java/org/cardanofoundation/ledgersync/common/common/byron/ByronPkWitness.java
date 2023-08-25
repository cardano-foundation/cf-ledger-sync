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
public class ByronPkWitness implements ByronTxWitnesses {

  public static final String TYPE = "ByronPkWitness";

  private String publicKey;
  private String signature;

  @Override
  public String getType() {
    return TYPE;
  }
}
