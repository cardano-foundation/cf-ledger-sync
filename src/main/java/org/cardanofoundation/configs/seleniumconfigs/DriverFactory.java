package org.cardanofoundation.configs.seleniumconfigs;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Lazy
@Configuration
@ComponentScan
public class DriverFactory {

    @Value( "${browser.type}" )
    String browserType;

    @Value( "${run.mode}" )
    String runMode;

    @Value( "${SELENIUM_HUB}" )
    String seleniumHub;

    @Value( "${SAUCE_HUB}" )
    String sauceHub;

    @Lazy
    @Autowired
    ApplicationContext ctx;

    @Scope("remotedriverscope")
    @Bean
    public RemoteWebDriver initializeDriver() throws MalformedURLException {
        return switch (browserType.toUpperCase()) {
            case "CHROME" -> getChromeDriver();
            case "FIREFOX" -> getFirefoxDriver();
            case "SAFARI" -> getSafariDriver();
            case "IOS" -> getIosDriver();
            case "ANDROID" -> getAndroidDriver();
            default -> throw new IllegalArgumentException("Invalid browser type: " + browserType);
        };
    }

    private RemoteWebDriver getChromeDriver() throws MalformedURLException {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        if (runMode.equalsIgnoreCase("local")){
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }else if(runMode.equalsIgnoreCase("seleniumhub")){
            return new RemoteWebDriver(new URL(seleniumHub), getChromeOptions());
        }else if(runMode.equalsIgnoreCase("saucehub")){
            return new RemoteWebDriver(new URL(sauceHub), getChromeOptions());
        }else{
            throw new IllegalArgumentException("Invalid run mode: " + browserType +
                    ". Acceptable options are: local, seleniumhub, saucehub.");
        }
    }

    private RemoteWebDriver getFirefoxDriver() {
        return null;
    }

    private RemoteWebDriver getSafariDriver() {
        return null;
    }

    private RemoteWebDriver getIosDriver() throws MalformedURLException {
        MutableCapabilities asdsd = getIosCapabilities();

        URL url = new URL("https://oauth-jaspreet.kaur-bb783:6809639d-25d1-4f6b-b960-0a2efe086852@ondemand.eu-central-1.saucelabs.com:443/wd/hub");
        IOSDriver driver = new IOSDriver(url, asdsd);
    return driver;
    }

    public ChromeOptions getChromeOptions(){
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", preferences);
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--no-sandbox", "--start-maximized", "--disable-dev-shm-usage", "--disable-gpu",
                "--ignore-certificate-errors", "--disable-extensions", "--remote-debugging-port=9222");

        return options;
    }

    private MutableCapabilities getIosCapabilities() throws MalformedURLException {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName","iOS");
        caps.setCapability("appium:deviceName","iPhone.*");
        caps.setCapability("appium:deviceOrientation", "portrait");
        caps.setCapability("appium:automationName", "XCUITest");
        caps.setCapability("appium:app", "storage:filename=proverbial_ios.ipa");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("build", "appium-build-UFTHQ1");
        sauceOptions.setCapability("appiumVersion", "2.0.0");
        sauceOptions.setCapability("name", "testInfo.getDisplayName()");
        caps.setCapability("sauce:options", sauceOptions);



        return caps;
    }

    private RemoteWebDriver getAndroidDriver() {
        return null;
    }
}
