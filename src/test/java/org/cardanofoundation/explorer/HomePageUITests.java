package org.cardanofoundation.explorer;

import org.cardanofoundation.explorer.pages.HomePage;
import org.cardanofoundation.utils.ChangeDisplayName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.HasAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

@SpringBootTest
@DisplayNameGeneration(ChangeDisplayName.class)
@DisplayName("Home Page UI Tests")
public class HomePageUITests extends ExplorerTestsBase{


    @Test
    public void verifyCardanoLogoPresence(){
        homePage.isLogoPresent();
    }

    @Test
    public void verifySelectedNet(){
        homePage.isSelectedNetPresent();
    }
    @Test
    public void verifyConnectWalletButtonPresence() throws InterruptedException {
        homePage.isConnectWalletButtonPresent();
    }
}
