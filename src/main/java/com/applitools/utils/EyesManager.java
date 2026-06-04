package com.applitools.utils;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;

public class EyesManager {

    // One runner and batch shared for the entire suite
    private static final VisualGridRunner runner =
            new VisualGridRunner(new RunnerOptions().testConcurrency(5));

    private static final BatchInfo batch = new BatchInfo("Login Feature Tests");

    // Per-thread Eyes instance (safe for parallel TestNG execution)
    private static final ThreadLocal<Eyes> eyesHolder = new ThreadLocal<>();

    private EyesManager() {}

    public static Eyes getEyes() {
        return eyesHolder.get();
    }

    /**
     * Opens an Eyes session for the given test method name.
     * Called in @BeforeMethod so each test gets its own session.
     */
    public static void openEyes(String testName) {
        Eyes eyes = new Eyes(runner);

        // Setters return the base Configuration type, so don't chain — mutate in place
        Configuration config = new Configuration();
        config.setApiKey(resolveApiKey());
        config.setBatch(batch);
        // Desktop browsers rendered on Ultrafast Grid
        config.addBrowser(1280, 800, BrowserType.CHROME);
        config.addBrowser(1280, 800, BrowserType.FIREFOX);
        config.addBrowser(1280, 800, BrowserType.EDGE_CHROMIUM);
        // Mobile emulation
        config.addDeviceEmulation(DeviceName.iPhone_14_Pro, ScreenOrientation.PORTRAIT);

        eyes.setConfiguration(config);
        eyes.open(DriverManager.getDriver(), "Login App", testName, new RectangleSize(1280, 800));
        eyesHolder.set(eyes);
    }

    /**
     * Closes the Eyes session normally (triggers comparison on the dashboard).
     * Call on test pass.
     */
    public static void closeEyes() {
        Eyes eyes = eyesHolder.get();
        if (eyes != null) {
            eyes.closeAsync();
            eyesHolder.remove();
        }
    }

    /**
     * Aborts the Eyes session without comparison.
     * Call on test failure so no false mismatches appear on the dashboard.
     */
    public static void abortEyes() {
        Eyes eyes = eyesHolder.get();
        if (eyes != null) {
            eyes.abortAsync();
            eyesHolder.remove();
        }
    }

    /**
     * Waits for all UFG renders to finish and returns consolidated results.
     * Call once in @AfterSuite.
     */
    public static void finalizeResults() {
        runner.getAllTestResults(false);
    }

    private static String resolveApiKey() {
        String key = System.getenv("APPLITOOLS_API_KEY");
        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "APPLITOOLS_API_KEY environment variable is not set");
        }
        return key;
    }
}
