package org.example.appiumconfig;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.time.Duration;

import static java.lang.Thread.sleep;

public class SampleTests {



    public static void main(String[] args) {
        AndroidDriver androidDriver = launchAppAndroid();
    }


    public static IOSDriver launchAppiOS() {

        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        AppiumDriverLocalService server;

        serviceBuilder.usingPort(getAvailablePort());
        server = AppiumDriverLocalService.buildService(serviceBuilder);
        server.start();

        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
        xcuiTestOptions.setPlatformName("iOS")
                .setPlatformVersion("16.2")
                .setAutomationName("XCUITest")
                .setDeviceName("iPhone 12 Pro")
                .setSimulatorStartupTimeout(Duration.ofMinutes(5));
//                .setWdaStartupRetries(4)
//                .setWdaStartupRetryInterval(Duration.ofSeconds(1));

//        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
//        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
//        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "16.2");
//        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 14");
//        desiredCapabilities.setCapability("simulatorStartupTimeout", 240000);
//        desiredCapabilities.setCapability("wdaStartupRetries", 4);
//        desiredCapabilities.setCapability("wdaStartupRetryInterval", 20000);
//        desiredCapabilities.setCapability("iosInstallPause", 8000);

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("Working Directory = " + server.getUrl());
        xcuiTestOptions.setApp(System.getProperty("user.dir") + "/src/main/resources/TestApp.app.zip");
        IOSDriver iosDriver = new IOSDriver(server.getUrl(), xcuiTestOptions);

        return iosDriver;
    }

    public static AndroidDriver launchAppAndroid() {

        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        AppiumDriverLocalService server;

        serviceBuilder.usingPort(getAvailablePort());
        server = AppiumDriverLocalService.buildService(serviceBuilder);
        server.start();

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
//        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "16.2");
//        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 14");
        desiredCapabilities.setCapability("uiautomator2ServerInstallTimeout", 60000);
//        desiredCapabilities.setCapability("wdaStartupRetries", 4);
//        desiredCapabilities.setCapability("wdaStartupRetryInterval", 20000);
//        desiredCapabilities.setCapability("iosInstallPause", 8000);

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("Working Directory = " + server.getUrl());
        desiredCapabilities.setCapability("app", System.getProperty("user.dir") + "/src/main/resources/ApiDemos-debug.apk");
        AndroidDriver androidDriver = new AndroidDriver(server.getUrl(), desiredCapabilities);
//        androidDriver.findElement(By"Animation").click();
        WebElement ele = androidDriver.findElement(By.className("android.widget.TextView"));
        System.out.println(ele.getAttribute("text"));
        return androidDriver;
    }

    public static int getAvailablePort() {
        int port = 4723;

        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return port;
    }

}
