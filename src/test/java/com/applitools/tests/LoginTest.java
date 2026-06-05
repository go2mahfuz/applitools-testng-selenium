package com.applitools.tests;

import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.pages.LoginPage;
import com.applitools.utils.EyesManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private static final String VALID_USER     = "admin";
    private static final String VALID_PASSWORD = "password123";
    private static final String INVALID_USER   = "admin";
    private static final String INVALID_PASS   = "wrongpass";

    @Test(description = "Valid credentials should show the dashboard")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage().open(loginPageUrl);
        EyesManager.getEyes().check("Login Page", Target.window().fully());

        loginPage.login(VALID_USER, VALID_PASSWORD);
        EyesManager.getEyes().check("Dashboard After Login", Target.window().fully());

        Assert.assertTrue(loginPage.isDashboardVisible(), "Dashboard should be visible after login");
        Assert.assertEquals(loginPage.getWelcomeMessage(), VALID_USER,
                "Welcome message should display the logged-in username");
    }

    @Test(description = "Invalid credentials should show an error message")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage().open(loginPageUrl);
        EyesManager.getEyes().check("Login Page", Target.window().fully());

        loginPage.login(INVALID_USER, INVALID_PASS);
        EyesManager.getEyes().check("Error Message After Invalid Login", Target.window().fully());

        Assert.assertFalse(loginPage.isDashboardVisible(), "Dashboard should not be visible on failed login");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid username or password.",
                "Error message text should match");
    }

    @Test(description = "Empty fields should show a validation error")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage().open(loginPageUrl);
        EyesManager.getEyes().check("Login Page", Target.window().fully());

        loginPage.clickLogin();
        EyesManager.getEyes().check("Validation Error After Empty Submit", Target.window().fully());

        Assert.assertFalse(loginPage.isDashboardVisible(), "Dashboard should not appear with empty fields");
        Assert.assertEquals(loginPage.getErrorMessage(), "Please enter both username and password.",
                "Validation message should prompt for both fields");
    }

    @Test(description = "Logout should return to the login form")
    public void testLogout() {
        LoginPage loginPage = new LoginPage().open(loginPageUrl);

        loginPage.login(VALID_USER, VALID_PASSWORD);
        EyesManager.getEyes().check("Dashboard Before Logout", Target.window().fully());
        Assert.assertTrue(loginPage.isDashboardVisible(), "Dashboard should be visible before logout");

        loginPage.clickLogout();
        EyesManager.getEyes().check("Login Page After Logout", Target.window().fully());
        Assert.assertTrue(loginPage.isLoginFormVisible(), "Login form should reappear after logout");
    }
}
