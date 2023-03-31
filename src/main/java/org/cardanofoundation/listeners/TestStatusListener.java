package org.cardanofoundation.listeners;


import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public class TestStatusListener implements TestExecutionListener {
    private static String testName;

    public void executionStarted(TestIdentifier test){
        testName = test.getDisplayName().replaceAll(" ", "_");
    }

    private static String getTestName(){
        return testName;
    }

}
