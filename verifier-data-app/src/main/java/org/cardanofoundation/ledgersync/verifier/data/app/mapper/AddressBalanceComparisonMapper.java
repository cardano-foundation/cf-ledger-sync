package org.cardanofoundation.ledgersync.verifier.data.app.mapper;

import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Interface for mapping address balance comparisons.
 * @param <T> The type of source data.
 */
@Component
public interface AddressBalanceComparisonMapper<T> {
    /**
     * Builds a map of address balance comparisons.
     * @param source The source data list.
     * @return A map of address balance comparisons.
     */
    Map<AddressBalanceComparisonKey, AddressBalanceComparison> buildMap(List<T> source);
}
