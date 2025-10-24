package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;

	// Define locators using By class

	private By userNameField = By.xpath("//input[@placeholder='Username']");
	private By passwordField = By.name("password");
	private By loginButton = By.cssSelector("button[type='submit']");
	private By errorMessage = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");

	// Initialize action driver
	/*
	 * public LoginPage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver); }
	 */
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	// Method to perform login
	public void login(String userName, String password) {
		actionDriver.enterText(userNameField, userName);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}

	// Method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}

	// Method to get Text from error message
	public String getErrorMessage() {
		return actionDriver.getText(errorMessage);
	}

	// Verify if error message is correct
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
	}

}
