package org.cardanofoundation.ledgersync.common.common.plutus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cardanofoundation.ledgersync.common.util.JsonUtil;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapPlutusElement {

  PlutusData k;

  PlutusData v;

  @Override
  public String toString() {
    return JsonUtil.getPrettyJson(this);
  }
}
