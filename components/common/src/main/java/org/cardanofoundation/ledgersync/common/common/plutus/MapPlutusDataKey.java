package org.cardanofoundation.ledgersync.common.common.plutus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.cardanofoundation.ledgersync.common.util.JsonUtil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapPlutusDataKey{

  private PlutusData key;

  @Override
  public String toString() {
    return JsonUtil.getPrettyJson(this.key);
  }
}
