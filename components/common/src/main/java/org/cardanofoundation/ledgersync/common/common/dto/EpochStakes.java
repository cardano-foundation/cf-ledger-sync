package org.cardanofoundation.ledgersync.common.common.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EpochStakes {
  private int epoch;
  private Set<Stake> stakes;
}
