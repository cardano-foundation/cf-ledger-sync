package org.cardanofoundation.ledgersync.common.common.byron;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
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
public class ByronBlockVersionMod {
  private Long scriptVersion;
  private BigInteger slotDuration;
  private BigInteger maxBlockSize;
  private BigInteger maxHeaderSize;
  private BigInteger maxTxSize;
  private BigInteger maxProposalSize;
  private BigDecimal mpcThd;
  private BigDecimal heavyDelThd;
  private BigDecimal updateVoteThd;
  private BigDecimal updateProposalThd;
  private BigInteger updateImplicit;
  private List<BigInteger> softForkRule;
  private Map<BigInteger, Object> txFeePolicy;
  private Long unlockStakeEpoch;
}
