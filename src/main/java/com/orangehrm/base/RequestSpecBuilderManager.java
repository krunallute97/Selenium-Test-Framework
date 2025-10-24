package com.orangehrm.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecBuilderManager {

	private static RequestSpecification baseSpec;

	// Create the base spec only once
	public static RequestSpecification getBaseSpec() {
		   if (baseSpec == null) {
	            // Get API key from BaseClass properties
			   String baseURI = BaseAPIClass.prop.getProperty("apiUrl");
	            String apiKey = BaseAPIClass.prop.getProperty("apiKey");

	            baseSpec = new RequestSpecBuilder()
	                    .setBaseUri(baseURI)
	                    .setContentType(ContentType.JSON)
	                    .addHeader("Accept", "application/json; charset=UTF-8")
	                    .addHeader("x-api-key", apiKey) // Example header for API key
	                    .build();
	        }
	        return baseSpec;
	    }

}
