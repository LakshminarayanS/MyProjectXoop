package com.Xooper.AIAPP;

import org.testng.annotations.DataProvider;

import com.Baseclass.com.BaseClass;
import com.PageObjectManager.com.PageObjectManager;
import com.aventstack.extentreports.Status;
import com.utils.ExcelUtils;
import com.utils.ExtentReportManager;

import dev.failsafe.internal.util.Assert;

public class RegistrationTest extends BaseClass {

	private static PageObjectManager pom;

	public static void testRegistration(String fullname, String email, String companyname, String phone,
			String password, String confirmpassword) {

		try {
			pom = new PageObjectManager(driver);

			// WebElement emailid = pom.getRegpage().getEmailField();
			// setText(emailid, "lakshmi@xooper.com");

			ExtentReportManager.log(Status.INFO, "Entering fullname");
			setText(pom.getRegpage().getFullNameField(), fullname);

			ExtentReportManager.log(Status.INFO, "Entering email id");
			setText(pom.getRegpage().getEmailField(), email);

			ExtentReportManager.log(Status.INFO, "Entering company name");
			setText(pom.getRegpage().getCompanyNameField(), companyname);

			ExtentReportManager.log(Status.INFO, "Entering phone number");
			setText(pom.getRegpage().getPhoneNoField(), phone);

			ExtentReportManager.log(Status.INFO, "Entering password");
			setText(pom.getRegpage().getPasswordField(), password);

			ExtentReportManager.log(Status.INFO, "Entering confirmation password");
			setText(pom.getRegpage().getConfirmPasswordField(), confirmpassword);

			ExtentReportManager.log(Status.INFO, "Clicking registration submit button");
			clickElement(pom.getRegpage().getSubmitButton());

			ExtentReportManager.log(Status.INFO, "Verifying registration results");
			Assert.isTrue(getTextFromElement(pom.getRegpage().getSuccessMessage()).contains("Registration successful"),
					"Registration failed.");

		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException("Registration test failed.", e);
		}
	}

	@DataProvider(name = "registrationData")
	public Object[][] getData() {

		ExtentReportManager.log(Status.INFO, "Passing different set of registration data with excel");
		return ExcelUtils.getTestData("registrationexcelfilepath", "sheet1");
	}

}
