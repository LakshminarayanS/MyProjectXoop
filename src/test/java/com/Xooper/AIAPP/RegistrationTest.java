package com.Xooper.AIAPP;

import org.testng.annotations.DataProvider;

import com.Baseclass.com.BaseClass;
import com.PageObjectManager.com.PageObjectManager;
import com.aventstack.extentreports.Status;
import com.utils.ExcelUtils;
import com.utils.ExtentReportManager;

public class RegistrationTest extends BaseClass {

	private static PageObjectManager pom;

	public static void testRegistration(String fullname, String email, String companyname, String phone,
			String password, String confirmpassword) {

		try {
			pom = new PageObjectManager(driver);

			ExtentReportManager.log(Status.INFO, "Starting registration test");

			waitForElementVisibility(pom.getRegpage().getFullNameField());
			setText(pom.getRegpage().getFullNameField(), fullname);
			ExtentReportManager.log(Status.PASS, "Fullname entered successfully: " + fullname);

			waitForElementVisibility(pom.getRegpage().getEmailField());
			setText(pom.getRegpage().getEmailField(), email);
			ExtentReportManager.log(Status.PASS, "Email entered successfully: " + email);

			waitForElementVisibility(pom.getRegpage().getCompanyNameField());
			setText(pom.getRegpage().getCompanyNameField(), companyname);
			ExtentReportManager.log(Status.PASS, "Company name entered successfully: " + companyname);

			waitForElementVisibility(pom.getRegpage().getPhoneNoField());
			setText(pom.getRegpage().getPhoneNoField(), phone);
			ExtentReportManager.log(Status.PASS, "Phone number entered successfully: " + phone);

			waitForElementVisibility(pom.getRegpage().getPasswordField());
			setText(pom.getRegpage().getPasswordField(), password);
			ExtentReportManager.log(Status.PASS, "Password entered successfully");

			waitForElementVisibility(pom.getRegpage().getConfirmPasswordField());
			setText(pom.getRegpage().getConfirmPasswordField(), confirmpassword);
			ExtentReportManager.log(Status.PASS, "Confirm password entered successfully");

			waitForElementToBeClickable(pom.getRegpage().getSubmitButton());
			clickElement(pom.getRegpage().getSubmitButton());
			ExtentReportManager.log(Status.INFO, "Clicked on registration submit button");

			waitForElementVisibility(pom.getRegpage().getSuccessMessage());
			String successMessage = getTextFromElement(pom.getRegpage().getSuccessMessage());

			if (successMessage.contains("Registration successful")) {
				ExtentReportManager.log(Status.PASS, "Registration completed successfully.");
			} else {
				ExtentReportManager.log(Status.FAIL, "Registration failed. Message displayed: " + successMessage);
				throw new RuntimeException("Registration failed. Expected success message not found.");
			}

		} catch (Exception e) {

			ExtentReportManager.log(Status.FAIL, "Registration test failed: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Registration test failed.", e);
		}
	}

	@DataProvider(name = "registrationData")
	public Object[][] getData() {

		ExtentReportManager.log(Status.INFO, "Fetching registration test data from Excel");

		String excelFilePath = "registrationexcelfilepath";
		String sheetName = "sheet1";

		Object[][] testData = null;

		try {
			testData = ExcelUtils.getTestData(excelFilePath, sheetName);

			if (testData == null || testData.length == 0) {
				ExtentReportManager.log(Status.FAIL,
						"No test data found in Excel: " + excelFilePath + ", Sheet: " + sheetName);
				throw new RuntimeException("Excel data is empty or not found.");
			}

			ExtentReportManager.log(Status.PASS,
					"Successfully fetched " + testData.length + " sets of registration data from Excel");

		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL, "Error fetching test data from Excel: " + e.getMessage());
			throw new RuntimeException("Failed to fetch registration data.", e);
		}

		return testData;
	}

}
