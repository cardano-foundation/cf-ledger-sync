package org.cardanofoundation.utils;

import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.openqa.selenium.devtools.DevTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParametersUtils {

    @Autowired
    GeneralUtils generalUtils;

    public String getUrl(){
        return System.getProperty("URL");
    }

    public String getHubUrl() {
        if (System.getProperty("runMode").equalsIgnoreCase("docker")){
            return getGridUrl();
        }else if (System.getProperty("runMode").equalsIgnoreCase("mobile")){
            return getSauceHub();
        }
        return "what?";
    }

    private String getSauceHub() {
        return "null";
    }

    private String getGridUrl() {
        return "http://dev.cf-jkaur-cardano-wallet.metadata.dev.cf-deployments.org:14444/wd/hub";
    }
}
