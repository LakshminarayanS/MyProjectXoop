package com.Config.com;

import java.io.InputStream;

import com.aventstack.extentreports.Status;
import com.constants.com.FrameworkConstants;
import com.utils.ExtentReportManager;

public class ConfigLoader extends FrameworkConstants {

	static {
		try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				String errorMessage = "Unable to find config.properties";
				ExtentReportManager.log(Status.FAIL, errorMessage);
				throw new RuntimeException(errorMessage);
			}
			// Load the properties file
			properties.load(input);
			ExtentReportManager.log(Status.INFO, "Successfully loaded config.properties");
		} catch (Exception e) {
			String errorMessage = "Error loading config.properties: " + e.getMessage();
			ExtentReportManager.log(Status.FAIL, errorMessage);
			System.err.println(errorMessage);
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {

		String value = properties.getProperty(key);

		if (value == null) {
			String errorMessage = "Property key '" + key + "' not found in config file.";
			ExtentReportManager.log(Status.WARNING, errorMessage);
			throw new RuntimeException(errorMessage);
		}

		ExtentReportManager.log(Status.INFO, "Retrieved property: " + key + " = " + value);
		return value;
	}

}
