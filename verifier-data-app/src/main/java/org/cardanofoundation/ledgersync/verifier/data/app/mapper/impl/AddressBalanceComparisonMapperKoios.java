package org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl;

import org.cardanofoundation.ledgersync.verifier.data.app.constant.Unit;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.AddressBalanceComparisonMapper;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import rest.koios.client.backend.api.address.model.AddressInfo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link AddressBalanceComparisonMapper} for Koios address information.
 */
public class AddressBalanceComparisonMapperKoios implements AddressBalanceComparisonMapper<AddressInfo> {

    /**
     * Builds a map of address balance comparisons based on Koios address information.
     *
     * @param addressInfoList The list of Koios address information.
     * @return A map containing address balance comparisons.
     */
    @Override
    public Map<AddressBalanceComparisonKey, AddressBalanceComparison> buildMap(List<AddressInfo> addressInfoList) {
        Map<AddressBalanceComparisonKey, AddressBalanceComparison> map = new HashMap<>();

        addressInfoList.forEach(addressInfo -> {
            AddressBalanceComparisonKey addressBalanceComparisonKey = AddressBalanceComparisonKey.builder()
                    .address(addressInfo.getAddress())
                    .unit(Unit.LOVELACE)
                    .build();
            AddressBalanceComparison addressBalanceComparison = AddressBalanceComparison.builder()
                    .addressBalanceComparisonKey(addressBalanceComparisonKey)
                    .balance(new BigInteger(addressInfo.getBalance()))
                    .build();
            map.put(addressBalanceComparisonKey, addressBalanceComparison);
        });
        return map;
    }
}
