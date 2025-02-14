package com.Config.com;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.aventstack.extentreports.Status;
import com.constants.com.FrameworkConstants;
import com.utils.ExtentReportManager;

public class ConfigLoader extends FrameworkConstants {

	private static final Map<String, Properties> propertiesMap = new HashMap<>();

	static {
		loadProperties("config.properties");
		loadProperties("data.properties"); // Load additional properties file
	}

	private static void loadProperties(String fileName) {
		try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
			if (input == null) {
				String errorMessage = "Unable to find " + fileName;
				ExtentReportManager.log(Status.FAIL, errorMessage);
				throw new RuntimeException(errorMessage);
			}
			// Load the properties file
			properties.load(input);
			propertiesMap.put(fileName, properties);
			ExtentReportManager.log(Status.INFO, "Successfully loaded " + fileName);
		} catch (Exception e) {
			String errorMessage = "Error loading " + fileName + ": " + e.getMessage();
			ExtentReportManager.log(Status.FAIL, errorMessage);
			System.err.println(errorMessage);
			e.printStackTrace();
		}
	}

	public static String getProperty(String fileName, String key) {
		Properties properties = propertiesMap.get(fileName);

		if (properties == null) {
			String errorMessage = "Properties file '" + fileName + "' not loaded.";
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage);
		}

		String value = properties.getProperty(key);

		if (value == null) {
			String errorMessage = "Property key '" + key + "' not found in " + fileName;
			ExtentReportManager.log(Status.WARNING, errorMessage);
			throw new RuntimeException(errorMessage);
		}

		ExtentReportManager.log(Status.INFO, "Retrieved property from " + fileName + ": " + key + " = " + value);
		return value;
	}
}
