package com.Xooper.AIAPP;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.DataProvider;

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

			ExtentReportManager.log(Status.INFO, "Entering job title");
			setText(pom.getAijdpage().getJobTitleField(), jobtitle);

			// setText(pom.getAijdpage().getSkillField(), skills);

			ExtentReportManager.log(Status.INFO, "Selecting skills");
			selectFromDropdown(pom.getAijdpage().getSkillField(), skills, DROPDOWN_TYPE);

			// Define values to select
			Set<String> valuesToSelect = new HashSet<>();
			valuesToSelect.add("option1");
			valuesToSelect.add("option2");

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
			clickElement(pom.getAijdpage().getPostField());

			ExtentReportManager.log(Status.INFO, "Editing the Job Description");
			clickElement(pom.getAijdpage().getEditOption());

			ExtentReportManager.log(Status.INFO, "Submit to generate the Job Description");
			clickElement(pom.getAijdpage().getSubmittoCreateJD());

		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException("AI JD Creation test failed.", e);
		}
	}

	@DataProvider(name = "AIJDCreationData")
	public Object[][] getData() {

		ExtentReportManager.log(Status.INFO, "Passing different set of Ai JD creation data with excel");
		return ExcelUtils.getTestData("AIJDexcelfilepath", "sheet2");
	}

}
