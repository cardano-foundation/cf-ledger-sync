package org.example.appiumconfig;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;

public class AppiumServer {
    private final AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
    private AppiumDriverLocalService server;
    private int port;

//    public AppiumServer() {
//        this.port = getAvailablePort();
//        this.serviceBuilder.usingPort(port);
//        this.server = AppiumDriverLocalService.buildService(serviceBuilder);
//        this.server.start();
//    }

    public AppiumDriverLocalService get(){
        return this.server;
    }

    public int getAvailablePort() {
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

    public IOSDriver launchApp() throws InterruptedException, MalformedURLException {

//        this.port = getAvailablePort();
//        this.serviceBuilder.usingPort(port);
//        this.server = AppiumDriverLocalService.buildService(serviceBuilder);
//        this.server.start();

//        AppiumServer appiumServer = new AppiumServer();
//        AppiumDriverLocalService as = appiumServer.get();

//        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
//        xcuiTestOptions.setPlatformName("iOS")
//                .setAutomationName("xcuitest")
//                .setApp("/Users/jaspreetkaur/GitHub/cf-qa-testsuite/src/main/resources/TestApp.app.zip");
//        IOSDriver iosDriver = new IOSDriver(new URL("http://127.0.0.1:4723/"), xcuiTestOptions);
////        IOSDriver iosDriver = new IOSDriver(as, xcuiTestOptions);
//
//        return iosDriver;
//        AppiumDriver appiumDriver = new AppiumDriver();

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        desiredCapabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir") + "/src/main/resources/TestApp.app.zip");
        IOSDriver iosDriver = new IOSDriver(new URL("http://127.0.0.1:4723"), desiredCapabilities);

        return iosDriver;
    }
}
