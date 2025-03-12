package com.XooperAPITest;

import static io.restassured.RestAssured.given;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.Status;
import com.utils.ExcelUtils;
import com.utils.ExtentReportManager;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class API_AIJDCreationTest {

	private static final String BASE_URL = "https://api-url.com";

	public static void testAIJDAPI(String role, int minExp, int maxExp, String tone, String language, String skills,
			String description, String industry, String location, String jobType, String employmentType) {
		ExtentReportManager.log(Status.INFO, "Starting API test for role: " + role);

		String[] skillsArray = skills.split(",");

		JSONObject requestBody = new JSONObject();
		requestBody.put("role", role);
		requestBody.put("min_experience", minExp);
		requestBody.put("max_experience", maxExp);
		requestBody.put("tone", tone);
		requestBody.put("language", language);
		requestBody.put("skills", skillsArray);
		requestBody.put("description", description);
		requestBody.put("industry", industry);
		requestBody.put("location", location);
		requestBody.put("job_type", jobType);
		requestBody.put("employment_type", employmentType);

		ExtentReportManager.log(Status.INFO, "Request Body: " + requestBody.toString());

		RestAssured.baseURI = BASE_URL;

		Response response = given()

				.contentType(ContentType.JSON).body(requestBody.toString()).when().post().then().statusCode(200)
				.extract().response();

		String responseBody = response.getBody().asString();
		ExtentReportManager.log(Status.INFO, "Response Body: " + responseBody);

		Assert.assertNotNull(response.getBody().asString());
		Assert.assertTrue(response.getBody().asString().contains("success"));

		ExtentReportManager.log(Status.PASS, "API test passed for role: " + role);
	}

	@DataProvider(name = "APIAIJDCreationData")
	public Object[][] getData() {

		String excelFilePath = "https://tisteps-my.sharepoint.com/:x:/r/personal/lakshminarayanan_s_impacteers_com/_layouts/15/Doc.aspx?sourcedoc=%7BB6CEB13C-1F64-407B-A5E8-EB29893BD808%7D&file=TestData.xlsx&action=default&mobileredirect=true&DefaultItemOpen=1";
		String sheetName = "AIJDCreation";

		ExtentReportManager.log(Status.INFO, "Fetching AI JD creation data from Excel");

		Object[][] testData = ExcelUtils.getTestData(excelFilePath, sheetName);

		if (testData == null || testData.length == 0) {
			ExtentReportManager.log(Status.WARNING, "No data found in Excel for AI JD creation.");
			throw new RuntimeException("Test data is empty or could not be retrieved.");
		}

		return testData;
	}

}
