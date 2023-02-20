package org.cardanofoundation.cardanowallet.shelly.sequential.wallets;

import com.bloxbean.cardano.client.crypto.bip39.MnemonicException;
import io.qameta.allure.Epic;
import org.cardanofoundation.apihelpers.cardanowallet.shelly.sequential.WalletsAPIHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Epic("Cardano Wallet")
@DisplayName("Shelly(Sequential) - Wallet API Tests")
public class CreateRestoreTests {

    @Autowired
    WalletsAPIHelper walletsAPI;

    @BeforeEach
    public void setDefaults() throws MnemonicException.MnemonicLengthException {
        walletsAPI.prepareCreateRestoreRequest();
    }

    @Test
    public void validReqReturns201(){
        walletsAPI.sendCreateRestoreRequest();
        walletsAPI.returnedHttpCodeIs(405);
    }

    @Test
    public void missingOptionalValuesinPayloadReturns201(){
        Assertions.assertTrue(false);
    }


    @ParameterizedTest(name = " - {0}")
    @ValueSource(strings = {"PUT", "DELETE"})
    void incorrectHTTPMethodReturns405(String httpMethod){
        walletsAPI.restBase.amendHttpMethod(httpMethod);
        walletsAPI.sendCreateRestoreRequest();
        walletsAPI.returnedHttpCodeIs(405);
    }
}


