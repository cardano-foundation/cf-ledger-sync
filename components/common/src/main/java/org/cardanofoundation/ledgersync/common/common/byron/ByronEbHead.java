package org.cardanofoundation.ledgersync.common.common.byron;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronEbHead implements ByronHead<String, ByronEbBlockCons, String> {

  private long protocolMagic;
  private String blockHash;
  private String prevBlock;
  private String bodyProof;
  private ByronEbBlockCons consensusData;
  private String extraData;
}
