package com.PageObjectModel.com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpandSignOut_Page {

	private WebDriver driver;

	@FindBy(xpath = "")
	private WebElement OTPGenerate;

	@FindBy(xpath = "")
	private WebElement EnterOTP;

	@FindBy(xpath = "")
	private WebElement OTPVerify;

	@FindBy(xpath = "")
	private WebElement OTPResend;

	@FindBy(xpath = "")
	private WebElement ForgotPassword;

	@FindBy(xpath = "")
	private WebElement EnterEmailID;

	@FindBy(xpath = "")
	private WebElement SendPasswordResetLink;

	@FindBy(xpath = "")
	private WebElement SignOut;

	public WebElement getOTPGenerate() {
		return OTPGenerate;
	}

	public WebElement getEnterOTP() {
		return EnterOTP;
	}

	public WebElement getOTPVerify() {
		return OTPVerify;
	}

	public WebElement getOTPResend() {
		return OTPResend;
	}

	public WebElement getForgotPassword() {
		return ForgotPassword;
	}

	public WebElement getEnterEmailID() {
		return EnterEmailID;
	}

	public WebElement getSendPasswordResetLink() {
		return SendPasswordResetLink;
	}

	public WebElement getSignOut() {
		return SignOut;
	}

	public SignUpandSignOut_Page(WebDriver sign) {

		driver = sign;
		PageFactory.initElements(driver, this);

	}
}
