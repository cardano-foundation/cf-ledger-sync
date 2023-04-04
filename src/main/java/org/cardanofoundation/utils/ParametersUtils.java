package org.cardanofoundation.utils;

import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.openqa.selenium.devtools.DevTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ParametersUtils {

    @Autowired
    GeneralUtils generalUtils;
    @Value( "${Selenium_Hub}" )
    String seleniumHub;

    @Value( "${FE_URL}" )
    String URL;

    public String getUrl(){
        return URL;
    }

    public String getHubUrl() {
        if (System.getProperty("runMode").equalsIgnoreCase("hub")){
            return seleniumHub;
        }else if (System.getProperty("runMode").equalsIgnoreCase("mobile")){
            return getSauceHub();
        }
        return "what?";
    }

    private String getSauceHub() {
        return "null";
    }

}
