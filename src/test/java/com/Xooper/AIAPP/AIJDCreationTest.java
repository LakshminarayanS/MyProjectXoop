package com.Xooper.AIAPP;

import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.Baseclass.com.BaseClass;
import com.PageObjectManager.com.PageObjectManager;
import com.aventstack.extentreports.Status;
import com.utils.ExcelUtils;
import com.utils.ExtentReportManager;

public class AIJDCreationTest extends BaseClass {

	private static PageObjectManager pom;

	public static void testAIJDCreation(String jobtitle, String skills, String joblocation, String jobtype,
			String employementtype, String experiencelevel, String salarylevel, String languagepreference,
			String tone) {

		try {

			pom = new PageObjectManager(driver);
			
			SoftAssert softAssert = new SoftAssert();

			ExtentReportManager.log(Status.INFO, "Entering job title");
			setText(pom.getAijdpage().getJobTitleField(), jobtitle);

			// setText(pom.getAijdpage().getSkillField(), skills);

			ExtentReportManager.log(Status.INFO, "Selecting skills");
			selectFromDropdown(pom.getAijdpage().getSkillField(), skills, DROPDOWN_TYPE);

			Set<String> availableOptions = new HashSet<>();
			for (WebElement element : pom.getAijdpage().getSkillList()) {
				String text = element.getText();
				if (text != null && !text.trim().isEmpty()) { // Filter out null or empty strings
					availableOptions.add(text.trim());
				}
			}

			// Define values to select
			Set<String> valuesToSelect = new HashSet<>();
			valuesToSelect.add("option1");
			valuesToSelect.add("option2");
			valuesToSelect.add("option3");

			valuesToSelect.retainAll(availableOptions); // Select only valid options

			ExtentReportManager.log(Status.INFO, "Selecting skills");
			handleSpecificCheckBoxes(pom.getAijdpage().getSkillList(), valuesToSelect, true);

			ExtentReportManager.log(Status.INFO, "Entering job location");
			setText(pom.getAijdpage().getLocationField(), joblocation);

			// setText(pom.getAijdpage().getJobTypeField(), jobtype);

			ExtentReportManager.log(Status.INFO, "Selecting job type");
			selectFromDropdown(pom.getAijdpage().getJobTypeField(), jobtype, DROPDOWN_TYPE);

			// setText(pom.getAijdpage().getEmploymentTypeField(), employementtype);

			ExtentReportManager.log(Status.INFO, "Selecting employment type");
			selectFromDropdown(pom.getAijdpage().getEmploymentTypeField(), employementtype, DROPDOWN_TYPE);

			ExtentReportManager.log(Status.INFO, "Entering experience level");
			setText(pom.getAijdpage().getExperienceLevelField(), experiencelevel);

			ExtentReportManager.log(Status.INFO, "Entering salary level");
			setText(pom.getAijdpage().getSalaryRangeField(), salarylevel);

			ExtentReportManager.log(Status.INFO, "Selecting language preference");
			selectFromDropdown(pom.getAijdpage().getLanguagePreferenceField(), languagepreference, DROPDOWN_TYPE);

			ExtentReportManager.log(Status.INFO, "Selecting tone");
			selectFromDropdown(pom.getAijdpage().getToneField(), tone, DROPDOWN_TYPE);

			ExtentReportManager.log(Status.INFO, "Clicking Post");
			waitForElementToBeClickable(pom.getAijdpage().getPostField());
			clickElement(pom.getAijdpage().getPostField());

			ExtentReportManager.log(Status.INFO, "Editing the Job Description");
			waitForElementToBeClickable(pom.getAijdpage().getEditOption());
			clickElement(pom.getAijdpage().getEditOption());

			ExtentReportManager.log(Status.INFO, "Submitting to generate Job Description");
			waitForElementToBeClickable(pom.getAijdpage().getSubmittoCreateJD());
			clickElement(pom.getAijdpage().getSubmittoCreateJD());

			String confirmationText = getTextFromElement(pom.getAijdpage().getSuccessMessage());

			softAssert.assertTrue(confirmationText.contains("Job Description Created"),
					"AI JD Creation failed: Confirmation message mismatch.");

		} catch (Exception e) {

			ExtentReportManager.log(Status.FAIL, "AI JD Creation test failed due to: " + e.getMessage());
			throw new RuntimeException("AI JD Creation test failed.", e);
		}
	}

	@DataProvider(name = "AIJDCreationData")
	public Object[][] getData() {

		ExtentReportManager.log(Status.INFO, "Fetching AI JD creation data from Excel");

		Object[][] testData = ExcelUtils.getTestData("AIJDexcelfilepath", "sheet2");

		if (testData == null || testData.length == 0) {
			ExtentReportManager.log(Status.WARNING, "No data found in Excel for AI JD creation.");
			throw new RuntimeException("Test data is empty or could not be retrieved.");
		}

		return testData;
	}
}
