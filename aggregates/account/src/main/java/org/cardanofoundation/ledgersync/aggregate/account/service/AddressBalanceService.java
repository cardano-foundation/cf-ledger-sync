package org.cardanofoundation.ledgersync.aggregate.account.service;


import org.cardanofoundation.ledgersync.aggregate.account.domain.AggregatedAddressBalance;

import java.util.Collection;
import java.util.Map;

public interface AddressBalanceService {

    /**
     * Handle all addresses' balances
     *
     * @param aggregatedAddressBalanceMap a map with key is address string (Base58 or Bech32 form) and
     *                                    value is its aggregated balance
     * @param stakeAddressMap             a map with key is raw stake address hex and value is the
     *                                    respective stake address entity
     * @param txMap                       a map with key is tx hash and value is the respective tx
     *                                    entity
     */
    void handleAddressBalance(
            Map<String, AggregatedAddressBalance> aggregatedAddressBalanceMap,
            Map<String, String> stakeAddressMap);

    /**
     * Rollback address balances stats
     *
     * @param txs txs being rolled back
     */
    void rollbackAddressBalances(Long blockNo);
}