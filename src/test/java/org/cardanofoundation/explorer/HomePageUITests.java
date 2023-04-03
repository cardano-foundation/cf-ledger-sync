package org.cardanofoundation.explorer;

import org.cardanofoundation.configs.junitconfigs.DisplayNameHelper;
//import org.cardanofoundation.listeners.CustomTestExecutionListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

@SpringBootTest
//@TestExecutionListeners(value = {
//        CustomTestExecutionListener.class,
//})
@DisplayNameGeneration(DisplayNameHelper.class)
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
