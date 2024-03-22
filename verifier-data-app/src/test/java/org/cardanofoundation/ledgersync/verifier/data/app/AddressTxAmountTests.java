package org.cardanofoundation.ledgersync.verifier.data.app;

import org.cardanofoundation.ledgersync.verifier.data.app.entity.ledgersync.AddressTxAmount;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl.AddressTxAmountComparisonMapperKoios;
import org.cardanofoundation.ledgersync.verifier.data.app.mapper.impl.AddressTxAmountComparisonMapperLS;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressTxAmountKey;
import org.cardanofoundation.ledgersync.verifier.data.app.service.AddressTxAmountService;
import org.cardanofoundation.ledgersync.verifier.data.app.service.KoiosService;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rest.koios.client.backend.api.base.exception.ApiException;
import rest.koios.client.backend.api.transactions.model.TxInfo;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AddressTxAmountTests {

    @Autowired
    AddressTxAmountService addressTxAmountService;

    @Autowired
    KoiosService koiosService;


    @RepeatedTest(value = 20, name = RepeatedTest.LONG_DISPLAY_NAME)
    void compare_amount_of_random_1000_AddressTxAmount() throws ApiException {
        Set<String> txHashes = addressTxAmountService.getRandomTxHashes(1000);

        List<TxInfo> txInfoList = koiosService.getListTxInfoFromTxHashes(txHashes);
        List<AddressTxAmount> addressTxAmountList = addressTxAmountService.getListAddressTxAmountFromTxHashes(txHashes);

        Map<AddressTxAmountKey, AddressTxAmountComparison> addressTxAmountComparisonMapForLs = new AddressTxAmountComparisonMapperLS().buildMap(addressTxAmountList);
        Map<AddressTxAmountKey, AddressTxAmountComparison> addressTxAmountComparisonMapForKoios = new AddressTxAmountComparisonMapperKoios().buildMap(txInfoList);


        for (Map.Entry<AddressTxAmountKey, AddressTxAmountComparison> entry : addressTxAmountComparisonMapForLs.entrySet()) {
            AddressTxAmountKey addressTxAmountKey = entry.getKey();
            AddressTxAmountComparison addressTxAmountComparison = entry.getValue();
            BigInteger koiosQuantity = addressTxAmountComparisonMapForKoios.get(addressTxAmountKey).getQuantity();
            String logMessage = MessageFormat.format("comparison txHash={0}, address={1}", addressTxAmountKey.getTxHash(), addressTxAmountKey.getAddress());
            System.out.println(logMessage);
            assertEquals(koiosQuantity, addressTxAmountComparison.getQuantity());

        }

        assertEquals(addressTxAmountComparisonMapForKoios.size(), addressTxAmountComparisonMapForLs.size());

    }

    @RepeatedTest(value = 20, name = RepeatedTest.LONG_DISPLAY_NAME)
    void compare_balance_of_random_10_Address() throws ApiException {
        Set<String> addresses = addressTxAmountService.getRandomAddresses(10);
        Map<AddressBalanceComparisonKey, AddressBalanceComparison> addressBalanceComparisonMapLS = addressTxAmountService.getMapAddressBalanceFromAddress(addresses);
        Map<AddressBalanceComparisonKey, AddressBalanceComparison> addressBalanceComparisonMapKoios = koiosService.getMapAddressBalanceFromAddress(addresses);

        assertEquals(addressBalanceComparisonMapKoios.size(), addressBalanceComparisonMapLS.size());

        for (Map.Entry<AddressBalanceComparisonKey, AddressBalanceComparison> entry : addressBalanceComparisonMapLS.entrySet()) {
            AddressBalanceComparisonKey addressBalanceComparisonKey = entry.getKey();
            AddressBalanceComparison addressBalanceComparison = entry.getValue();
            BigInteger koiosBalance = addressBalanceComparisonMapKoios.get(addressBalanceComparisonKey).getBalance();
            String s = MessageFormat.format("comparison address={0}, ls_balance={1}, koios_balance={2}", addressBalanceComparisonKey.getAddress(), addressBalanceComparison.getBalance(), koiosBalance);
            System.out.println(s);
            assertEquals(koiosBalance, addressBalanceComparison.getBalance());

        }

    }

}
