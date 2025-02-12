package com.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import com.aventstack.extentreports.Status;

public class RetryTransformer implements IAnnotationTransformer {

	private static final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		if (annotation == null) {
			logToExtentReport("ERROR", "ITestAnnotation is null. Skipping retry setup.");
			return;
		}

		String methodName = (testMethod != null) ? testMethod.getName() : "Unknown Method";
		logToExtentReport("INFO", "Execution started for test method: " + methodName);

		Class<? extends IRetryAnalyzer> retry = annotation.getRetryAnalyzerClass();
		if (retry == null) {
			logToExtentReport("INFO", "Setting RetryAnalyzer for method: " + methodName);
			annotation.setRetryAnalyzer(RetryAnalyzer.class); // Assign RetryAnalyzer
		} else {
			logToExtentReport("INFO", "RetryAnalyzer is already set for method: " + methodName);
		}
	}

	private void logToExtentReport(String level, String message) {

		String timestamp = dateFormat.get().format(new Date());
		String logMessage = "[" + timestamp + "] " + message;

		if (ExtentReportManager.test != null) {
			switch (level.toUpperCase()) {
			case "INFO":
				ExtentReportManager.log(Status.INFO, logMessage);
				break;
			case "WARNING":
				ExtentReportManager.log(Status.WARNING, logMessage);
				break;
			case "ERROR":
				ExtentReportManager.log(Status.FAIL, logMessage);
				break;
			default:
				ExtentReportManager.test.log(Status.INFO, logMessage);
				break;
			}
			System.out.println(logMessage);
		}

	}
}
