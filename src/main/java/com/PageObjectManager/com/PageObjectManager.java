package com.PageObjectManager.com;

import org.openqa.selenium.WebDriver;

import com.PageObjectModel.com.AI_JDCreator_Page;
import com.PageObjectModel.com.Registration_Page;
import com.PageObjectModel.com.SignUpandSignOut_Page;
import com.aventstack.extentreports.Status;
import com.utils.ExtentReportManager;

public class PageObjectManager {

	private WebDriver driver;

	private Registration_Page regpage;

	private SignUpandSignOut_Page signpage;

	private AI_JDCreator_Page aijdpage;

	public PageObjectManager(WebDriver driver) {

		this.driver = driver;
		ExtentReportManager.log(Status.INFO, "Initializing PageObjectManager with WebDriver instance.");
	}

	public Registration_Page getRegpage() {

		if (regpage == null) {
			ExtentReportManager.log(Status.INFO, "Initializing Registration Page Object.");
			regpage = new Registration_Page(driver);
		}
		return regpage;

	}

	public SignUpandSignOut_Page getSignpage() {

		if (signpage == null) {
			ExtentReportManager.log(Status.INFO, "Initializing SignUp and SignOut Page Object.");
			signpage = new SignUpandSignOut_Page(driver);
		}
		return signpage;
	}

	public AI_JDCreator_Page getAijdpage() {

		if (aijdpage == null) {
			ExtentReportManager.log(Status.INFO, "Initializing AI JD Creator Page Object.");
			aijdpage = new AI_JDCreator_Page(driver);
		}
		return aijdpage;
	}

}
