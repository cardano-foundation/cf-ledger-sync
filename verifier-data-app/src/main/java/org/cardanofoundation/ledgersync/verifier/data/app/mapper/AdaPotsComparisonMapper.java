package org.cardanofoundation.ledgersync.verifier.data.app.mapper;

import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AdaPotsComparisonKey;

import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public interface AdaPotsComparisonMapper<T> {
  /**
   * Builds a map of Ada pots comparisons.
   *
   * @param source The source data list.
   * @return A map of Ada pots comparisons.
   */
  Map<AdaPotsComparisonKey, AdaPotsComparison> buildMap(List<T> source) throws SQLException;
}
