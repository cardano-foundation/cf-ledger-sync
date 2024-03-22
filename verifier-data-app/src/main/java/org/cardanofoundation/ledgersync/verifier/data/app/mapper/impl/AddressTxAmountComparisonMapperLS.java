package org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl;

import org.cardanofoundation.ledgersync.verifier.data.app.entity.ledgersync.AddressTxAmount;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.AddressTxAmountComparisonMapper;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the AddressTxAmountComparisonMapper interface for LedgerSync transactions.
 */
public class AddressTxAmountComparisonMapperLS implements AddressTxAmountComparisonMapper<AddressTxAmount> {

    /**
     * Builds a map of address transaction amounts by processing the given list of LedgerSync transactions.
     *
     * @param addressTxAmountList The list of LedgerSync transactions to process.
     * @return A map of address transaction amounts.
     */
    @Override
    public Map<AddressTxAmountKey, AddressTxAmountComparison> buildMap(List<AddressTxAmount> addressTxAmountList) {
        Map<AddressTxAmountKey, AddressTxAmountComparison> map = new HashMap<>();
        addressTxAmountList.forEach(addressTxAmount -> {
            AddressTxAmountKey addressTxAmountKey = AddressTxAmountKey.builder()
                    .address(addressTxAmount.getAddress())
                    .txHash(addressTxAmount.getTxHash())
                    .unit(addressTxAmount.getUnit())
                    .build();
            AddressTxAmountComparison addressTxAmountComparison = AddressTxAmountComparison.builder()
                    .addressTxAmountKey(addressTxAmountKey)
                    .quantity(addressTxAmount.getQuantity())
                    .build();
            map.put(addressTxAmountKey, addressTxAmountComparison);
        });
        return map;
    }
}
