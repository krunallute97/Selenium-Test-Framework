package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.orangehrm.base.RequestSpecBuilderManager;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIUtility {

	// Global spec, initialized once
	private static final RequestSpecification spec = RequestSpecBuilderManager.getBaseSpec();

//	// Method to send the GET request
//	public static Response sendGetRequest(String baseURI, String basePath) {		
//		RestAssured.baseURI = baseURI;
//		RestAssured.basePath = basePath;
//
//		return RestAssured.get();
//	}

	// Method to send the POST request
//	public static Response sendPostRequest(String endpoint, String payload) {
//		return RestAssured.given().header("Content-Type", "application/json").body(payload).post();
//	}

	// Method to validate the Response status
//	public static boolean validateStatusCode(Response response, int statusCode) {
//		return response.getStatusCode() == statusCode;
//	}

	// Method to extract value from JSON response
//	public static String getJsonValue(Response response, String value) {
//		return response.jsonPath().getString(value);
//	}

	// Modified Methods//

	// ------------------- GET Method -------------------
	public static Response sendGetRequest(String basePath, Map<String, Object> queryParams) {
		// RequestSpecification spec = RequestSpecBuilderManager.getBaseSpec();

		// Add query parameters if present
		if (queryParams != null && !queryParams.isEmpty()) {
			spec.queryParams(queryParams);
		}

		return RestAssured.given().spec(spec).basePath(basePath).when().get().then().log().all().extract().response();

	}

	// ------------------- POST Method -------------------
	public static Response sendPostRequest(String basePath, Object payload) {
		return RestAssured.given().spec(spec).basePath(basePath).body(payload).when().post().then().log().all()
				.extract().response();
	}

	// ------------------- PUT Method -------------------
	public static Response sendPutRequest(String basePath, Object payload, Map<String, Object> queryParams) {

		// Add query parameters if present
		if (queryParams != null && !queryParams.isEmpty()) {
			spec.queryParams(queryParams);
		}

		return RestAssured.given().spec(spec).body(payload).basePath(basePath).when().put().then().log().all().extract()
				.response();
	}

	// ------------------- DELETE Method -------------------
	public static Response sendDeleteRequest(String basePath, Map<String, Object> queryParams) {

		// Add query parameters if present
		if (queryParams != null && !queryParams.isEmpty()) {
			spec.queryParams(queryParams);

		}

		return RestAssured.given().spec(spec).basePath(basePath).when().delete().then().log().all().extract()
				.response();
	}

	// ------------------- STATUSCODE Validation Method -------------------
	public static boolean validateStatusCode(Response response, int expectedStatusCode) {
		return response.getStatusCode() == expectedStatusCode;
	}

	// ------------------- Extract JSON Value Method -------------------
	public static String getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);
	}

	// ------------------- JSON Schema Validator Method -------------------
	public static void validateSchema(Response response, String schemaFilePath) {
		File schemaFile = new File(System.getProperty("user.dir") + schemaFilePath);
		try {
			response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
			ExtentManager.logStepValidationForAPI("✅ Schema Validation Passed for: " + schemaFile.getName());
		} catch (AssertionError e) {
			ExtentManager
					.logFailureAPI("❌ Schema Validation Failed for: " + schemaFile.getName() + "<br>" + e.getMessage());
			// Assert.fail("Schema validation failed: " + e.getMessage());
		}
	}

	// ------------------- Store Value from Response Method -------------------
	public static String getValueFromResponse(Response response, String jsonPath) {
		try {
			String value = response.jsonPath().getString(jsonPath);
			ExtentManager.logStepValidationForAPI(
					"🔹 Extracted value from response: <b>" + jsonPath + " = " + value + "</b>");
			return value;
		} catch (Exception e) {
			ExtentManager.logFailureAPI("❌ Failed to extract value for path: " + jsonPath + "<br>" + e.getMessage());
			return null;
		}
	}

	// ------------------- Store Value from Response Method -------------------
	public static String getJsonPayload(String relativeFilePath) {
		try {
			String fullPath = System.getProperty("user.dir") + relativeFilePath;
			return new String(Files.readAllBytes(Paths.get(fullPath)));
		} catch (IOException e) {
			ExtentManager
					.logFailureAPI("❌ Failed to read JSON payload from: " + relativeFilePath + "<br>" + e.getMessage());
			throw new RuntimeException("Failed to read JSON file: " + relativeFilePath, e);
		}
	}

}
