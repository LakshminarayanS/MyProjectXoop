package com.PageObjectModel.com;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AI_JDCreator_Page {

	private WebDriver driver;

	@FindBy(xpath = "")
	private WebElement jobTitleField;

	@FindBy(xpath = "")
	private WebElement skillField;

	@FindBys({ @FindBy(xpath = "input") })
	private List<WebElement> skillList;

	@FindBy(xpath = "")
	private WebElement locationField;

	@FindBy(xpath = "")
	private WebElement jobTypeField;

	@FindBy(xpath = "")
	private WebElement employmentTypeField;

	@FindBy(xpath = "")
	private WebElement experienceLevelField;

	@FindBy(xpath = "")
	private WebElement salaryRangeField;

	@FindBy(xpath = "")
	private WebElement languagePreferenceField;

	@FindBy(xpath = "")
	private WebElement postField;

	@FindBy(xpath = "")
	private WebElement editOption;

	@FindBy(xpath = "")
	private WebElement toneField;

	@FindBy(xpath = "")
	private WebElement submittoCreateJD;

	public WebElement getJobTitleField() {
		return jobTitleField;
	}

	public WebElement getSkillField() {
		return skillField;
	}

	public List<WebElement> getSkillList() {
		return skillList;
	}

	public WebElement getLocationField() {
		return locationField;
	}

	public WebElement getJobTypeField() {
		return jobTypeField;
	}

	public WebElement getEmploymentTypeField() {
		return employmentTypeField;
	}

	public WebElement getExperienceLevelField() {
		return experienceLevelField;
	}

	public WebElement getSalaryRangeField() {
		return salaryRangeField;
	}

	public WebElement getLanguagePreferenceField() {
		return languagePreferenceField;
	}

	public WebElement getToneField() {
		return toneField;
	}

	public WebElement getPostField() {
		return postField;
	}

	public WebElement getEditOption() {
		return editOption;
	}

	public WebElement getSubmittoCreateJD() {
		return submittoCreateJD;
	}

	public WebElement getSuccessMessage() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='success-message']")));
	}

	public AI_JDCreator_Page(WebDriver jd) {

		driver = jd;
		PageFactory.initElements(driver, jd);
	}
}
