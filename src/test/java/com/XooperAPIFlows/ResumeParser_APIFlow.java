package com.XooperAPIFlows;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.POJO_CandidateMatching.com.CandidateProfile;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;
import com.utils.MongoDBUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ResumeParser_APIFlow {

	private static final String ResumeParser_BASE_URL = "https://dev.xooper.in/resume_parser/resume-extraction";
	private static final String DB_NAME = "resume_parser";
	private static final String COLLECTION_NAME = "extracted_resumes";
	private static final String MONGO_URI = "mongodb+srv://xooper:lsBAmSmNcI0s7uUW@xoopercluster.alvrs.mongodb.net/?retryWrites=true&w=majority&appName=xoopercluster";

	@BeforeSuite
	public void setupReport() {
		ExtentReportManager.createInstance();
	}

	private String extractParserId(Response response) {
		return response.jsonPath().getString("data._id.$oid");
	}

	@Test
	public void resumeParseUploadFile() {

		try {

			MongoDBUtil.init(MONGO_URI, DB_NAME);

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

			String parserId = extractParserId(response);
			Assert.assertNotNull(parserId, "Parser ID should not be null in the response.");
			System.out.println("Expected Parser_id = " + parserId);

			Assert.assertEquals(statusCode, 200, "Response Status Code.");
			ExtentReportManager.log(Status.PASS, "Test passed with status code 200");

			Document doc = null;
			int retryCount = 0;

			while (doc == null && retryCount < 5) {
			    Thread.sleep(8000);
			    doc = MongoDBUtil.getDocumentById(COLLECTION_NAME, parserId);
			    retryCount++;
			}

			Assert.assertNotNull(doc, "Document not found in MongoDB after waiting.");

			System.out.println("Document doc " + doc);
			
			System.out.println("Retrying to fetch document from MongoDB for ID chck: " + parserId);

			Assert.assertNotNull(doc, "Candidate document not found in MongoDB for ID: " + parserId);
			ExtentReportManager.log(Status.PASS, "Document found in MongoDB for Parser_id: " + parserId);

			String actualId = extractParserId(response);
			System.out.println("Actual Parser_id = " + actualId);
			Assert.assertEquals(actualId, parserId, "Mismatch between expected and actual parser ID.");
			ExtentReportManager.log(Status.PASS, "MongoDB parser_id matches as expected: " + parserId);

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
