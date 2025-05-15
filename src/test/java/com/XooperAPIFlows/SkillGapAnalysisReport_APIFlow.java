package com.XooperAPIFlows;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;
import com.utils.MongoDBUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SkillGapAnalysisReport_APIFlow {

	private static final String SkillGapReport_BASE_URL = "https://dev.xooper.in/candidate_matching/evaluate_candidate";

	@BeforeSuite
	public void setupReport() {
		ExtentReportManager.createInstance();
	}

	@BeforeClass
	public void setup() {

		MongoDBUtil.init(
				"mongodb+srv://xooper:lsBAmSmNcI0s7uUW@xoopercluster.alvrs.mongodb.net/?retryWrites=true&w=majority&appName=xoopercluster",
				"recruitment_db");
	}

	@Test(dependsOnMethods = { "com.XooperAPIFlows.CandidateMatching_APIFlow.candidateMatching" })
	public void ReportGeneration() {

		try {

			ExtentReportManager.startTest("Skill Gap Analysis Report API Test Started");
			ExtentReportManager.log(Status.INFO, "Testing Skill Gap Analysis Report API");

			String jobId = AIJDCreation_APIFlow.JOB_ID;
			UUID candidateId = CandidateMatching_APIFlow.candidateID;

			Response response = (Response) given().baseUri(SkillGapReport_BASE_URL).pathParam("job_id", jobId)
					.pathParam("candidate_id", candidateId).contentType(ContentType.JSON)
					.get("/{job_id}/{candidate_id}");

			int statusCode = response.getStatusCode();
			String responseBody = response.getBody().asString();

			ExtentReportManager.log(Status.INFO, "Status Code: " + statusCode);
			ExtentReportManager.log(Status.INFO, "Response Body: " + responseBody);

			System.out.println("Status Code: " + statusCode);
			System.out.println("Response Body: " + responseBody);

			Assert.assertEquals(statusCode, 200, "Response Status Code.");
			ExtentReportManager.log(Status.PASS, "Test passed with status code 200");

			String collection = "candidate";

			Map<String, Object> fieldMap = new HashMap<>();
			fieldMap.put("job_id", AIJDCreation_APIFlow.JOB_ID);
			fieldMap.put("candidate_id", CandidateMatching_APIFlow.candidateID);

			Document doc = MongoDBUtil.getDocumentByFields(collection, fieldMap);

			System.out.println("MongoDB expected fielpMap : " + fieldMap);

			Assert.assertNotNull(doc, "Matching document not found");
			for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
				Assert.assertEquals(doc.get(entry.getKey()), entry.getValue(), "Mismatch for field: " + entry.getKey());
			}
			ExtentReportManager.log(Status.PASS, "Document found in MongoDB for candidate_id and job_id : " + fieldMap);

			Assert.assertEquals(doc.getString("job_id"), AIJDCreation_APIFlow.JOB_ID);
			Assert.assertEquals(doc.getString("candidate_id"), CandidateMatching_APIFlow.candidateID);
			ExtentReportManager.log(Status.PASS, "MongoDB candidate_id and job_id matches as expected: " + fieldMap);

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
