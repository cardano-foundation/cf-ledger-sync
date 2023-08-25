package org.cardanofoundation.ledgersync.common.common.byron;

import org.cardanofoundation.ledgersync.common.common.Epoch;
import org.cardanofoundation.ledgersync.common.common.byron.signature.BlockSignature;
import lombok.*;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronBlockCons {

  private Epoch slotId;
  private String pubKey;
  private BigInteger difficulty;
  private BlockSignature blockSig;
}
