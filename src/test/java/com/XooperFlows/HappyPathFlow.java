package com.XooperFlows;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Baseclass.com.BaseClass;
import com.Xooper.AIAPP.AIJDCreationTest;
import com.Xooper.AIAPP.RegistrationTest;
import com.Xooper.AIAPP.SignUpSignOutTest;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;

public class HappyPathFlow extends BaseClass {

	@BeforeClass
	public void setup(ITestResult result) {

		try {
			ExtentReportManager.startTest(result.getMethod().getMethodName());
			getDriver(BROWSER_NAME);
			ExtentReportManager.log(Status.INFO, "Navigating to Xooper");
			navigateGetUrl(BASE_URL);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Setup failed.", e);
		}
	}

	@Test(priority = 1)
	public void testRegistrationData() {

		try {

			RegistrationTest.testRegistration(FULL_NAME, EMAIL_ID, COMPANY_NAME, PHONE_NO, PASS_WORD, CONFIRM_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("User registration test failed.", e);
		}

	}

	@Test(dataProvider = "registrationData", priority = 1)
	public void testRegistrationWithExcelData(String fullname, String email, String companyname, String phone,
			String password, String confirmpassword) {

		try {

			RegistrationTest.testRegistration(fullname, email, companyname, phone, password, confirmpassword);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("User registration test failed with Excel Data.", e);
		}
	}

	@Test(priority = 2)
	public void testUserSignUpSignOut() {

		try {

			SignUpSignOutTest.testSignUpSignOut(OTP, EMAIL_ID);

			ExtentReportManager.pass("Login successful");

		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException("User SignUp and SignOut test failed.", e);
		}
	}

	@Test(priority = 3)
	public void testAIJDCreation() {

		try {

			AIJDCreationTest.testAIJDCreation(JOB_TITLE, JOB_SKILLS, JOB_LOCATION, JOB_TYPE, EMPLOYEMENT_TYPE,
					EXPERIENCE_LEVEL, SALARY_LEVEL, LANGUAGE_PREFERENCE, TONE);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("AI JD Creation test failed.", e);
		}
	}

	@Test(dataProvider = "AIJDCreationData", priority = 3)
	public void testAIJDCreationWithExcelData(String jobtitle, String skills, String joblocation, String jobtype,
			String employementtype, String experiencelevel, String salarylevel, String languagepreference,
			String tone) {

		try {

			AIJDCreationTest.testAIJDCreation(jobtitle, skills, joblocation, jobtype, employementtype, experiencelevel,
					salarylevel, languagepreference, tone);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("AI JD Creation test failed with Excel Data.", e);
		}
	}

	@AfterClass
	public void teardown(ITestResult result) {

		try {
			closeCurrentBrowser(result, SCREENSHOT_DIR);
			ExtentReportManager.endTest();

		} catch (Exception e) {
			System.err.println("Error during teardown: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
