package org.cardanofoundation.ledgersync.verifier.data.app;

import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparison;
import org.cardanofoundation.ledgersync.verifier.data.app.model.AddressBalanceComparisonKey;
import org.cardanofoundation.ledgersync.verifier.data.app.service.AddressBalanceService;
import org.cardanofoundation.ledgersync.verifier.data.app.service.AddressService;
import org.cardanofoundation.ledgersync.verifier.data.app.service.KoiosService;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import rest.koios.client.backend.api.base.exception.ApiException;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AddressBalanceTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressBalanceService addressBalanceService;

    @Autowired
    KoiosService koiosService;

    @RepeatedTest(value = 20, name = RepeatedTest.LONG_DISPLAY_NAME)
    void compare_balance_of_random_10_Address() throws ApiException {
        Set<String> addresses;
        do {
            addresses = addressService.getRandomAddresses(10);
        } while (addresses.size() != 10);


        Map<AddressBalanceComparisonKey, AddressBalanceComparison> addressBalanceComparisonMapLS = addressBalanceService.getMapAddressBalanceFromAddress(addresses);
        Map<AddressBalanceComparisonKey, AddressBalanceComparison> addressBalanceComparisonMapKoios = koiosService.getMapAddressBalanceFromAddress(addresses);

        for (Map.Entry<AddressBalanceComparisonKey, AddressBalanceComparison> entry : addressBalanceComparisonMapLS.entrySet()) {
            AddressBalanceComparisonKey addressBalanceComparisonKey = entry.getKey();
            AddressBalanceComparison addressBalanceComparison = entry.getValue();
            BigInteger koiosBalance = addressBalanceComparisonMapKoios.get(addressBalanceComparisonKey).getBalance();
            String s = MessageFormat.format("comparison address={0}, ls_balance={1}, koios_balance={2}", addressBalanceComparisonKey.getAddress(), addressBalanceComparison.getBalance(), koiosBalance);
            System.out.println(s);
            assertEquals(koiosBalance, addressBalanceComparison.getBalance());

        }

        assertEquals(addressBalanceComparisonMapKoios.size(), addressBalanceComparisonMapLS.size());
    }

}
