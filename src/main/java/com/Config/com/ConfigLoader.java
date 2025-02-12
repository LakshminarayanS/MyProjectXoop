package com.Config.com;

import java.io.InputStream;

import com.constants.com.FrameworkConstants;

public class ConfigLoader extends FrameworkConstants {

	static {
		try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("Unable to find config.properties");
			}
			// Load the properties file
			properties.load(input);
		} catch (Exception e) {
			System.err.println("Error loading config.properties: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {

		String value = properties.getProperty(key);

		if (value == null) {
			throw new RuntimeException("Property key '" + key + "' not found in config file.");
		}
		return value;
	}

}
