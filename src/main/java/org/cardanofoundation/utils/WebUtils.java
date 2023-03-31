package org.cardanofoundation.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class WebUtils {

    @Lazy
    @Autowired
    ApplicationContext ctx;

    public void captureScreenshot(){
        Allure.addAttachment("Screenshot",
                new ByteArrayInputStream(((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES)));
    }

    public RemoteWebDriver getDriver(){
        return this.ctx.getBean(RemoteWebDriver.class);
    }
}
