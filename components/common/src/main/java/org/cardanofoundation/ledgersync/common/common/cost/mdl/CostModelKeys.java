package org.cardanofoundation.ledgersync.common.common.cost.mdl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://plutus.readthedocs.io/en/latest/reference/cardano/language-changes.html
 */
public interface CostModelKeys {
  List<String> getKeys();

  default Map<String, BigInteger> getCostModelMap(List<BigInteger> values) {
    return IntStream.range(0, values.size())
        .boxed()
        .collect(Collectors.toMap(idx -> getKeys().get(idx), values::get));
  }
}
