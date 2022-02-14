/*
 *   (C) Copyright 2021 OpenText and others.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Contributors:
 *     Joaquín Garzón - initial implementation
 *
 */
package com.opentext.explore.util;

public class TextUtil {
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
