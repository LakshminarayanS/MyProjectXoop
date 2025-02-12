package com.constants.com;

import java.util.Properties;

import com.Config.com.ConfigLoader;

public class FrameworkConstants {

	public static final Properties properties = new Properties();

	public static final String BROWSER_NAME = ConfigLoader.getProperty("browser.name");

	public static final String BASE_URL = ConfigLoader.getProperty("base.url");

	public static final String DROPDOWN_TYPE = ConfigLoader.getProperty("dropdown.type");

	public static final String SCREENSHOT_DIR = ConfigLoader.getProperty("screenshot.dir");

	public static final String FULL_NAME = ConfigLoader.getProperty("full.name");

	public static final String EMAIL_ID = ConfigLoader.getProperty("email.id");

	public static final String COMPANY_NAME = ConfigLoader.getProperty("company.name");

	public static final String PHONE_NO = ConfigLoader.getProperty("phone.no");

	public static final String PASS_WORD = ConfigLoader.getProperty("password");

	public static final String CONFIRM_PASSWORD = ConfigLoader.getProperty("confirm.password");

	public static final String OTP = ConfigLoader.getProperty("O.T.P");

	public static final String JOB_TITLE = ConfigLoader.getProperty("job.title");

	public static final String JOB_SKILLS = ConfigLoader.getProperty("job.skills");

	public static final String JOB_LOCATION = ConfigLoader.getProperty("job.location");

	public static final String JOB_TYPE = ConfigLoader.getProperty("job.type");

	public static final String EMPLOYEMENT_TYPE = ConfigLoader.getProperty("employement.type");

	public static final String EXPERIENCE_LEVEL = ConfigLoader.getProperty("experience.level");

	public static final String SALARY_LEVEL = ConfigLoader.getProperty("salary.level");

	public static final String LANGUAGE_PREFERENCE = ConfigLoader.getProperty("language.preference");

	public static final String TONE = ConfigLoader.getProperty("tone");

}
