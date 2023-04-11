package org.cardanofoundation.explorer;

import io.qameta.allure.Epic;
import org.cardanofoundation.configs.junitconfigs.DisplayNameHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Epic("Phase1")
@DisplayNameGeneration(DisplayNameHelper.class)
@DisplayName("Home Page UI Tests")
public class HomePageTests extends ExplorerTestsBase{


    @Test
    public void verifyHeaderElements() throws InterruptedException {
        homePage.isLogoPresent()
                .isSearchBarDisplayed()
                .verifyFiltersDropdown()
                .isSelectedNetworkDisplayed()
                .verifyNetworkOptions()
                .isConnectWalletButtonPresent();
    }

    @Test
    public void verifyAdaPriceBox() {
        homePage.isAdaPriceBoxDisplayed()
                .isAdaPriceIconDisplayed()
                .verifyAdaPriceBoxTitle()
                .verifyAdaCurrentPrice()
                .verifyAdaPriceInBTC();
    }

    @Test
    public void verifyMarketCapBox() {
        homePage.isMarketCapBoxDisplayed()
                .isMarketCapIconDisplayed()
                .verifyMarketCapBoxTitle()
                .verifyMarketCapValue();
    }

    @Test
    public void verifyCurrentEpochBox() {
        homePage.isCurrentEpochBoxDisplayed()
                .isCurrentEpochIconDisplayed()
                .verifyCurrentEpochBoxTitle()
                .verifyCurrentEpochNumber()
                .verifyCurrentSlotNumber()
                .verifyTotalSlotsCount();
    }

    @Test
    public void verifyLiveStakeBox() {
        homePage.isLiveStakeBoxDisplayed()
                .isLiveStakeIconDisplayed()
                .verifyLiveStakeBoxTitle()
                .verifyActiveStakeNumber()
                .verifyCirculatingSupply();
    }

}
