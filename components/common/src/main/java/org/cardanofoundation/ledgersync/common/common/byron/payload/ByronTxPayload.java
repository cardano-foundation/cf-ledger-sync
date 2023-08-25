package org.cardanofoundation.ledgersync.common.common.byron.payload;

import org.cardanofoundation.ledgersync.common.common.byron.ByronTx;
import org.cardanofoundation.ledgersync.common.common.byron.ByronTxWitnesses;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronTxPayload {

  private ByronTx transaction;
  private List<ByronTxWitnesses> witnesses;
}
