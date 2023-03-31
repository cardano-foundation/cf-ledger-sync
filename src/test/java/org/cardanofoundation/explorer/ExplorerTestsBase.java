package org.cardanofoundation.explorer;

import org.cardanofoundation.explorer.pages.HomePage;
import org.cardanofoundation.utils.DriverManager;
import org.cardanofoundation.utils.GeneralUtils;
import org.cardanofoundation.utils.ParametersUtils;
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
    protected ParametersUtils parametersUtils;
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
        testResult = "unknown";
        driverManager.setupDriver();
        homePage.isPageHeaderPresent();
    }

    @AfterEach
    public void tearDownTest(){
        if (testResult.equalsIgnoreCase("failed"))
            webUtils.captureScreenshot();
        driverManager.tearDown();
    }

}
