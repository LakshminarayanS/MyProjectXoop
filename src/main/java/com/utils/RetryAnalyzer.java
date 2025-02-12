package com.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int retryCount = 0;
	private static final int maxRetryCount = 2; // Number of retries

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			retryCount++;
			ExtentReportManager.log(Status.WARNING, "Retrying test: " + result.getName() + " | Attempt: " + retryCount);
			return true; // Retry the test
		}
		return false; // Stop retrying
	}

}
