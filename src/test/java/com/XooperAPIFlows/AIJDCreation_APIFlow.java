package com.XooperAPIFlows;

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

	@Test(dataProvider = "APIAIJDCreationData")
	public void AIJDwithExcelTestData(String role, String minExp, String maxExp, String tone, String language,
			String skills, String description, String industry, String location, String jobType,
			String employmentType) {
		try {
			System.out.println("Starting Test: AIJD Creation");
			ExtentReportManager.startTest("AI JD Creation API Test - " + role);
			ExtentReportManager.log(Status.INFO, "Starting AI JD Creation API test with Job Title: " + role);

			API_AIJDCreationTest.testAIJDAPI(role, minExp, maxExp, tone, language, skills, description, industry,
					location, jobType, employmentType);

			ExtentReportManager.log(Status.PASS, "AI JD Creation test passed successfully for Job Title: " + role);

		} catch (Exception e) {

			ExtentReportManager.log(Status.FAIL,
					"AI JD Creation test failed for Job Title: " + role + ". Error: " + e.getMessage());

			e.printStackTrace();
			throw new RuntimeException("AI JD Creation test failed with Excel Data.", e);
		}

	}

	@DataProvider(name = "APIAIJDCreationData")
	public static Object[][] getData() {

		String filePath = "C:\\Users\\LAKSHMINARAYANA\\MyProjectXoop\\src\\main\\resource\\excelTestData\\TestData(AIJDCreation).csv";
		String sheetName = "AIJDCreation";

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
		}
	}

}
