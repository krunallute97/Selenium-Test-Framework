package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test (dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {

		// ExtentManager.startTest("Valid Login Test"); -> This has been implemented in
		// TestListener
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successful login ");
		homePage.logout();
		ExtentManager.logStep("Logout successfully!");
		staticWait(2);
	}

	@Test(dataProvider = "invalidLoginData", dataProviderClass = DataProviders.class)
	public void verifyInvalidLoginTest(String username, String password) {
		SoftAssert softAssert= getSoftAssert();
		
		// ExtentManager.startTest("Invalid Login Test"); -> This has been implemented
		// in TestListener
		loginPage.login(username, password);
		String expectedErrorMessage = "Invalid credentials";
		softAssert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test failed : Invalid error message");
		
		softAssert.assertAll(); // to report all failure at last
		
		//soft assert useful when multiple assertions are there in test case and we want run test case completely without minor failure.
	}

}
