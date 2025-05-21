package com.XooperAPIFlows;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Baseclass.com.BaseClass;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;
import com.utils.MongoDBUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SkillGapAnalysisReport_APIFlow extends BaseClass {

	private static final String SkillGapReport_BASE_URL = "https://dev.xooper.in/candidate_matching/evaluate_candidate";
	private static final String DB_NAME = "recruitment_db";
	private static final String COLLECTION_NAME = "candidate_report";
	private static final String MONGO_URI = "mongodb+srv://xooper:lsBAmSmNcI0s7uUW@xoopercluster.alvrs.mongodb.net/?retryWrites=true&w=majority&appName=xoopercluster";

	@BeforeClass
	public void setup() {

		MongoDBUtil.init(MONGO_URI, DB_NAME);
	}

	@Test
	public void ReportGeneration() {

		try {

			ExtentReportManager.startTest("Skill Gap Analysis Report API Testing Started");
			ExtentReportManager.log(Status.INFO, "Testing Skill Gap Analysis Report API");

			Response response = (Response) given().baseUri(SkillGapReport_BASE_URL)
					.pathParam("job_id", AIJDCreation_APIFlow.JOB_ID)
					.pathParam("candidate_id", CandidateMatching_APIFlow.candidateID.toString())
					.contentType(ContentType.JSON).get("/{job_id}/{candidate_id}");

			int statusCode = response.getStatusCode();
			String responseBody = response.getBody().asString();

			ExtentReportManager.log(Status.INFO, "Response Status Code: " + statusCode);
			ExtentReportManager.log(Status.INFO, "Response Body: " + responseBody);

			System.out.println("Response Status Code: " + statusCode);
			System.out.println("Response Body: " + responseBody);

			Assert.assertEquals(statusCode, 200, "Response Status Code.");
			ExtentReportManager.log(Status.PASS, "Test passed with status code 200");

			Map<String, Object> fieldMap = new HashMap<>();
			fieldMap.put("job_id", AIJDCreation_APIFlow.JOB_ID);
			fieldMap.put("candidate_id", CandidateMatching_APIFlow.candidateID.toString());

			Document doc = null;
			int retryCount = 0;

			while (doc == null && retryCount < 5) {
				Thread.sleep(5000);
				doc = MongoDBUtil.getDocumentByFields(COLLECTION_NAME, fieldMap);
				retryCount++;
			}
			System.out.println("MongoDB expected fielpMap : " + fieldMap);

			Assert.assertNotNull(doc, "Matching document not found");
			for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
				Assert.assertEquals(doc.get(entry.getKey()), entry.getValue(), "Mismatch for field: " + entry.getKey());
			}
			ExtentReportManager.log(Status.PASS, "Document found in MongoDB for candidate_id and job_id : " + fieldMap);

			ExtentReportManager.log(Status.INFO,
					"Skill Gap Analysis Report test data successfully stored in candidate_report table: "
							+ doc.toJson());

			Assert.assertEquals(doc.getString("job_id"), AIJDCreation_APIFlow.JOB_ID);
			Assert.assertEquals(doc.getString("candidate_id"), CandidateMatching_APIFlow.candidateID.toString());
			ExtentReportManager.log(Status.PASS, "MongoDB candidate_id and job_id matches as expected: " + fieldMap);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception during test: " + e.getMessage());
			ExtentReportManager.log(Status.FAIL, "Test failed with exception: " + e.getMessage());
		}

	}

	@AfterClass
	public static void cleanup() {
		MongoDBUtil.close();
	}

}
