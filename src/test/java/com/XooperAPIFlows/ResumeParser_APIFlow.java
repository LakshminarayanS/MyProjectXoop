package com.XooperAPIFlows;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.POJO_CandidateMatching.com.CandidateProfile;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ResumeParser_APIFlow {

	private static final String ResumeParser_BASE_URL = "https://dev.xooper.in/resume_parser/resume-extraction";

	@BeforeSuite
	public void setupReport() {
		ExtentReportManager.createInstance();
	}

	@Test
	public void ResumeParseUploadFile() {

		try {

			ExtentReportManager.startTest("Resume Parser API Test Started");
			ExtentReportManager.log(Status.INFO, "Testing Resume Parser API");
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("ResumeParserData/SoftwareDeveloperResume.pdf");

			if (inputStream == null) {
				throw new RuntimeException("File not found in resources.");
			}

			File file = File.createTempFile("resume", ".pdf");
			try (FileOutputStream out = new FileOutputStream(file)) {
				inputStream.transferTo(out);
			}

			RestAssured.baseURI = ResumeParser_BASE_URL;

			Response response = given().multiPart("file", file).when().post();

			int statusCode = response.getStatusCode();
			String responseBody = response.getBody().asString();

			ExtentReportManager.log(Status.INFO, "Status Code: " + statusCode);
			ExtentReportManager.log(Status.INFO, "Response Body: " + responseBody);

			System.out.println("Status Code: " + statusCode);
			System.out.println("Response Body: " + responseBody);

			CandidateProfile profile = response.jsonPath().getObject("data", CandidateProfile.class);
			CandidateMatching_APIFlow.parsedCandidateProfile = profile;

			Assert.assertEquals(statusCode, 200, "Unexpected status code.");
			ExtentReportManager.log(Status.PASS, "Test passed with status code 200");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception during test: " + e.getMessage());
			ExtentReportManager.log(Status.FAIL, "Test failed with exception: " + e.getMessage());
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
