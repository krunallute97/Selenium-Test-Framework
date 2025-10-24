package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	//Two ways two apply RetryAnalyzer:
	//1) Using this class in test class
	//2)TestListener level
    private int retryCount=0; //number of retries
    private static final int maxRetryCount = 2; //maximum number of retries
	@Override
	public boolean retry(ITestResult result) {
		if(retryCount<maxRetryCount)
		{
			retryCount++;
			return true; //Retry the test
		}
		return false;
	}

}
