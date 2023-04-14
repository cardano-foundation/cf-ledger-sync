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
        if (runMode.equalsIgnoreCase("githubaction")) {
            return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), getChromeOptions());
        } else if (runMode.equalsIgnoreCase("local")) {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver(getChromeOptions());
        } else{throw new IllegalArgumentException("Invalid run mode: " + browserType +
                ". Acceptable options are: local, githubaction.");
        }
    }

    private RemoteWebDriver getFirefoxDriver() {
        return null;
    }

    private RemoteWebDriver getSafariDriver() {
        return null;
    }

    private RemoteWebDriver getIosDriver() throws MalformedURLException {
        MutableCapabilities iosCapabilities = getIosCapabilities();

        URL url = new URL("");
        IOSDriver driver = new IOSDriver(url, iosCapabilities);
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
        options.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--start-maximized", "--disable-gpu",
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

