package com.XooperAPITest;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class API_AIJDCreationTest {

	private static final String BASE_URL = "https://dev.xooper.in/create-job/";

	public static void testAIJDAPI(String role, String minExp, String maxExp, String tone, String language,
			String skills, String description, String industry, String location, String jobType,
			String employmentType) {

		int maxRetries = 5;
		int attempt = 0;
		boolean success = false;

		ExtentReportManager.log(Status.INFO, "Starting API test for role: " + role);

		String[] skillsArray = skills.split(",");
		JSONArray skillsJsonArray = new JSONArray();
		for (String skill : skillsArray) {
			skillsJsonArray.put(skill.trim());
		}

		JSONObject requestBody = new JSONObject();
		requestBody.put("role", role);
		requestBody.put("min_experience", minExp);
		requestBody.put("max_experience", maxExp);
		requestBody.put("tone", tone);
		requestBody.put("language", language);
		requestBody.put("skills", skillsJsonArray);
		requestBody.put("description", description);
		requestBody.put("industry", industry);
		requestBody.put("location", location);
		requestBody.put("job_type", jobType);
		requestBody.put("employment_type", employmentType);

		ExtentReportManager.log(Status.INFO, "Request Body: " + requestBody.toString());

//		RestAssured.baseURI = BASE_URL;

		while (attempt < maxRetries) {
			Response response = given().baseUri(BASE_URL)

					.contentType(ContentType.JSON).body(requestBody.toString()).when().post();

			int statusCode = response.getStatusCode();
			System.out.println("Attempt " + (attempt + 1) + ": Status Code = " + statusCode);

			if (statusCode == 200) {
				success = true;
				if (response.getBody() != null) {
					String responseBody = response.getBody().asString();
					ExtentReportManager.log(Status.INFO, "Response Status Code: " + statusCode);
					ExtentReportManager.log(Status.INFO, "Response Body: " + responseBody);
					System.out.println("Response Body: " + responseBody);
				} else {
					ExtentReportManager.log(Status.WARNING, "Response body is null");
				}
				break;
			}

			attempt++;
			try {
				TimeUnit.SECONDS.sleep(7);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (!success) {
			ExtentReportManager.log(Status.FAIL, "API request failed after " + maxRetries + " attempts.");
			throw new AssertionError("API request failed after " + maxRetries + " attempts.");
		}
		// Assert.assertNotNull(response.getBody().asString());
		// Assert.assertTrue(response.getBody().asString().contains("success"));

		ExtentReportManager.log(Status.PASS, "API test passed for role: " + role);
	}

}
