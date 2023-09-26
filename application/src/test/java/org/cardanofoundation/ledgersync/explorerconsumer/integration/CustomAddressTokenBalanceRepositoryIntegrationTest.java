package org.cardanofoundation.ledgersync.explorerconsumer.integration;

import org.cardanofoundation.explorer.consumercommon.entity.AddressTokenBalance;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.impl.CustomAddressTokenBalanceRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomAddressTokenBalanceRepositoryIntegrationTest {

    @Autowired
    private CustomAddressTokenBalanceRepositoryImpl repository;

    @Test
    @Transactional
    @Rollback()
    void testFindAllByAddressFingerprintPairIn() {
        List<Pair<String, String>> addressFingerprintPairs = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            addressFingerprintPairs.add(Pair.of("addr1q9h4dvhr7kqhtz7fhs6tmkwt5vvh207jxqc627auajfw9rhey6vulmq96cy4h7m84c7gc7tzmq63v3t60hxgjp33zfyq64m0ph", "asset1z8cuw684wfqyadfnpz228w562kd75f26ss3uww"));
        }

        for (int i = 0; i < 3000; i++) {
            Collection<AddressTokenBalance> result = repository.findAllByAddressFingerprintPairIn(addressFingerprintPairs);
            assertNotNull(result);
        }
    }

}

