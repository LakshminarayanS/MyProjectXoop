package com.XooperAPIFlows;

import static io.restassured.RestAssured.given;

import org.bson.Document;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Baseclass.com.BaseClass;
import com.aventstack.extentreports.Status;
import com.constants.com.FrameworkConstants;
import com.utils.ExtentReportManager;
import com.utils.MongoDBUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AIJDCreation_APIFlow extends BaseClass {

	private static final String JDCreator_BASE_URL = "https://dev.xooper.in/creator/create-job/";
	private static final String DB_NAME = "recruitment_db";
	private static final String COLLECTION_NAME = "job_posting";
	private static final String MONGO_URI = "mongodb+srv://xooper:lsBAmSmNcI0s7uUW@xoopercluster.alvrs.mongodb.net/?retryWrites=true&w=majority&appName=xoopercluster";
	public static String JOB_ID;

	@BeforeClass
	public void setup() {

		MongoDBUtil.init(MONGO_URI, DB_NAME);
	}

	@Test
	public static void AIJDCreatorTestData() {

		try {

			ExtentReportManager.startTest("AI JD Creation API Test with Valid Data - " + FrameworkConstants.JOB_ROLE);
			ExtentReportManager.log(Status.INFO,
					"Testing AI JD Creation API with valid data for " + FrameworkConstants.JOB_ROLE);

			ExtentReportManager.log(Status.INFO, "JD Creator API starting test for: " + FrameworkConstants.JOB_ROLE);

			JSONObject requestBody = new JSONObject();
			requestBody.put("role", FrameworkConstants.JOB_ROLE);
			requestBody.put("min_experience", FrameworkConstants.MIN_EXPERIENCE);
			requestBody.put("max_experience", FrameworkConstants.MAX_EXPERIENCE);
			requestBody.put("tone", FrameworkConstants.TONE);
			requestBody.put("language", FrameworkConstants.LANGUAGE_PREFERENCE);
			requestBody.put("skills", FrameworkConstants.JOB_SKILLS);
			requestBody.put("description", FrameworkConstants.JOB_DESCRIPTION);
			requestBody.put("industry", FrameworkConstants.INDUSTRY_TYPE);
			requestBody.put("location", FrameworkConstants.JOB_LOCATION);
			requestBody.put("job_type", FrameworkConstants.JOB_TYPE);
			requestBody.put("employment_type", FrameworkConstants.EMPLOYEMENT_TYPE);

			ExtentReportManager.log(Status.INFO, "Request Body: " + requestBody.toString());

			Response response = given().baseUri(JDCreator_BASE_URL).contentType(ContentType.JSON)
					.body(requestBody.toString()).when().post();

			int statusCode = response.getStatusCode();

			String responseBody = response.getBody().asString();
			ExtentReportManager.log(Status.INFO, "Response Status Code: " + statusCode);
			ExtentReportManager.log(Status.INFO, "Response Body: " + responseBody);

			System.out.println("AI JD Creator Response Status Code: " + statusCode);
			System.out.println("Response Body: " + responseBody);

			String jobId = response.jsonPath().getString("job_id");
			System.out.println("Extracted job_id = " + jobId);

			JOB_ID = jobId;

			JSONObject jsonResponse = new JSONObject(responseBody);
			Assert.assertTrue(jsonResponse.has("retrieved_results"),
					"Response does not contain expected 'retrieved_results'");
			ExtentReportManager.log(Status.PASS, "'retrieved_results' found in response");

			String collection = COLLECTION_NAME;
			String fieldName = "job_id";
			String fieldValue = jobId;

			Document doc = MongoDBUtil.getDocumentByField(collection, fieldName, fieldValue);

			Assert.assertNotNull(doc, "Candidate document should not be null");
			ExtentReportManager.log(Status.PASS, "Document found in MongoDB for job_id: " + jobId);

			Assert.assertEquals(doc.getString("job_id"), jobId);
			ExtentReportManager.log(Status.PASS, "MongoDB job_id matches as expected: " + jobId);

			ExtentReportManager.log(Status.INFO,
					"Ai JD Creator test data successfully stored in job_posting table: " + doc.toJson());

			ExtentReportManager.log(Status.PASS, "API test passed for: " + FrameworkConstants.JOB_ROLE);

		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL, "AI JD Creation test failed for valid data: "
					+ FrameworkConstants.JOB_ROLE + ". Error: " + e.getMessage());
			throw new RuntimeException("Valid AIJD test failed", e);
		}

	}

	@AfterClass
	public static void cleanup() {
		MongoDBUtil.close();
	}

}
