package com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
	private static ExtentReports extent;
	public static ExtentTest test;

	public static ExtentReports createInstance() {
		if (extent == null) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" + timeStamp + ".html";

			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
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
		test = extent.createTest(testName);
	}

	public static void log(Status status, String message) {
		if (test == null) {
			System.out.println("Warning: Extent Report not started. Skipping log: " + message);
			return;
		}
		test.log(status, message);
	}

	public static void log(Status status, String message, String screenShotPath) {
		if (test == null) {
			throw new IllegalStateException("Test is not started. Call startTest() first.");
		}
		test.log(status, message);
		try {
			test.addScreenCaptureFromPath(screenShotPath);
		} catch (Exception e) {
			System.out.println("Error adding screenshot: " + e.getMessage());
		}
	}

	public static void logFail(String message, String screenShotPath) {
		log(Status.FAIL, message);
		if (screenShotPath != null && !screenShotPath.isEmpty()) {
			try {
				test.addScreenCaptureFromPath(screenShotPath);
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
		}
	}

}
