package com.applitools.tests;

import com.applitools.utils.DriverManager;
import com.applitools.utils.EyesManager;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

public abstract class BaseTest {

    protected String loginPageUrl;

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("chrome") String browser, @Optional("false") String headless,
                      Method method) throws MalformedURLException {
        DriverManager.initDriver(browser, Boolean.parseBoolean(headless));
        File loginFile = new File("login.html").getAbsoluteFile();
        loginPageUrl = loginFile.toURI().toURL().toString();
        EyesManager.openEyes(method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // Abort on failure so mismatched baselines don't pollute the dashboard
        if (result.getStatus() == ITestResult.FAILURE) {
            EyesManager.abortEyes();
        } else {
            EyesManager.closeEyes();
        }
        DriverManager.quitDriver();
    }

    // Wait for all UFG renders to complete and surface any visual diffs
    @AfterSuite
    public void finalizeVisualTests() {
        EyesManager.finalizeResults();
    }
}
