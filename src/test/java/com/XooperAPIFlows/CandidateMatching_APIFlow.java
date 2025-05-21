package com.XooperAPIFlows;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Baseclass.com.BaseClass;
import com.POJO_CandidateMatching.com.CandidateProfile;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.ExtentReportManager;
import com.utils.MongoDBUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CandidateMatching_APIFlow extends BaseClass {

	private static final String CandidateMatch_BASE_URL = "https://dev.xooper.in/candidate_matching/candidates/";
	private static final String DB_NAME = "recruitment_db";
	private static final String COLLECTION_NAME = "candidate";
	private static final String MONGO_URI = "mongodb+srv://xooper:lsBAmSmNcI0s7uUW@xoopercluster.alvrs.mongodb.net/?retryWrites=true&w=majority&appName=xoopercluster";

	public static UUID candidateID;
	public static CandidateProfile parsedCandidateProfile;

	@BeforeClass
	public void setup() {

		MongoDBUtil.init(MONGO_URI, DB_NAME);
	}

	@Test
	public void candidateMatching() {

		try {

			ExtentReportManager.startTest("Candidate Matching API Testing Started");
			ExtentReportManager.log(Status.INFO, "Testing Candidate Matching API");

			CandidateProfile profile = parsedCandidateProfile;

			if (profile == null) {
				throw new IllegalStateException("CandidateProfile not set from ResumeParser_APIFlow");
			}

			UUID uuid = UUID.randomUUID();

			candidateID = uuid;

			System.out.println("Expected Candidate_ID : " + candidateID);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> candidateMatchPayload = new HashMap<>();

			candidateMatchPayload.put("candidate_id", candidateID.toString());
			candidateMatchPayload.put("job_id", AIJDCreation_APIFlow.JOB_ID);
			candidateMatchPayload.put("candidate_information", profile.getCandidate_information());
			candidateMatchPayload.put("professional_summary", profile.getProfessional_summary());
			candidateMatchPayload.put("education", profile.getEducation());
			candidateMatchPayload.put("skills", profile.getSkills());
			candidateMatchPayload.put("interpersonal_strengths", profile.getInterpersonal_strengths());
			candidateMatchPayload.put("professional_experience", profile.getProfessional_experience());
			candidateMatchPayload.put("certifications", profile.getCertifications());
			candidateMatchPayload.put("projects", profile.getProjects());
			candidateMatchPayload.put("achievements", profile.getAchievements());

			String jsonBody = mapper.writeValueAsString(candidateMatchPayload);

			ExtentReportManager.log(Status.INFO, "Request Body: " + candidateMatchPayload.toString());

			Response response = given().baseUri(CandidateMatch_BASE_URL).contentType(ContentType.JSON).body(jsonBody)
					.post();

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
			fieldMap.put("candidate_id", candidateID.toString());

			Document doc = null;
			int retryCount = 0;

			while (doc == null && retryCount < 5) {
				Thread.sleep(5000);
				doc = MongoDBUtil.getDocumentByFields(COLLECTION_NAME, fieldMap);
				retryCount++;
			}
			System.out.println("MongoDB expected fieldMap : " + fieldMap);

			Assert.assertNotNull(doc, "Matching document not found");
			System.out.println("Document found: " + doc.toJson());
			for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
				Assert.assertEquals(doc.get(entry.getKey()), entry.getValue(), "Mismatch for field: " + entry.getKey());
			}
			ExtentReportManager.log(Status.PASS, "Document found in MongoDB for candidate_id and job_id : " + fieldMap);

			ExtentReportManager.log(Status.INFO,
					"Candidate Matching test data successfully stored in candidate table: " + doc.toJson());

			Assert.assertEquals(doc.getString("job_id"), AIJDCreation_APIFlow.JOB_ID);
			Assert.assertEquals(doc.getString("candidate_id"), candidateID.toString());
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
