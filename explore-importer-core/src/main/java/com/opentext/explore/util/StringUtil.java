package com.opentext.explore.util;

public class StringUtil {
	public static String[] stringToArrayString(String value) {
		if(value != null) {
			return value.split(",");
		}
		else {
			return new String[] {};
		}

	}
	
	/**
	 * Convert CamelCase string into human-readable string
	 * @see https://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
	 * @param Camel Case string
	 * @return Human readable string
	 */
	public static String camelCaseToHumanReadable(String s) {
		return s.replaceAll(String.format("%s|%s|%s", 
				"(?<=[A-Z])(?=[A-Z][a-z])", 
				"(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
	
	/**
	 * Convert snake_case string into human-readable string
	 * @see https://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
	 * @param Snake case string
	 * @return Human readable string
	 */
	public static String snakeCaseToHumanReadable(String s) {
		return s.replaceAll("_", " ");
	}		
}
