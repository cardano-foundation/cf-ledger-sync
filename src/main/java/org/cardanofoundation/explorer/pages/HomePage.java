package org.cardanofoundation.explorer.pages;

import io.qameta.allure.Step;
import org.cardanofoundation.springconfigs.pageobjects.PageObjects;
import org.cardanofoundation.utils.WebUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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

    @Step("ajbhuhds")
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
