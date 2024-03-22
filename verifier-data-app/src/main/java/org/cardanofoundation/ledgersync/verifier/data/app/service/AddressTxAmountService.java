package org.cardanofoundation.ledgersync.verifier.data.app.service;

import org.cardanofoundation.ledgersync.verifier.data.app.entity.ledgersync.AddressTxAmount;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service interface for retrieving address transaction amounts and balance comparisons.
 */
public interface AddressTxAmountService {

    /**
     * Retrieves address transaction amounts for a given set of transaction hashes.
     *
     * @param txHashes The set of transaction hashes to retrieve address transaction amounts for.
     * @return A list of address transaction amounts.
     */
    List<AddressTxAmount> getListAddressTxAmountFromTxHashes(Set<String> txHashes);

    /**
     * Generates a set of random transaction hashes.
     *
     * @param maxNumOfTxHashes The maximum number of transaction hashes to generate.
     * @return A set of random transaction hashes.
     */
    Set<String> getRandomTxHashes(int maxNumOfTxHashes);

    /**
     * Generates a set of random addresses.
     *
     * @param maxNumOfAddresses The maximum number of addresses to generate.
     * @return A set of random addresses.
     */
    Set<String> getRandomAddresses(int maxNumOfAddresses);

    /**
     * Retrieves address balance comparison information for a given set of addresses.
     *
     * @param addresses The set of addresses to retrieve balance comparison information for.
     * @return A map of address balance comparison information.
     */
    Map<AddressBalanceComparisonKey, AddressBalanceComparison> getMapAddressBalanceFromAddress(Set<String> addresses);
}
