package org.cardanofoundation.configs.seleniumconfigs;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class CapabilitiesManager {

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
}
