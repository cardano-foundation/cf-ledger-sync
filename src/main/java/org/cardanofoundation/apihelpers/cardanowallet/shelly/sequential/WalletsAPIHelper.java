package org.cardanofoundation.apihelpers.cardanowallet.shelly.sequential;

import com.bloxbean.cardano.client.crypto.bip39.MnemonicCode;
import com.bloxbean.cardano.client.crypto.bip39.MnemonicException;
import com.bloxbean.cardano.client.crypto.bip39.Words;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.cardanofoundation.pojos.wallets.shelly.sequential.walletsapi.createrestore.request.CreateRestoreWallet;
import org.cardanofoundation.configs.restassuredconfigs.RestBase;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

@Component
public class WalletsAPIHelper {


    private static final String CREATE_RESTORE_ENDPOINT = "/wallets";
    MnemonicCode mnemonicCode = new MnemonicCode();
    Random random = new Random();

    @Autowired
    public RestBase restBase;

    public WalletsAPIHelper() throws IOException {
    }

    @Step("Prepare a valid Create/Restore Wallet Request.")
    public void prepareCreateRestoreRequest() throws MnemonicException.MnemonicLengthException {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put(RestBase.CONTENT_TYPE, "application/json");
        restBase.setHeaders(headers);
        prepareCreateRestorePayload();
    }

    @Step("Make a POST call to /wallets endpoint.")
    public void sendCreateRestoreRequest() {
        restBase.post("WalletCreateRestore", getCreateRestoreEndpoint(), true, true, false, false);
    }

    private void prepareCreateRestorePayload() throws MnemonicException.MnemonicLengthException {
        CreateRestoreWallet payload = new CreateRestoreWallet();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        payload.setName("Automation Test Wallet " + dtf.format(now));

        payload.setMnemonicSentence(mnemonicCode.createMnemonic(Words.EIGHTEEN));
        payload.setMnemonicSecondFactor(mnemonicCode.createMnemonic(Words.TWELVE));
        payload.setPassphrase(RandomStringUtils.randomAlphabetic(random.nextInt(255 - 10 + 1) + 10));
        payload.setAddressPoolGap(random.nextInt(20 - 10 + 1) + 10);
        restBase.setPayload(new Gson().toJson(payload));
    }

    private String getCreateRestoreEndpoint() {
        return RestBase.API_HOSTNAME + CREATE_RESTORE_ENDPOINT;
    }

    @Step("Verify HTTP status code is 201")
    public void returnedHttpCodeIs(int statusCode) {
        Assertions.assertEquals(statusCode, restBase.getResponse().getStatusCode(), "Expected and actual HTTP status code do not match.");
    }
}
