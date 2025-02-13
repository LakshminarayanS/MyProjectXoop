package com.XooperFlows;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Baseclass.com.BaseClass;
import com.Xooper.AIAPP.AIJDCreationTest;
import com.Xooper.AIAPP.RegistrationTest;
import com.Xooper.AIAPP.SignUpSignOutTest;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;

public class NegativeTestFlow extends BaseClass {

	@BeforeClass
	public void setup(ITestResult result) {

		try {
			ExtentReportManager.startTest(result.getMethod().getMethodName());
			ExtentReportManager.log(Status.INFO, "Test execution started: " + result.getMethod().getMethodName());

			getDriver(BROWSER_NAME);
			ExtentReportManager.log(Status.INFO, "Browser launched: " + BROWSER_NAME);

			ExtentReportManager.log(Status.INFO, "Navigating to URL: " + BASE_URL);
			navigateGetUrl(BASE_URL);

		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL, "Setup failed: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Setup failed.", e);
		}
	}

	@Test(priority = 1)
	public void testRegistrationData() {

		try {
			ExtentReportManager.startTest("User Registration Test");
			ExtentReportManager.log(Status.INFO, "Starting user registration test");

			RegistrationTest.testRegistration("LN", "lak%$(.in@gov.com", "420", "", "Password", "Password0");

			ExtentReportManager.log(Status.PASS, "User registration test completed successfully");
		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL, "User registration test failed: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("User registration test failed.", e);
		}
	}

	@Test(dataProvider = "registrationData", priority = 1)
	public void testRegistrationWithExcelData(String fullname, String email, String companyname, String phone,
			String password, String confirmpassword) {

		try {
			ExtentReportManager.startTest("User Registration Test with Excel Data");
			RegistrationTest.testRegistration(fullname, email, companyname, phone, password, confirmpassword);
			ExtentReportManager.log(Status.PASS, "User registration test passed for email: " + email);
		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL,
					"User registration test failed for email: " + email + ". Error: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("User registration test failed with Excel Data.", e);
		}
	}

	@Test(priority = 1, enabled = false)
	public void loginTest_Skip() {

		ExtentReportManager.log(Status.INFO, "Starting skipped login test");

		ExtentReportManager.logSkip("Skipping this test due to dependency issue");
		throw new SkipException("Skipping test...");
	}

	@Test(priority = 2)
	public void testUserSignUpSignOut() {

		try {
			ExtentReportManager.startTest("User SignUp and SignOut Test");
			ExtentReportManager.log(Status.INFO, "Starting user sign-up and sign-out test for email: " + EMAIL_ID);

			SignUpSignOutTest.testSignUpSignOut("000000", "lak%$(.in@gov.com^");

			ExtentReportManager.pass("Login successful");
			ExtentReportManager.log(Status.PASS, "User sign-up and sign-out test passed for email: " + EMAIL_ID);
		} catch (Exception e) {

			ExtentReportManager.log(Status.FAIL,
					"User sign-up and sign-out test failed for email: " + EMAIL_ID + ". Error: " + e.getMessage());

			e.printStackTrace();
			throw new RuntimeException("User SignUp and SignOut test failed.", e);
		}
	}

	@Test(priority = 3)
	public void testAIJDCreation() {

		try {
			ExtentReportManager.startTest("AI JD Creation Test");
			ExtentReportManager.log(Status.INFO, "Starting AI JD Creation test with Job Title: " + JOB_TITLE);

			AIJDCreationTest.testAIJDCreation("Dev", "#", "Home", "", "2 hours", "Fresher", "9LPA", "Tamil", "");

			ExtentReportManager.log(Status.PASS, "AI JD Creation test passed successfully for Job Title: " + JOB_TITLE);
		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL,
					"AI JD Creation test failed for Job Title: " + JOB_TITLE + ". Error: " + e.getMessage());

			e.printStackTrace();
			throw new RuntimeException("AI JD Creation test failed.", e);
		}
	}

	@Test(dataProvider = "AIJDCreationData", priority = 3)
	public void testAIJDCreationWithExcelData(String jobtitle, String skills, String joblocation, String jobtype,
			String employementtype, String experiencelevel, String salarylevel, String languagepreference,
			String tone) {

		try {
			ExtentReportManager.startTest("AI JD Creation Test - " + jobtitle);
			ExtentReportManager.log(Status.INFO, "Starting AI JD Creation test with Job Title: " + jobtitle);

			AIJDCreationTest.testAIJDCreation(jobtitle, skills, joblocation, jobtype, employementtype, experiencelevel,
					salarylevel, languagepreference, tone);
			ExtentReportManager.log(Status.PASS, "AI JD Creation test passed successfully for Job Title: " + jobtitle);
		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL,
					"AI JD Creation test failed for Job Title: " + jobtitle + ". Error: " + e.getMessage());

			e.printStackTrace();
			throw new RuntimeException("AI JD Creation test failed with Excel Data.", e);
		}
	}

	@AfterClass
	public void teardown(ITestResult result) {

		try {
			if (result.getStatus() == ITestResult.FAILURE) {

				String screenshotPath = SCREENSHOT_DIR + "/" + result.getName() + ".png";
				ExtentReportManager.log(Status.FAIL, "Test failed: " + result.getName());
				ExtentReportManager.log(Status.FAIL, "Error: " + result.getThrowable().getMessage());
				ExtentReportManager.test.addScreenCaptureFromPath(screenshotPath);
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				ExtentReportManager.log(Status.PASS, "Test passed: " + result.getName());
			} else if (result.getStatus() == ITestResult.SKIP) {
				ExtentReportManager.log(Status.SKIP, "Test skipped: " + result.getName());
			}
			closeCurrentBrowser(result, SCREENSHOT_DIR);
			ExtentReportManager.endTest();

		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL, "Error during teardown: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
