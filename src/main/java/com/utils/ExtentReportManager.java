package com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
	private static ExtentReports extent;
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	public static ExtentReports createInstance() {
		if (extent == null) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" + timeStamp + ".html";

			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			System.out.println("Extent Report initialized: " + reportPath);
		}
		return extent;
	}

	public static synchronized ExtentReports getInstance() {
		if (extent == null) {
			throw new IllegalStateException("ExtentReports instance is not initialized. Call createInstance() first.");
		}
		return extent;
	}

	public static void startTest(String testName) {
		if (extent == null) {
			throw new IllegalStateException("ExtentReports instance is not initialized. Call createInstance() first.");
		}
		ExtentTest newTest = extent.createTest(testName);
		test.set(newTest);
	}

	public static void log(Status status, String message) {
		ExtentTest currentTest = test.get();
		if (currentTest == null) {
			System.out.println("Warning: Test not started. Skipping log: " + message);
			return;
		}
		currentTest.log(status, message);
	}

	public static void log(Status status, String message, String screenShotPath) {
		ExtentTest currentTest = test.get();
		if (currentTest == null) {
			throw new IllegalStateException("Test is not started. Call startTest() first.");
		}
		currentTest.log(status, message);
		try {
			currentTest.addScreenCaptureFromPath(screenShotPath);
		} catch (Exception e) {
			System.out.println("Error adding screenshot: " + e.getMessage());
		}
	}

	public static void logFail(String message, String screenShotPath) {
		log(Status.FAIL, message);
		if (screenShotPath != null && !screenShotPath.isEmpty()) {
			try {
				test.get().addScreenCaptureFromPath(screenShotPath);
			} catch (Exception e) {
				System.out.println("Error attaching screenshot: " + e.getMessage());
			}
		}
	}

	public static void logSkip(String message) {
		log(Status.SKIP, message);
	}

	public static void pass(String message) {
		log(Status.PASS, message);
	}

	public static void endTest() {

		if (extent != null) {
			extent.flush();
			System.out.println("Extent Report generated successfully.");
		} else {
			System.out.println("Warning: No Extent Report instance found. Nothing to flush.");
		}
	}

}
