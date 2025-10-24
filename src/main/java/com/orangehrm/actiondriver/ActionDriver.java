package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	// Initialize via constructor
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver instance is created.");
	}

	// Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("click and element: " + elementDescription);
			logger.info("clicked an element-->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to click element " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element: ",
					elementDescription + "_unable to click");
			logger.error("Unable to click element");
		}
	}

	// Method to enter text into the field
	public void enterText(By by, String value) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(value);
			logger.info("Entered text on : " + getElementDescription(by) + "-->" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to enter the value " + e.getMessage());
		}
	}

	// Method to get text from an input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get the text " + e.getMessage());
			return "";
		}
	}

	// Method to compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();

			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				logger.info("Text are matching : " + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified Successfully!" + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Text are not matching : " + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Compare Text",
						"Text Comparison Failed!" + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to compare Texts: " + e.getMessage());
		}
		return false;
	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed: " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed" + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: ",
					"Element is not displayed: " + getElementDescription(by));
			return false;
		}
	}

	// ======================JAVASCRIPT UTILITY
	// METHODS=============================//

	// Method to click using Javascript
	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("argument[0].click;", element);
			applyBorder(by, "green");
			logger.info("Clicked element using Javascript: " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Unable to click using Javascript", e);
		}
	}

	// Wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> (JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete");
			logger.info("Page load successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}
	}

	// Scroll to element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("argument[0],scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to locate element " + e.getMessage());
		}
	}

	// Wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable : " + e.getMessage());
		}
	}

	// Wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible : " + e.getMessage());
			
		}
	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		// Check for null driver or locator to avoid null pointer Exception
		if (driver == null) {
			return "driver is null";
		}
		if (locator == null) {
			return "locator is null";
		}

		try {
			// find the element using locator
			WebElement element = driver.findElement(locator);

			// Get element Attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			// Return description based on element attributes
			if (isNotEmpty(name)) {
				return "Element with name : " + name;
			} else if (isNotEmpty(id)) {
				return "Element with id : " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text : " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class : " + className;
			} else if (isNotEmpty(placeHolder)) {
				return "Element with id : " + placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element" + e.getMessage());
		}
		return "Unable to describe the element";
	}

	// Utility method to check a String is not NULL or empty
	public boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long String
	public String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility method to Border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the Element
			WebElement element = driver.findElement(by);

			// Apply the border
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + "to element " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element: " + getElementDescription(by));
		}

	}

	// =============Window and Frame handling===============================

	// Method to switch between browsers windows
	public void switchToWindow(String windowTitle) {

		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switched to window: " + windowTitle);
					return;
				}
			}
			logger.warn("Window with title " + windowTitle + "not found.");
		} catch (Exception e) {
			logger.error("Unable to switch window", e);
		}
	}

	// Method to switch to an Iframe
	public void switchToFrame(By by) {

		try {
			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switched to iframe: " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to switch to iframe", e);
		}
	}

	// Method to switch back to default content
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logger.info("Switched back to default content.");
	}

	// ======================Alert Handling=====================

	// Method to accept alert pop up
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert accepted. ");
		} catch (Exception e) {
			logger.error("No alert found to accept", e);
		}
	}

	// Method to dismiss alert pop up
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert dismissed. ");
		} catch (Exception e) {
			logger.error("No alert found to dismiss", e);
		}
	}

	// Method to get alert text
	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			logger.error("No alert text found", e);
			return "";
		}
	}

	// Method to upload a file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			logger.info("Uploaded file: " + filePath);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to upload file: " + e.getMessage());
		}
	}

}
