package com.PageObjectModel.com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Registration_Page {

	private WebDriver driver;

	@FindBy(xpath = "")
	private WebElement emailField;

	@FindBy(xpath = "")
	private WebElement fullNameField;

	@FindBy(xpath = "")
	private WebElement phoneNoField;

	@FindBy(xpath = "")
	private WebElement companyNameField;

	@FindBy(xpath = "")
	private WebElement passwordField;

	@FindBy(xpath = "")
	private WebElement confirmPasswordField;

	@FindBy(xpath = "")
	private WebElement submitButton;

	@FindBy(xpath = "")
	private WebElement successMessage;

	public WebElement getEmailField() {
		return emailField;
	}

	public WebElement getFullNameField() {
		return fullNameField;
	}

	public WebElement getPhoneNoField() {
		return phoneNoField;
	}

	public WebElement getCompanyNameField() {
		return companyNameField;
	}

	public WebElement getPasswordField() {
		return passwordField;
	}

	public WebElement getConfirmPasswordField() {
		return confirmPasswordField;
	}

	public WebElement getSubmitButton() {
		return submitButton;
	}

	public WebElement getSuccessMessage() {
		return successMessage;
	}

	public Registration_Page(WebDriver reg) {

		driver = reg;
		PageFactory.initElements(driver, this);
	}

}
