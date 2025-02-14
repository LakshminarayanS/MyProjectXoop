package com.constants.com;

import java.util.Properties;

import com.Config.com.ConfigLoader;

public class FrameworkConstants {

	public static final Properties properties = new Properties();

	public static final String BROWSER_NAME = ConfigLoader.getProperty("config.properties", "browser.name");

	public static final String BASE_URL = ConfigLoader.getProperty("config.properties", "base.url");

	public static final String DROPDOWN_TYPE = ConfigLoader.getProperty("config.properties", "dropdown.type");

	public static final String SCREENSHOT_DIR = ConfigLoader.getProperty("config.properties", "screenshot.dir");

	public static final String FULL_NAME = ConfigLoader.getProperty("config.properties", "full.name");

	public static final String EMAIL_ID = ConfigLoader.getProperty("config.properties", "email.id");

	public static final String COMPANY_NAME = ConfigLoader.getProperty("config.properties", "company.name");

	public static final String PHONE_NO = ConfigLoader.getProperty("config.properties", "phone.no");

	public static final String PASS_WORD = ConfigLoader.getProperty("config.properties", "password");

	public static final String CONFIRM_PASSWORD = ConfigLoader.getProperty("config.properties", "confirm.password");

	public static final String OTP = ConfigLoader.getProperty("config.properties", "O.T.P");

	public static final String JOB_TITLE = ConfigLoader.getProperty("config.properties", "job.title");

	public static final String JOB_SKILLS = ConfigLoader.getProperty("config.properties", "job.skills");

	public static final String JOB_LOCATION = ConfigLoader.getProperty("config.properties", "job.location");

	public static final String JOB_TYPE = ConfigLoader.getProperty("config.properties", "job.type");

	public static final String EMPLOYEMENT_TYPE = ConfigLoader.getProperty("config.properties", "employement.type");

	public static final String EXPERIENCE_LEVEL = ConfigLoader.getProperty("config.properties", "experience.level");

	public static final String SALARY_LEVEL = ConfigLoader.getProperty("config.properties", "salary.level");

	public static final String LANGUAGE_PREFERENCE = ConfigLoader.getProperty("config.properties",
			"language.preference");

	public static final String TONE = ConfigLoader.getProperty("config.properties", "tone");

}
