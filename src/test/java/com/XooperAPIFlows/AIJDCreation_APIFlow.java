package com.XooperAPIFlows;

import org.testng.annotations.Test;

import com.XooperAPITest.API_AIJDCreationTest;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;

public class AIJDCreation_APIFlow {

	@Test(dataProvider = "APIAIJDCreationData")
	public void AIJDwithExcelTestData(String role, int minExp, int maxExp, String tone, String language, String skills,
			String description, String industry, String location, String jobType, String employmentType) {
		try {

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

}
