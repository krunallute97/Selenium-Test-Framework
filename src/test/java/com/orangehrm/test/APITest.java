package com.orangehrm.test;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseAPIClass;
import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class APITest extends BaseAPIClass {

	@Test//(retryAnalyzer = RetryAnalyzer.class)
	public void verifyGetUserAPI() {
		// Step1 : Define API Endpoint
		String basePath = "/users/1";
		ExtentManager.logStep("API Endpoint : " + basePath);

		// Step2 : Send GET Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = APIUtility.sendGetRequest(basePath, null);
		ExtentManager.logStep("📥 Response Body:<br><pre>" + response.asPrettyString() + "</pre>");

		// Step3 : Validate statusCode
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 200);

		Assert.assertTrue(isStatusCodeValid, "Status code is not as expected");

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("StatuCode Validation Failed");
		}

		// Step4 : Validate username
		ExtentManager.logStep("Validating response body for username");
		String userName = APIUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		Assert.assertTrue(isUserNameValid, "Username is not valid");

		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Username Validation Failed");
		}

		// Step4 : Validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = APIUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		Assert.assertTrue(isEmailValid, "Email is not valid");

		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Email Validation Failed");
		}

		// Step5 : Validate JSON Schema
		ExtentManager.logStep("Validating JSON schema for response");
		String FILE_PATH = "/src/test/resources/schemas/getUser.json";
		APIUtility.validateSchema(response, FILE_PATH);
	}

	@Test(priority = 1)
	public void verifyCreateResourceAPI() {
		// Step1 : Define API Endpoint
		String basePath = "/posts";
		String payload = APIUtility.getJsonPayload("/src/test/resources/payloads/createResourcePayload.json");
		ExtentManager.logStep("API Endpoint : " + basePath);

		// Step2 : Send POST Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = APIUtility.sendPostRequest(basePath, payload);
		ExtentManager.logStep("📥 Response Body:<br><pre>" + response.asPrettyString() + "</pre>");

		// Step3 : Validate statusCode
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 201);

		Assert.assertTrue(isStatusCodeValid, "Status code is not as expected");

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("StatuCode Validation Failed");
		}

		// Step4 : Validate title
		ExtentManager.logStep("Validating response body for title");
		String title = APIUtility.getJsonValue(response, "title");
		boolean isTitleValid = "Testing".equals(title);
		Assert.assertTrue(isTitleValid, "title is not valid");

		if (isTitleValid) {
			ExtentManager.logStepValidationForAPI("Title Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Title Validation Failed");
		}

		// Step5 : Validate body
		ExtentManager.logStep("Validating response body for body");
		String body = APIUtility.getJsonValue(response, "body");
		boolean isBodyValid = "API Automation".equals(body);
		Assert.assertTrue(isBodyValid, "Body is not valid");

		if (isBodyValid) {
			ExtentManager.logStepValidationForAPI("Body Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Body Validation Failed");
		}

		// Step6 : Validate JSON Schema
		ExtentManager.logStep("Validating JSON schema for response");
		String FILE_PATH = "/src/test/resources/schemas/CreateResourceSchema.json";
		APIUtility.validateSchema(response, FILE_PATH);

	}
}
