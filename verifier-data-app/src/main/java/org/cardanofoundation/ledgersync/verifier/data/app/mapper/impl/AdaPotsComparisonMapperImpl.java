package org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cardanofoundation.ledgersync.verifier.data.app.mapper.AdaPotsComparisonMapper;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.AdaPotsProjection;

public class AdaPotsComparisonMapperImpl implements AdaPotsComparisonMapper<AdaPotsProjection> {
  @Override
  public Map<AdaPotsComparisonKey, AdaPotsComparison> buildMap(List<AdaPotsProjection> source)
      throws SQLException {
    Map<AdaPotsComparisonKey, AdaPotsComparison> map = new HashMap<>();
    for (AdaPotsProjection adaPotsProjection : source) {
      AdaPotsComparisonKey key =
          AdaPotsComparisonKey.builder()
              .slotNo(adaPotsProjection.getSlotNo())
              .epochNo(adaPotsProjection.getEpochNo())
              .build();
      AdaPotsComparison value =
          AdaPotsComparison.builder()
              .adaPotsComparisonKey(key)
              .treasury(adaPotsProjection.getTreasury())
              .reserves(adaPotsProjection.getReserves())
              .rewards(adaPotsProjection.getRewards())
              .build();
      map.put(key, value);
    }
    return map;
  }
}
