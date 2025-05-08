package com.XooperAPIFlows;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.XooperAPITest.API_AIJDCreationTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.utils.ExcelUtils;
import com.utils.ExtentReportManager;

public class AIJDCreation_APIFlow {

	@BeforeSuite
	public void setupReport() {
		ExtentReportManager.createInstance();
	}

	@Test(dataProvider = "ValidAPIAIJDCreationData", timeOut = 40000)
	public void AIJDwithExcelTestData(String role, String minExp, String maxExp, String tone, String language,
			String skills, String description, String industry, String location, String jobType,
			String employmentType) {
		try {
			ExtentReportManager.startTest("AI JD Creation API Test - Valid Data - " + role);
			ExtentReportManager.log(Status.INFO, "Testing AI JD API with valid data: " + role);

			String apiResponse = API_AIJDCreationTest.testAIJDAPI(role, minExp, maxExp, tone, language, skills,
					description, industry, location, jobType, employmentType);

			Assert.assertFalse(apiResponse.contains("Error"), "Unexpected error for valid input!");
			ExtentReportManager.log(Status.PASS, "AI JD Creation test passed for valid data: " + role);
		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL,
					"AI JD Creation test failed for valid data: " + role + ". Error: " + e.getMessage());
			throw new RuntimeException("Valid AIJD test failed", e);
		}

	}

	@Test(dataProvider = "InvalidAPIAIJDCreationData", expectedExceptions = AssertionError.class, timeOut = 40000)
	public void AIJDwithInvalidExcelTestData(String role, String minExp, String maxExp, String tone, String language,
			String skills, String description, String industry, String location, String jobType,
			String employmentType) {
		System.out.println("Testing API with invalid data: " + role);
		ExtentReportManager.startTest("AI JD Creation API Test - Invalid Data - " + role);
		ExtentReportManager.log(Status.INFO, "Testing AI JD API with invalid data: " + role);

		API_AIJDCreationTest.testAIJDAPI(role, minExp, maxExp, tone, language, skills, description, industry, location,
				jobType, employmentType);

		ExtentReportManager.log(Status.FAIL, "Test failed! API accepted invalid data: " + role);
	}

	@DataProvider(name = "ValidAPIAIJDCreationData")
	public static Object[][] getData() {

		String filePath = "C:\\Users\\LAKSHMINARAYANA\\MyProjectXoop\\src\\main\\resource\\excelTestData\\Testdata1(AIJDCreation_Data).csv";
		String sheetName = "ValidAIJDCreation_Data";

		if (!Files.exists(Paths.get(filePath))) {
			throw new RuntimeException("Test data file not found: " + filePath);
		}

		if (filePath.endsWith(".csv")) {
			return ExcelUtils.readCSVData(filePath);
		} else if (filePath.endsWith(".xlsx")) {
			return ExcelUtils.readExcelData(filePath, sheetName);
		} else {
			throw new RuntimeException("Unsupported file format: " + filePath);
		}
	}

	@DataProvider(name = "InvalidAPIAIJDCreationData")
	public static Object[][] invalidDataProvider() {
		String filePath = "C:\\Users\\LAKSHMINARAYANA\\MyProjectXoop\\src\\main\\resource\\excelTestData\\TestData2_Invalid(Sheet1).csv";
		String sheetName = "Sheet1";

		if (!Files.exists(Paths.get(filePath))) {
			throw new RuntimeException("Test data file not found: " + filePath);
		}

		if (filePath.endsWith(".csv")) {
			return ExcelUtils.readCSVData(filePath);
		} else if (filePath.endsWith(".xlsx")) {
			return ExcelUtils.readExcelData(filePath, sheetName);
		} else {
			throw new RuntimeException("Unsupported file format: " + filePath);
		}

	}

	@AfterSuite
	public void tearDown() {
		ExtentReports extent = ExtentReportManager.getInstance();
		if (extent != null) {
			extent.flush();
			System.out.println("Extent report flushed successfully.");
		} else {
			System.err.println("Error: ExtentReports instance is null. Report not generated.");
		}
	}

}
