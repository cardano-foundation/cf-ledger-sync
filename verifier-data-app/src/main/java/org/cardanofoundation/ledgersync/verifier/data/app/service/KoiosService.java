package org.cardanofoundation.ledgersync.verifier.data.app.service;

import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import rest.koios.client.backend.api.base.exception.ApiException;
import rest.koios.client.backend.api.transactions.model.TxInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service interface for interacting with the Koios backend.
 */
public interface KoiosService {

    /**
     * Retrieves transaction information for a given set of transaction hashes.
     *
     * @param txHashes The set of transaction hashes to retrieve information for.
     * @return A list of transaction information.
     * @throws ApiException if an error occurs while retrieving the transaction information.
     */
    List<TxInfo> getListTxInfoFromTxHashes(Set<String> txHashes) throws ApiException;

    /**
     * Retrieves address balance comparison information for a given set of addresses.
     *
     * @param addresses The set of addresses to retrieve balance comparison information for.
     * @return A map of address balance comparison information.
     * @throws ApiException if an error occurs while retrieving the address balance comparison information.
     */
    Map<AddressBalanceComparisonKey, AddressBalanceComparison> getMapAddressBalanceFromAddress(Set<String> addresses) throws ApiException;
}
