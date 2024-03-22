package org.cardanofoundation.ledgersync.verifier.data.app.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.verifier.data.app.entity.ledgersync.AddressTxAmount;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl.AddressBalanceComparisonMapperLS;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.projection.AddressBalanceProjection;
import org.cardanofoundation.ledgersync.verifier.data.app.repository.ledgersync.AddressTxAmountRepository;
import org.cardanofoundation.ledgersync.verifier.data.app.service.AddressTxAmountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the AddressTxAmountService interface that provides methods to retrieve address transaction amounts.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AddressTxAmountServiceImpl implements AddressTxAmountService {

    AddressTxAmountRepository addressTxAmountRepository;

    /**
     * Retrieves a random list of address transaction amounts.
     *
     * @param maxNumOfTransaction The maximum number of transactions to retrieve.
     * @return A list of random address transaction amounts.
     */
    private List<AddressTxAmount> getRandomAddressTxAmounts(int maxNumOfTransaction) {
        long totalElements = addressTxAmountRepository.count();
        int maxPage = (int) (totalElements / maxNumOfTransaction);
        int page = new Random().nextInt(0, maxPage + 1);
        Pageable pageable = PageRequest.of(page, maxNumOfTransaction);
        return addressTxAmountRepository.findAll(pageable).stream().toList();
    }

    /**
     * Retrieves a list of address transaction amounts associated with the given set of transaction hashes.
     *
     * @param txHashes The set of transaction hashes to retrieve address transaction amounts for.
     * @return A list of address transaction amounts.
     */
    @Override
    public List<AddressTxAmount> getListAddressTxAmountFromTxHashes(Set<String> txHashes) {
        return addressTxAmountRepository.findByTxHashIn(txHashes);
    }

    /**
     * Retrieves a set of random transaction hashes.
     *
     * @param maxNumOfTxHashes The maximum number of transaction hashes to retrieve.
     * @return A set of random transaction hashes.
     */
    @Override
    public Set<String> getRandomTxHashes(int maxNumOfTxHashes) {
        List<AddressTxAmount> addressTxAmountList = getRandomAddressTxAmounts(maxNumOfTxHashes);
        return addressTxAmountList.stream().map(AddressTxAmount::getTxHash).collect(Collectors.toSet());
    }

    /**
     * Retrieves a set of random addresses associated with the transaction amounts.
     *
     * @param maxNumOfAddresses The maximum number of addresses to retrieve.
     * @return A set of random addresses.
     */
    @Override
    public Set<String> getRandomAddresses(int maxNumOfAddresses) {
        List<AddressTxAmount> addressTxAmountList = getRandomAddressTxAmounts(maxNumOfAddresses);
        return addressTxAmountList.stream().map(AddressTxAmount::getAddress).filter(address->address.startsWith("addr_")).collect(Collectors.toSet());
    }


    /**
     * Retrieves the address balance comparison information for a given set of addresses.
     *
     * @param addresses The set of addresses to retrieve balance comparison information for.
     * @return A map containing the address balance comparison information.
     */
    @Override
    public Map<AddressBalanceComparisonKey, AddressBalanceComparison> getMapAddressBalanceFromAddress(Set<String> addresses) {
        List<AddressBalanceProjection> addressBalanceProjectionList = addressTxAmountRepository.getAddressBalanceFromAddresses(addresses);
        return new AddressBalanceComparisonMapperLS().buildMap(addressBalanceProjectionList);
    }
}
