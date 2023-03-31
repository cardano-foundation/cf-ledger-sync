package org.cardanofoundation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Lazy
@Configuration
@ComponentScan
public class DriverFactory {

    @Autowired
    ParametersUtils parametersUtils;

    @Autowired
    CapabilitiesManager capabilitiesManager;

    @Scope("remotedriverscope")
    @Bean
    public RemoteWebDriver initializeDriver() throws MalformedURLException {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        String browser = System.getProperty("browser");
        return switch (browser.toUpperCase()) {
            case "CHROME" -> getChromeDriver();
            case "FIREFOX" -> getFirefoxDriver();
            case "SAFARI" -> getSafariDriver();
            case "IOS" -> getIosDriver();
            case "ANDROID" -> getAndroidDriver();
            default -> throw new IllegalArgumentException("Invalid browser type: " + browser);
        };
    }

    private RemoteWebDriver getChromeDriver() throws MalformedURLException {
        if (System.getProperty("runMode").equalsIgnoreCase("local")){
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }else {
            return new RemoteWebDriver(new URL(parametersUtils.getHubUrl()), capabilitiesManager.getChromeOptions());
        }
    }

    private RemoteWebDriver getFirefoxDriver() {
        return null;
    }

    private RemoteWebDriver getSafariDriver() {
        return null;
    }

    private RemoteWebDriver getIosDriver() {
        return null;
    }

    private RemoteWebDriver getAndroidDriver() {
        return null;
    }
}
