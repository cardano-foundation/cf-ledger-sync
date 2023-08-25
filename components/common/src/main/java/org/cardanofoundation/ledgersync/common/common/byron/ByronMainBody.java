package org.cardanofoundation.ledgersync.common.common.byron;

import org.cardanofoundation.ledgersync.common.common.byron.payload.ByronDlgPayload;
import org.cardanofoundation.ledgersync.common.common.byron.payload.ByronSscPayload;
import org.cardanofoundation.ledgersync.common.common.byron.payload.ByronTxPayload;
import org.cardanofoundation.ledgersync.common.common.byron.payload.ByronUpdatePayload;
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
public class ByronMainBody {
  private List<ByronTxPayload> txPayload;
  private ByronSscPayload sscPayload;
  private List<ByronDlgPayload> dlgPayload;
  private ByronUpdatePayload updPayload;
}
