package org.cardanofoundation.pages.explorer;

import io.qameta.allure.Step;
import org.apache.commons.collections4.CollectionUtils;
import org.cardanofoundation.configs.springconfigs.pageobjects.PageObjects;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;


@PageObjects
public class HomePage extends ExplorerBasePage{

    @FindBy(xpath = "//div[@data-testid='NetworkSelectionDropdown']/div[@aria-haspopup='listbox']")
    public WebElement networkSelectionDropdown;

    @FindBy(xpath = "//li[@data-testid='NetworkOptions']")
    public List<WebElement> networkOptions;

    @FindBy(xpath = "//div[@data-testid='AdaPriceBox']")
    public WebElement adaPriceBox;

    @FindBy(xpath = "//img[@data-testid='AdaPriceIcon']")
    public WebElement adaPriceIcon;

    @FindBy(xpath = "//h4[@data-testid='AdaPriceBoxTitle']")
    public WebElement adaPriceBoxTitle;

    @FindBy(xpath = "//h3[@data-testid='AdaCurrentPrice']")
    public WebElement adaCurrentPrice;

    @FindBy(xpath = "//small[@data-testid='AdaPriceInBTC']")
    public WebElement adaPriceInBTC;

    @FindBy(xpath = "//div[@data-testid='MarketCapBox']")
    public WebElement marketCapBox;

    @FindBy(xpath = "//img[@data-testid='MarketCapIcon']")
    public WebElement marketCapIcon;

    @FindBy(xpath = "//h4[@data-testid='MarketCapBoxTitle']")
    public WebElement marketCapBoxTitle;

    @FindBy(xpath = "//h3[@data-testid='MarketCapValue']")
    public WebElement currentMarketCap;

    @FindBy(xpath = "//div[@data-testid='CurrentEpochBox']")
    public WebElement currentEpochBox;

    @FindBy(xpath = "//img[@data-testid='CurrentEpochIcon']")
    public WebElement currentEpochIcon;

    @FindBy(xpath = "//h4[@data-testid='CurrentEpochBoxTitle']")
    public WebElement currentEpochBoxTitle;

    @FindBy(xpath = "//span[@data-testid='EpochLabel']")
    public WebElement epochLabel;

    @FindBy(xpath = "//span[@data-testid='CurrentEpochNumber']")
    public WebElement currentEpochNumber;

    @FindBy(xpath = "//span[@data-testid='SlotLabel']")
    public WebElement slotLabel;

    @FindBy(xpath = "//span[@data-testid='CurrentSlotNumber']")
    public WebElement currentSlotNumber;

    @FindBy(xpath = "//span[@data-testid='TotalSlots']")
    public WebElement totalSlots;

    @FindBy(xpath = "//div[@data-testid='LiveStakeBox']")
    public WebElement liveStakeBox;

    @FindBy(xpath = "//img[@data-testid='LiveStakeIcon']")
    public WebElement liveStakeIcon;

    @FindBy(xpath = "//h4[@data-testid='LiveStakeBoxTitle']")
    public WebElement liveStakeBoxTitle;

    @FindBy(xpath = "//small[@data-testid='ActiveStakeLabel']")
    public WebElement activeStakeLabel;

    @FindBy(xpath = "//small[@data-testid='ActiveStakeValue']")
    public WebElement activeStakeValue;

    @FindBy(xpath = "//small[@data-testid='ActiveStakePercentage']")
    public WebElement activeStakePercentage;

    @FindBy(xpath = "//small[@data-testid='CirculatingSupplyLabel']")
    public WebElement circulatingSupplyLabel;

    @FindBy(xpath = "//small[@data-testid='CirculatingSupplyValue']")
    public WebElement circulatingSupplyValue;


    @FindBy(xpath = "//small[@data-testid='CirculatingSupplyPercentage']")
    public WebElement circulatingSupplyPercentage;

    @FindBy(xpath = "//div[@data-testid='LiveStakeProgressBar']")
    public WebElement liveStakeProgressBar;

    @FindBy(xpath = "//div[@data-testid='AllFiltersDropdown']")
    public WebElement allFiltersDropdown;

    @FindBy(xpath = "//li[@data-testid='FilterOptions']")
    public List<WebElement> filterOptions;

    @FindBy(xpath = "//div[@data-testid='SearchBar']/input")
    public WebElement searchBar;

    public void isPageHeaderDisplayed() {
        Assertions.assertTrue(pageHeader.isDisplayed(), "Page header title is not present.");
    }

    @Step("Verifying Cardano Logo is present.")
    public HomePage isLogoPresent() {
        Assertions.assertTrue(cardanoLogo.isDisplayed(), "Cardano logo is not displayed.");
        return this;
    }

    @Step("Verify Search bar is displayed.")
    public HomePage isSearchBarDisplayed() {
        Assertions.assertTrue(searchBar.isDisplayed(), "Search Bar is not displayed.");
        Assertions.assertEquals("Search transactions, address, blocks, epochs, pools...", searchBar.getAttribute("placeholder"), "Placeholder text is not displayed.");
        return this;
    }

    @Step("Verify filter dropdown and its values.")
    public HomePage verifyFiltersDropdown() throws InterruptedException {
        Assertions.assertTrue(allFiltersDropdown.isDisplayed(), "Filters dropdown is not displayed.");
        allFiltersDropdown.click();
        sleep(2000);
        List<String> expectedOptions = Arrays.asList("All Filters", "Epochs", "Blocks", "Transactions",
                "Tokens", "Stake Keys", "Addresses", "Pools");

        Assertions.assertEquals(8, filterOptions.size(),
                "Incorrect number of options in the All Filter dropdown.");

        List<String> actualOptions = filterOptions.stream().map(WebElement::getText).toList();
        Assertions.assertTrue(CollectionUtils.isEqualCollection(expectedOptions, actualOptions), "Values in filter dropdown are incorrect. " +
                " Expected Values: " + expectedOptions +
                " Actual Values: " +actualOptions);
        return this;
    }

    @Step("Verifying selected network is displayed with Cardano logo & is correct.")
    public HomePage isSelectedNetworkDisplayed() {
        Assertions.assertTrue(selectedNet.isDisplayed(), "Selected net is not displayed with logo.");

        //TODO Talk to Satya about it.
        Assertions.assertEquals(networkSelectionDropdown.getText(), selectedNet.getText(),
                "Displayed selected network is incorrect." +
                " Expected: " + networkSelectionDropdown.getText() +
                " Actual: " + selectedNet.getText());

        return this;
    }

    @Step("Verifying network options are correct.")
    public HomePage verifyNetworkOptions() {
        List<String> expectedNetworks = Arrays.asList("Mainnet", "Preprod", "Preview", "Testnet");
        //TODO Talk to Satya about it.
        networkSelectionDropdown.click();
        Assertions.assertEquals(4, networkOptions.size(),
                "Incorrect count." +
                        " Expected options: 4" +
                        " Actual options: " + networkOptions.size());

        List<String> actualNetworks = networkOptions.stream().map(WebElement::getText).toList();
        Assertions.assertTrue(CollectionUtils.isEqualCollection(expectedNetworks, actualNetworks));
        return this;
    }


    @Step("Verify Connect Wallet button is displayed.")
    public HomePage isConnectWalletButtonPresent() throws InterruptedException {
        Assertions.assertTrue(connectWallet.isEnabled(), "Connect Wallet button is not enabled.");
        //TODO talk to Satya about it
        Assertions.assertTrue(connectWallet.isDisplayed(), "Connect Wallet button is not present.");
        return this;
    }














    @Step("Verify Ada Price box is displayed.")
    public HomePage isAdaPriceBoxDisplayed() {
        Assertions.assertTrue(adaPriceBox.isDisplayed(), "Ada Price box is not displayed.");
        return this;
    }

    @Step("Verify Ada Price icon is displayed in Ada Price box.")
    public HomePage isAdaPriceIconDisplayed() {
        Assertions.assertTrue(adaPriceIcon.isDisplayed(), "Ada Price icon is not displayed.");
        return this;
    }

    @Step("Verify Ada Price box title is displayed and is correct.")
    public HomePage verifyAdaPriceBoxTitle() {
        Assertions.assertTrue(adaPriceBoxTitle.isDisplayed(), "Ada Price title is not displayed.");
        Assertions.assertEquals("Ada Price", adaPriceBoxTitle.getText(), "Ada Price title is incorrect.");
        return this;
    }

    @Step("Verify Ada Price box displays current Ada price.")
    public HomePage verifyAdaCurrentPrice() {
        Assertions.assertTrue(adaCurrentPrice.isDisplayed(), "Ada Current Price is not displayed.");
        //TODO - Data price Validation once I get the postgress acess and KT.
        return this;
    }

    @Step("Verify Ada Price box displays Ada price in BTC.")
    public void verifyAdaPriceInBTC() {
        Assertions.assertTrue(adaPriceInBTC.isDisplayed(), "Ada Price in BTC is not displayed.");
        //TODO - BTC Data price Validation once I get the postgress acess and KT.
    }

    @Step("Verify Market Cap box is displayed.")
    public HomePage isMarketCapBoxDisplayed() {
        Assertions.assertTrue(marketCapBox.isDisplayed(), "Market Cap box is not displayed.");
        return this;
    }

    @Step("Verify Market Cap icon is displayed.")
    public HomePage isMarketCapIconDisplayed() {
        Assertions.assertTrue(marketCapIcon.isDisplayed(), "Market Cap icon is not displayed.");
        return this;
    }

    @Step("Verify Market Cap box title is displayed and is correct.")
    public HomePage verifyMarketCapBoxTitle() {
        Assertions.assertTrue(marketCapBoxTitle.isDisplayed(), "Market Cap box title is not displayed.");
        Assertions.assertEquals("Market Cap", marketCapBoxTitle.getText(), "Market Cap title is incorrect.");
        return this;
    }

    @Step("Verify Market Cap box displays current market cap value.")
    public void verifyMarketCapValue() {
        Assertions.assertTrue(currentMarketCap.isDisplayed(), "Ada Current Price is not displayed.");
        //TODO - Data price Validation once I get the postgress acess and KT.
    }

    @Step("Verify Current Epoch box is displayed.")
    public HomePage isCurrentEpochBoxDisplayed() {;
        Assertions.assertTrue(currentEpochBox.isDisplayed(), "Current Epoch box is not displayed.");
        return this;
    }

    @Step("Verify Current Epoch icon is displayed.")
    public HomePage isCurrentEpochIconDisplayed() {
        Assertions.assertTrue(currentEpochIcon.isDisplayed(), "Current Epoch icon is not displayed.");
        return this;
    }

    @Step("Verify Current Epoch box title is displayed and is correct.")
    public HomePage verifyCurrentEpochBoxTitle() {
        Assertions.assertTrue(currentEpochBoxTitle.isDisplayed(), "Current Epoch box title is not displayed.");
        Assertions.assertEquals("Current Epoch", currentEpochBoxTitle.getText(), "Current Epoch box title is incorrect.");
        return this;
    }

    @Step("Verify current Epoch number is displayed and is correct.")
    public HomePage verifyCurrentEpochNumber() {
        Assertions.assertTrue(epochLabel.isDisplayed(), "Epoch label is not displayed.");
        Assertions.assertEquals("Epoch:", epochLabel.getText(), "Epoch label has incorrect case.");
        Assertions.assertTrue(currentEpochNumber.isDisplayed(), "Epoch number is not displayed.");
        //TODO - Data price Validation once I get the postgress acess and KT.
        return this;
    }

    @Step("Verify current Slot number is displayed and is correct.")
    public HomePage verifyCurrentSlotNumber() {
        Assertions.assertTrue(slotLabel.isDisplayed(), "Slot label is not displayed.");
        Assertions.assertEquals("Slot:", slotLabel.getText(), "Slot has incorrect case.");
        Assertions.assertTrue(currentSlotNumber.isDisplayed(), "Current slot number is not displayed.");
        //TODO - Data price Validation once I get the postgress acess and KT.
        return this;
    }

    @Step("Verify total slots count is displayed and is correct.")
    public void verifyTotalSlotsCount() {
        Assertions.assertTrue(totalSlots.isDisplayed(), "Total slots number is not displayed.");
        //TODO - Data price Validation once I get the postgress acess and KT.
    }


    @Step("Verify Live Stake box is displayed.")
    public HomePage isLiveStakeBoxDisplayed() {
        Assertions.assertTrue(liveStakeBox.isDisplayed(), "Live Stake box is not displayed.");
        return this;
    }

    @Step("Verify Live Stake icon is displayed.")
    public HomePage isLiveStakeIconDisplayed() {
        Assertions.assertTrue(liveStakeIcon.isDisplayed(), "Live Stake icon is not displayed.");
        //Todo Confirm with Satya about details of icon verification
        return this;
    }

    @Step("Verify Live Stake box title is displayed and is correct.")
    public HomePage verifyLiveStakeBoxTitle() {
        Assertions.assertTrue(liveStakeBoxTitle.isDisplayed(), "Live Stake box title is not displayed.");
        Assertions.assertEquals("Live Stake", liveStakeBoxTitle.getText(), "Live Stake box title is incorrect.");
        return this;
    }

    @Step("Verify Active Stake number is displayed and is correct.")
    public HomePage verifyActiveStakeNumber() {
        Assertions.assertTrue(activeStakeLabel.isDisplayed(), "Active Stake label is not displayed.");
        Assertions.assertEquals("Active Stake:", activeStakeLabel.getText(), "Active Stake label has incorrect case.");

        Assertions.assertTrue(activeStakeValue.isDisplayed(), "Active Stake value is not displayed.");
        Assertions.assertTrue(activeStakePercentage.isDisplayed(), "Active Stake percentage is not displayed.");
        //TODO - Data price Validation once I get the postgress acess and KT.
        return this;
    }

    @Step("Verify Circulating Supply is displayed and is correct.")
    public void verifyCirculatingSupply() {
        Assertions.assertTrue(circulatingSupplyLabel.isDisplayed(), "Circulating Supply label is not displayed.");
        Assertions.assertEquals("Circulating Supply:", circulatingSupplyLabel.getText(), "Circulating Supply label has incorrect case.");

        Assertions.assertTrue(circulatingSupplyValue.isDisplayed(), "Circulating Supply value is not displayed.");
        Assertions.assertTrue(circulatingSupplyPercentage.isDisplayed(), "Circulating Supply percentage is not displayed.");
        //TODO - Data price Validation once I get the postgress acess and KT.
    }


}
