package org.cardanofoundation.explorer;

import org.cardanofoundation.pages.explorer.HomePage;
import org.cardanofoundation.configs.seleniumconfigs.DriverManager;
import org.cardanofoundation.utils.GeneralUtils;
import org.cardanofoundation.utils.WebUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class ExplorerTestsBase {

    public String testResult;

    @Lazy
    @Autowired
    protected DriverManager driverManager;

    @Lazy
    @Autowired
    protected GeneralUtils generalUtils;

    @Lazy
    @Autowired
    protected WebUtils webUtils;

    @Lazy
    @Autowired
    HomePage homePage;

    @BeforeEach
    public void setupDriver() throws InterruptedException {
        testResult = "failed";
        driverManager.setupDriver();
        homePage.isPageHeaderDisplayed();
    }

    @AfterEach
    public void tearDownTest(){
        if (testResult.equalsIgnoreCase("failed"))
            webUtils.captureScreenshot();
        driverManager.tearDown();
    }

}
