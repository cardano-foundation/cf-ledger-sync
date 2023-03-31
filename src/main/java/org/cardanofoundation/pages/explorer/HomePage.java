package org.cardanofoundation.pages.explorer;

import io.qameta.allure.Step;
import org.cardanofoundation.configs.springconfigs.pageobjects.PageObjects;
import org.junit.jupiter.api.Assertions;

import static java.lang.Thread.sleep;


@PageObjects
public class HomePage extends ExplorerBasePage{

    public void isLogoPresent() {
        Assertions.assertTrue(cardanoLogo.isDisplayed(), "Cardano logo is not displayed.");
    }

    public void isSelectedNetPresent() {
        Assertions.assertTrue(selectedNet.isDisplayed(), "Selected net is not displayed at all.");
//        Update this after net selction dropdown is identifiable.
//        Assertions.assertEquals("abd", selectedNet.getText(), "Displayed selected net is incorrect." +
//                "Expected: " +
//                "Actual: " + selectedNet.getText());
    }

    @Step("Step Step")
    public void isConnectWalletButtonPresent() throws InterruptedException {
        sleep(5000);
        Assertions.assertTrue(connectWallet.isEnabled(), "Connect Wallet button is not enabled.");
        //TODO talk to Satya about it
        Assertions.assertTrue(connectWallet.isDisplayed(), "Connect Wallet button is not present.");
    }

    public void isPageHeaderPresent() {
        Assertions.assertTrue(pageHeader.isDisplayed(), "Page header title is not present.");
    }
}
