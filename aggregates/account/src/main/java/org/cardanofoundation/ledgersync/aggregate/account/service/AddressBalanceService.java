package org.cardanofoundation.ledgersync.aggregate.account.service;


import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedAddressBalance;

import java.util.Map;

public interface AddressBalanceService {

    /**
     * Handle all addresses' balances
     *
     * @param aggregatedAddressBalanceMap a map with key is address string (Base58 or Bech32 form) and
     *                                    value is its aggregated balance
     * @param stakeAddressMap             a map with key is raw stake address hex and value is the
     *                                    respective stake address entity
     */
    void handleAddressBalance(
            Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap,
            Map<String, String> stakeAddressMap);

    /**
     * Rollback address balances stats
     */
    void rollbackAddressBalances(Long blockNo);
}
