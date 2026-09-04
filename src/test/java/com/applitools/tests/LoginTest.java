package com.applitools.tests;

import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.pages.LoginPage;

import org.openqa.selenium.By;
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
        getEyes().check("Login Page", Target.window().fully().matchLevel(MatchLevel.DYNAMIC));

        // Pre-login form assertions
        Assert.assertEquals(loginPage.getPageHeading(), "Sign In",
                "Page heading should be 'Sign In'");
        Assert.assertEquals(loginPage.getTitleStrongText(), "Welcome to Demo Login",
                "Title should be 'Welcome to Demo Login'");
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(),
                "Username field should be visible");
        Assert.assertEquals(loginPage.getUsernamePlaceholder(), "Enter username",
                "Username placeholder should be 'Enter username'");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
                "Password field should be visible");
        Assert.assertEquals(loginPage.getPasswordPlaceholder(), "Enter password",
                "Password placeholder should be 'Enter password'");
        Assert.assertEquals(loginPage.getLoginButtonText(), "Login",
                "Login button text should be 'Login'");
        Assert.assertTrue(loginPage.isForgotPasswordLinkDisplayed(),
                "Forgot password link should be visible");
        Assert.assertTrue(loginPage.isRememberMeCheckboxDisplayed(),
                "Remember me checkbox should be visible");
        Assert.assertEquals(loginPage.getLoginButtonBackgroundColor(), "rgba(79, 70, 229, 1)",
                "Login button background color should be indigo (#4f46e5)");
        Assert.assertFalse(loginPage.isDashboardVisible(),
                "Dashboard should not be visible before login");

        loginPage.login(VALID_USER, VALID_PASSWORD);
        getEyes().check("Dashboard After Login", Target.window().fully().matchLevel(MatchLevel.DYNAMIC));

        // Post-login dashboard assertions
        Assert.assertTrue(loginPage.isDashboardVisible(),
                "Dashboard should be visible after successful login");
        Assert.assertEquals(loginPage.getDashboardHeading(), "Login Successful!",
                "Dashboard heading should be 'Login Successful!'");
        Assert.assertEquals(loginPage.getWelcomeMessage(), VALID_USER,
                "Welcome message should display the logged-in username");
        Assert.assertTrue(loginPage.isLogoutButtonDisplayed(),
                "Logout button should be visible on the dashboard");
        Assert.assertFalse(loginPage.isLoginFormVisible(),
                "Login form should not be visible after successful login");
    }


    @Test(description = "Invalid credentials should show an error message")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage().open(loginPageUrl);
        Assert.assertEquals(DriverManager.getDriver().findElement(By.cssSelector(".title strong")).getText(), "Welcome to Demo Login", "Page title should be 'Welcome to Demo'");
        loginPage.login(INVALID_USER, INVALID_PASS);
        Assert.assertFalse(loginPage.isDashboardVisible(), "Dashboard should not be visible on failed login");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid username or password.",
                "Error message text should match");
    }
/*
    @Test(description = "Empty fields should show a validation error")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage().open(loginPageUrl);
        Assert.assertEquals(DriverManager.getDriver().findElement(By.cssSelector(".title strong")).getText(), "Welcome to Demo", "Page title should be 'Welcome to Demo'");
        loginPage.clickLogin();
        Assert.assertFalse(loginPage.isDashboardVisible(), "Dashboard should not appear with empty fields");
        Assert.assertEquals(loginPage.getErrorMessage(), "Please enter both username and password.",
                "Validation message should prompt for both fields");
    }
*/

}
