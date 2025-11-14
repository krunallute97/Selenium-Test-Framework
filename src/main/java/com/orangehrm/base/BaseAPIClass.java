package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeSuite;

public class BaseAPIClass {

	protected static Properties prop;

	@BeforeSuite
	public void loadAPIConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		System.out.println("API config.properties file loaded");
	}
	
	//checking github from eclipse
}
