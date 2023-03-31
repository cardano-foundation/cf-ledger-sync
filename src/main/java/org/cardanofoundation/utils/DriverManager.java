package org.cardanofoundation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v109.network.Network;
import org.openqa.selenium.devtools.v109.network.model.Headers;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

@Component
public class DriverManager {

    @Autowired
    GeneralUtils generalUtils;

    @Autowired
    ParametersUtils parametersUtils;

    @Lazy
    @Autowired
    ApplicationContext ctx;

    ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    public void setupDriver() throws InterruptedException {
        String runMode = System.getProperty("runMode");
        String browser = System.getProperty("browser");
        generalUtils.addInfoLog("Starting " + browser + " driver.....!");
        driver.set(this.ctx.getBean(RemoteWebDriver.class));
        if ((runMode.equalsIgnoreCase("local") && browser.equalsIgnoreCase("chrome")) ||
                runMode.equalsIgnoreCase("firefox") ||
                runMode.equalsIgnoreCase("safari") ||
                runMode.equalsIgnoreCase("edge")){
            getDriver().manage().window().maximize();
        }
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//        DevTools devTools = ((ChromeDriver)getDriver()).getDevTools();
//        devTools.createSession();
//        devTools.send(Network.enable(Optional.of(100000), Optional.of(100000), Optional.of(100000)));
//        String auth = System.getProperty("username") + ":" + System.getProperty("password");
//        String encodeToString = Base64.getEncoder().encodeToString(auth.getBytes());
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("Authorization", "Basic " + encodeToString);
//        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));

        getDriver().get(parametersUtils.getUrl());

        sleep(15000);

    }

    private WebDriver getDriver() {
        return driver.get();
    }

    public void tearDown(){
        getDriver().close();
        generalUtils.addInfoLog("Driver closed.");
        getDriver().quit();
        generalUtils.addInfoLog("Testcase execution finished.");
    }

}
