package com.XooperAPIFlows;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.POJO_CandidateMatching.com.CandidateProfile;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.ExtentReportManager;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CandidateMatching_APIFlow {

	private static final String CandidateMatch_BASE_URL = "https://dev.xooper.in/candidate_matching/candidates/";

	public static UUID candidateID;
	public static CandidateProfile parsedCandidateProfile;

	@BeforeSuite
	public void setupReport() {
		ExtentReportManager.createInstance();
	}

	@Test(dependsOnMethods = { "com.XooperAPIFlows.ResumeParser_APIFlow.ResumeParseUploadFile" })
	public void candidateMatching() {

		try {

			ExtentReportManager.startTest("Candidate Matching API Test Started");
			ExtentReportManager.log(Status.INFO, "Testing Candidate Matching API");

			CandidateProfile profile = parsedCandidateProfile;

			if (profile == null) {
				throw new IllegalStateException("CandidateProfile not set from ResumeParser_APIFlow");
			}

			UUID uuid = UUID.randomUUID();

			candidateID = uuid;

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

			ExtentReportManager.log(Status.INFO, "Status Code: " + statusCode);
			ExtentReportManager.log(Status.INFO, "Response Body: " + responseBody);

			System.out.println("Status Code: " + statusCode);
			System.out.println("Response Body: " + responseBody);

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
