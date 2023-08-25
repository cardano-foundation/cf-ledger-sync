package org.cardanofoundation.ledgersync.common.common.plutus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.cardanofoundation.ledgersync.common.util.JsonUtil;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapPlutusDataValue {
  private PlutusData value;

  @Override
  public String toString() {
    return JsonUtil.getPrettyJson(this.value);
  }
}
