package com.applitools.tests;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import com.applitools.utils.DriverManager;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

public abstract class BaseTest {

    private static final VisualGridRunner runner =
            new VisualGridRunner(new RunnerOptions().testConcurrency(5));
    private static final BatchInfo batch = new BatchInfo("Login Feature Tests");
    private static final ThreadLocal<Eyes> eyesHolder = new ThreadLocal<>();

    protected String loginPageUrl;

    protected Eyes getEyes() {
        return eyesHolder.get();
    }

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("chrome") String browser, @Optional("false") String headless,
                      Method method) throws MalformedURLException {
        DriverManager.initDriver(browser, Boolean.parseBoolean(headless));
        // Set the URL 
        File loginFile = new File("login.html").getAbsoluteFile();
        loginPageUrl = loginFile.toURI().toURL().toString();

        Eyes eyes = new Eyes(runner);
        Configuration config = new Configuration();
        config.setSaveNewTests(false);
        config.setApiKey(resolveApiKey());
        config.setBatch(batch);
        config.addBrowser(1280, 800, BrowserType.CHROME);
        // config.addBrowser(1280, 800, BrowserType.FIREFOX);
        // config.addBrowser(1280, 800, BrowserType.EDGE_CHROMIUM);
        // config.addDeviceEmulation(DeviceName.iPhone_14_Pro, ScreenOrientation.PORTRAIT);
        eyes.setConfiguration(config);
        eyes.open(DriverManager.getDriver(), "Login App 4", method.getName(), new RectangleSize(1280, 800));
        eyesHolder.set(eyes);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        Eyes eyes = eyesHolder.get();
        if (eyes != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                eyes.abortAsync();
            } else {
                eyes.closeAsync();
            }
            eyesHolder.remove();
        }
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void finalizeVisualTests() {
        runner.getAllTestResults(false);
    }

    private static String resolveApiKey() {
        String key = System.getenv("APPLITOOLS_API_KEY");
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("APPLITOOLS_API_KEY environment variable is not set");
        }
        return key;
    }
}
