package org.cardanofoundation.ledgersync.verifier.data.app.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl.AddressBalanceComparisonMapperKoios;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.service.KoiosService;
import org.springframework.stereotype.Service;
import rest.koios.client.backend.api.address.AddressService;
import rest.koios.client.backend.api.address.model.AddressInfo;
import rest.koios.client.backend.api.base.exception.ApiException;
import rest.koios.client.backend.api.transactions.TransactionsService;
import rest.koios.client.backend.api.transactions.model.TxInfo;
import rest.koios.client.backend.factory.options.Options;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the KoiosService interface for interacting with KOIOS backend services.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KoiosServiceImpl implements KoiosService {

    AddressService addressService;
    TransactionsService transactionsService;

    /**
     * Retrieves transaction information for the given set of transaction hashes.
     *
     * @param txHashes The set of transaction hashes to retrieve information for.
     * @return A list of transaction information.
     * @throws ApiException If an error occurs while retrieving the transaction information.
     */
    @Override
    public List<TxInfo> getListTxInfoFromTxHashes(Set<String> txHashes) throws ApiException {
        return transactionsService.getTransactionInformation(txHashes.stream().toList(), Options.EMPTY).getValue();
    }

    /**
     * Retrieves address balance comparison information for the given set of addresses.
     *
     * @param addresses The set of addresses to retrieve balance comparison information for.
     * @return A map containing address balance comparison information.
     * @throws ApiException If an error occurs while retrieving the address balance comparison information.
     */
    @Override
    public Map<AddressBalanceComparisonKey, AddressBalanceComparison> getMapAddressBalanceFromAddress(Set<String> addresses) throws ApiException {
        List<AddressInfo> addressInfoList = new ArrayList<>();
        for (String address : addresses) {
            addressInfoList.add(addressService.getAddressInformation(address).getValue());
        }
        return new AddressBalanceComparisonMapperKoios().buildMap(addressInfoList);
    }
}
