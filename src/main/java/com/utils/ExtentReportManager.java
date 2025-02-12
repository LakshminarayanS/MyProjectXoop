package com.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
	private static ExtentReports extent;
	public static ExtentTest test;

	public static ExtentReports createInstance(String fileName) {
		ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		return extent;
	}

	public static void startTest(String testName) {
		if (extent == null) {
			throw new IllegalStateException("ExtentReports instance is not initialized. Call createInstance() first.");
		}

		test = extent.createTest(testName);
	}

	public static void log(Status status, String message) {
		if (test == null) {
			throw new IllegalStateException("Test is not started. Call startTest() first.");
		}

		test.log(status, message);
	}

	public static void endTest() {

		if (extent != null) {
			extent.flush();
		}
	}
	
}
