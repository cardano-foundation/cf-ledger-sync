package org.cardanofoundation.ledgersync.common.common.byron;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronTxProof {

  private long txpNumber;
  private String txpRoot;
  private String txpWitnessesHash;
}
