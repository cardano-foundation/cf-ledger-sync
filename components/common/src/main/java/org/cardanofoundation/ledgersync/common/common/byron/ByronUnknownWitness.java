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
public class ByronUnknownWitness implements ByronTxWitnesses {

  public static final String TYPE = "ByronUnknownWitness";

  private String data;

  @Override
  public String getType() {
    return TYPE;
  }
}
