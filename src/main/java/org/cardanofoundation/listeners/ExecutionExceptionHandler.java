package org.cardanofoundation.listeners;

import io.qameta.allure.Allure;
import org.cardanofoundation.configs.seleniumconfigs.DriverManager;
import org.cardanofoundation.utils.WebUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

public class ExecutionExceptionHandler implements TestExecutionExceptionHandler, LifecycleMethodExecutionExceptionHandler {

    @Autowired
    WebUtils webUtils;

    @Autowired
    DriverManager driverManager;

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("in ExecutionExceptionHandler");
        Assumptions.assumeTrue(driverManager.getClass()!=null, "Driver not initialized, testcase skipped.");
        Assumptions.assumeTrue(((RemoteWebDriver)driverManager.getDriver()).getSessionId()!=null, "Session not created, testcase skipped.");
        throw throwable;
    }


    @Override
    public void handleBeforeAllMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("in ExecutionExceptionHandler");
        webUtils.captureScreenshot();
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("in ExecutionExceptionHandler");
        webUtils.captureScreenshot();
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("in ExecutionExceptionHandler");
        webUtils.captureScreenshot();
        throw throwable;
    }

    @Override
    public void handleAfterAllMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        webUtils.captureScreenshot();
        throw throwable;
    }
}
