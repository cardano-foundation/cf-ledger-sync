package org.cardanofoundation.ledgersync.verifier.data.app.mapper;

import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Interface for mapping address transaction amount comparisons.
 *
 * @param <T> The type of object to map from.
 */
@Component
public interface AddressTxAmountComparisonMapper<T> {

    /**
     * Builds a map of address transaction amount comparisons.
     *
     * @param from The list of objects to map from.
     * @return A map of address transaction amount comparisons.
     */
    Map<AddressTxAmountKey, AddressTxAmountComparison> buildMap(List<T> from);
}
