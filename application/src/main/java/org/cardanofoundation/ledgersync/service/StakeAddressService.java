package org.cardanofoundation.ledgersync.service;

import org.cardanofoundation.ledgersync.consumercommon.entity.StakeAddress;
import org.cardanofoundation.ledgersync.consumercommon.entity.Tx;

import java.util.Collection;
import java.util.Map;

public interface StakeAddressService {

    /**
     * Handle stake addresses along with its first appeared tx
     *
     * @param stakeAddressTxHashMap a map with key is the stake address and value is
     *                              its first appeared tx hash
     * @param txMap                 a map with key is tx hash and value is the
     *                              respective tx entity
     * @return a map of stake address found and newly created
     */
    Map<String, StakeAddress> handleStakeAddressesFromTxs(
            Map<String, String> stakeAddressTxHashMap, Map<String, Tx> txMap);

    /**
     * Find stake address records by their raw hash
     *
     * @param hashRaw a collection of stake address raw hashes
     * @return a collection of stake addresses found
     */
    Collection<StakeAddress> findStakeAddressByHashRawIn(Collection<String> hashRaw);
}
