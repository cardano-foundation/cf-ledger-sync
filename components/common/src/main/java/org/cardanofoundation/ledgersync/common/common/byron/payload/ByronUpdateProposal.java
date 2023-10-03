package org.cardanofoundation.ledgersync.common.common.byron.payload;

import org.cardanofoundation.ledgersync.common.common.SoftwareVersion;
import org.cardanofoundation.ledgersync.common.common.byron.ByronBlockVersion;
import org.cardanofoundation.ledgersync.common.common.byron.ByronBlockVersionMod;
import org.cardanofoundation.ledgersync.common.common.byron.ByronUpdateData;
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
public class ByronUpdateProposal {
  private ByronBlockVersion blockVersion;
  private ByronBlockVersionMod blockVersionMod;
  private SoftwareVersion softwareVersion;
  private Map<String, ByronUpdateData> data;
  private String attributes;
  private String from;
  private String signature;
}
