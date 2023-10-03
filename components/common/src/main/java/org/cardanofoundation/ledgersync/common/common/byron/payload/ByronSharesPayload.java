package org.cardanofoundation.ledgersync.common.common.byron.payload;

import org.cardanofoundation.ledgersync.common.common.byron.ByronInnerSharesMap;
import org.cardanofoundation.ledgersync.common.common.byron.ByronSscCert;
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
public class ByronSharesPayload implements ByronSscPayload {

  public static final String TYPE = "ByronSharesPayload";

  private Map<String, ByronInnerSharesMap> sscShares;
  private List<ByronSscCert> sscCerts;

  @Override
  public String getType() {
    return TYPE;
  }
}
