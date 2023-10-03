package org.cardanofoundation.ledgersync.common.common.byron.payload;

import org.cardanofoundation.ledgersync.common.common.byron.ByronUpdateVote;
import java.util.List;
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
public class ByronUpdatePayload {
  private ByronUpdateProposal proposal;
  private List<ByronUpdateVote> votes;
}
