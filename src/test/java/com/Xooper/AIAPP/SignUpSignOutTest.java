package com.Xooper.AIAPP;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

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

			if (isElementDisplayed(pom.getSignpage().getOTPResend())) {
				ExtentReportManager.log(Status.INFO, "Resending OTP");
				clickElement(pom.getSignpage().getOTPResend());

				ExtentReportManager.log(Status.INFO, "Waiting for new OTP...");
				waitForElementVisibility(pom.getSignpage().getOTPResend());

				ExtentReportManager.log(Status.INFO, "Entering new OTP");
				setText(pom.getSignpage().getEnterOTP(), OTPvalue);

				ExtentReportManager.log(Status.INFO, "Verifying new OTP");
				clickElement(pom.getSignpage().getOTPVerify());
			}

			ExtentReportManager.log(Status.INFO, "Clicking forgot password");
			clickElement(pom.getSignpage().getForgotPassword());

			ExtentReportManager.log(Status.INFO, "Entering email ID: " + email);
			setText(pom.getSignpage().getEnterEmailID(), email);

			ExtentReportManager.log(Status.INFO, "Sending reset password link");
			clickElement(pom.getSignpage().getSendPasswordResetLink());

			ExtentReportManager.log(Status.INFO, "Clicking sign-out button");
			clickElement(pom.getSignpage().getSignOut());

		} catch (Exception e) {

			ExtentReportManager.log(Status.FAIL, "Sign-up and sign-out test failed due to an error: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("SignUp and SignOut test failed.", e);
		}
	}

	public static String fetchLatestOTP() {

		String host = "imap.gmail.com"; // Update for your email provider
		String username = "your-email@gmail.com";
		String password = "your-email-password"; // Use App Password if 2FA is enabled

		try {
			Properties properties = new Properties();
			properties.put("mail.store.protocol", "imaps");
			properties.put("mail.imaps.host", host);
			properties.put("mail.imaps.port", "993");

			Session session = Session.getDefaultInstance(properties);
			Store store = session.getStore("imaps");
			store.connect(username, password);

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Search for unread emails with OTP
			javax.mail.Message[] messages = inbox.getMessages();
			for (int i = messages.length - 1; i >= 0; i--) { // Start from latest email
				// String subject = messages[i].getSubject();
				String content = messages[i].getContent().toString();

				// Regex to extract OTP (assumes 6-digit OTP)
				Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
				Matcher matcher = pattern.matcher(content);

				if (matcher.find()) {
					return matcher.group(0);
				}
			}

			inbox.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch OTP from email.", e);
		}
		return null;
	}

}
