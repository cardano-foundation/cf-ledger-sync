package org.cardanofoundation.ledgersync.common.common.byron;

import org.cardanofoundation.ledgersync.common.common.BlockVersion;
import org.cardanofoundation.ledgersync.common.common.SoftwareVersion;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronBlockExtraData<T> {

  private BlockVersion blockVersion;
  private SoftwareVersion softwareVersion;
  private T attributes;
  private String extraProof;
}
