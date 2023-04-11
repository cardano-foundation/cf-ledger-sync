package org.cardanofoundation.configs.seleniumconfigs;

import org.cardanofoundation.utils.GeneralUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static java.lang.Thread.sleep;

@Component
public class DriverManager {

    @Autowired
    GeneralUtils generalUtils;

    @Lazy
    @Autowired
    ApplicationContext ctx;

    @Value( "${FE_URL}" )
    String url;

    @Value( "${run.mode}" )
    String runMode;

    @Value( "${browser.type}" )
    String browserType;

    ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    public void setupDriver() throws InterruptedException {
        generalUtils.addInfoLog("Starting " + browserType.toUpperCase() + " browser.....!");
        driver.set(this.ctx.getBean(RemoteWebDriver.class));
        if ((runMode.equalsIgnoreCase("local") && browserType.equalsIgnoreCase("chrome")) ||
                runMode.equalsIgnoreCase("firefox") ||
                runMode.equalsIgnoreCase("safari") ||
                runMode.equalsIgnoreCase("edge")){
            getDriver().manage().window().maximize();
        }
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        getDriver().get(url);

        sleep(1000);

    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public void tearDown(){
        getDriver().close();
        generalUtils.addInfoLog("Driver closed.");
        getDriver().quit();
        generalUtils.addInfoLog("Testcase execution finished.");
    }

}
