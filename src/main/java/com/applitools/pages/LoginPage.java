package com.applitools.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    @FindBy(id = "message")
    private WebElement messageEl;

    @FindBy(id = "dashboard")
    private WebElement dashboard;

    @FindBy(id = "welcomeUser")
    private WebElement welcomeUser;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    private static final By MESSAGE_LOCATOR  = By.id("message");
    private static final By DASHBOARD_LOCATOR = By.id("dashboard");
    private static final By USERNAME_LOCATOR = By.id("username");

    public LoginPage() {
        super();
    }

    public LoginPage open(String url) {
        driver.get(url);
        waitForVisible(USERNAME_LOCATOR);
        return this;
    }

    public LoginPage enterUsername(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    public LoginPage clickLogin() {
        loginButton.click();
        return this;
    }

    public LoginPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    public boolean isDashboardVisible() {
        return isElementVisible(DASHBOARD_LOCATOR);
    }

    public String getWelcomeMessage() {
        waitForVisible(DASHBOARD_LOCATOR);
        return welcomeUser.getText();
    }

    public String getErrorMessage() {
        waitForVisible(MESSAGE_LOCATOR);
        return messageEl.getText();
    }

    public LoginPage clickLogout() {
        logoutButton.click();
        return this;
    }

    public boolean isLoginFormVisible() {
        return usernameInput.isDisplayed();
    }

    public String getLoginButtonBackgroundColor() {
        return loginButton.getCssValue("background-color");
    }
}
