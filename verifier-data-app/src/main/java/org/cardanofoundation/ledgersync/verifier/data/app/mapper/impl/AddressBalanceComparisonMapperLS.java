package org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl;

import org.cardanofoundation.ledgersync.verifier.data.app.mapper.AddressBalanceComparisonMapper;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.AddressBalanceProjection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of AddressBalanceComparisonMapper for LedgerSync.
 */
public class AddressBalanceComparisonMapperLS implements AddressBalanceComparisonMapper<AddressBalanceProjection> {

    /**
     * Builds a map of address balance comparisons from the given list of address balance projections.
     *
     * @param addressBalanceProjectionList The list of address balance projections.
     * @return A map of address balance comparisons.
     */
    @Override
    public Map<AddressBalanceComparisonKey, AddressBalanceComparison> buildMap(List<AddressBalanceProjection> addressBalanceProjectionList) {
        Map<AddressBalanceComparisonKey, AddressBalanceComparison> map = new HashMap<>();
        addressBalanceProjectionList.forEach(addressBalanceProjection -> {
            AddressBalanceComparisonKey addressBalanceComparisonKey = AddressBalanceComparisonKey.builder()
                    .address(addressBalanceProjection.getAddress())
                    .unit(addressBalanceProjection.getUnit())
                    .build();

            AddressBalanceComparison addressBalanceComparison = AddressBalanceComparison.builder()
                    .addressBalanceComparisonKey(addressBalanceComparisonKey)
                    .balance(addressBalanceProjection.getBalance())
                    .build();
            map.put(addressBalanceComparisonKey, addressBalanceComparison);
        });
        return map;
    }
}
