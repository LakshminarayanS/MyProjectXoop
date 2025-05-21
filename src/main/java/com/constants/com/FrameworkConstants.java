package com.constants.com;

import java.util.Arrays;

import com.Config.com.ConfigLoader;

public class FrameworkConstants {

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

	public static final String JOB_ROLE = ConfigLoader.getProperty("config.properties", "job.role");

	public static final String JOB_SKILLS_RAW = ConfigLoader.getProperty("config.properties", "job.skills");

	public static final String[] JOB_SKILLS = Arrays.stream(JOB_SKILLS_RAW.split(",")).map(String::trim)
			.toArray(String[]::new);

	public static final String JOB_LOCATION = ConfigLoader.getProperty("config.properties", "job.location");

	public static final String JOB_TYPE = ConfigLoader.getProperty("config.properties", "job.type");

	public static final String EMPLOYEMENT_TYPE = ConfigLoader.getProperty("config.properties", "employement.type");

	public static final String MIN_EXPERIENCE = ConfigLoader.getProperty("config.properties", "min_experience");

	public static final String MAX_EXPERIENCE = ConfigLoader.getProperty("config.properties", "max_experience");

	public static final String INDUSTRY_TYPE = ConfigLoader.getProperty("config.properties", "industry_type");

	public static final String LANGUAGE_PREFERENCE = ConfigLoader.getProperty("config.properties",
			"language.preference");

	public static final String TONE = ConfigLoader.getProperty("config.properties", "tone");

	public static final String JOB_DESCRIPTION = ConfigLoader.getProperty("config.properties", "job_description");

}
