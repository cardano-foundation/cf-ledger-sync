package org.example;

import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.example.appiumconfig.AppiumServer;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;

import static java.lang.Thread.sleep;

public class SampleTests {

    @Test
    public void setup() throws InterruptedException, MalformedURLException {
        extracted();
        System.out.printf("asdkalsdk");
    }


    @Step("Take Screenshot")
    private void extracted() throws InterruptedException, MalformedURLException {
        AppiumServer appiumServer = new AppiumServer();

        IOSDriver iosDriver = appiumServer.launchApp();

//        By.ByXPath asds = new By.ByXPath("//XCUIElementTypeTextField[@name=\"IntegerA\"]");
//
//        WebElement element = iosDriver.findElement(asds);
//        element.sendKeys("123");
//        Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) iosDriver).getScreenshotAs(OutputType.BYTES)));

        sleep(2000);
    }
}
