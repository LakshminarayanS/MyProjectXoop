package com.Xooper.AIAPP;

import com.Baseclass.com.BaseClass;
import com.PageObjectManager.com.PageObjectManager;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;

public class SignUpSignOutTest extends BaseClass {

	private static PageObjectManager pom;

	public static void testSignUpSignOut(String OTPvalue, String email) {

		try {

			ExtentReportManager.log(Status.INFO, "Genarating OTP");
			clickElement(pom.getSignpage().getOTPGenerate());

			ExtentReportManager.log(Status.INFO, "Entering received OTP");
			setText(pom.getSignpage().getEnterOTP(), OTPvalue);

			ExtentReportManager.log(Status.INFO, "Verifying entered OTP");
			clickElement(pom.getSignpage().getOTPVerify());

			ExtentReportManager.log(Status.INFO, "Resending OTP");
			clickElement(pom.getSignpage().getOTPResend());

			// Ensure we are referring same EnterOTP and OTPVerify XPath are different one.
			// If differ need handle accordingly.
			ExtentReportManager.log(Status.INFO, "Entering received OTP");
			setText(pom.getSignpage().getEnterOTP(), OTPvalue);

			ExtentReportManager.log(Status.INFO, "Verifying entered OTP");
			clickElement(pom.getSignpage().getOTPVerify());

			ExtentReportManager.log(Status.INFO, "Clicking forgot password");
			clickElement(pom.getSignpage().getForgotPassword());

			ExtentReportManager.log(Status.INFO, "Entering email id");
			setText(pom.getSignpage().getEnterEmailID(), email);

			ExtentReportManager.log(Status.INFO, "Sending reset password link");
			clickElement(pom.getSignpage().getSendPasswordResetLink());

			ExtentReportManager.log(Status.INFO, "Clicking sign-out button");
			clickElement(pom.getSignpage().getSignOut());

		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException("SignUp and SignOut test failed.", e);
		}
	}

}
