package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test
	public void verifyOrangeHRMLogo() {
		//ExtentManager.startTest("Verifying Logo"); -> This has been implemented in TestListener
		loginPage.login("Admin", "admin123");
		Assert.assertTrue(homePage.verifyOrangeHRMLogo(), "Logo is not visible");
		staticWait(2);
	}

}
