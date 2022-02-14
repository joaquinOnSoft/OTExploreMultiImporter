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
package com.opentext.explore.importer.excel;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;

public class JSonMappingConfigReader {
	private static final Logger log = LogManager.getLogger(JSonMappingConfigReader.class);

	/**
	 * Read the mapping JSON file that defines the mapping between the excel headers
	 * and the Solr fields used in Qfiniti/Explore
	 * 
	 * @see JSON to Java Object.
	 *      https://www.baeldung.com/jackson-object-mapper-tutorial#2-json-to-java-object
	 * @param file - JSON File object
	 * @return Object with the field mapping
	 */
	public TextDataImporterMapping read(File file) {
		TextDataImporterMapping config = null;

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			config = objectMapper.readValue(file, TextDataImporterMapping.class);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return config;
	}

	/**
	 * Read the configuration JSON file that defines the mapping between the 3rd
	 * party excel including metadata and the input file required by OpenText
	 * Qfiniti Data Importer
	 * 
	 * @see JSON to Java Object.
	 *      https://www.baeldung.com/jackson-object-mapper-tutorial#2-json-to-java-object
	 * @param filename - File Name
	 * @return Object with the field mapping
	 */
	public TextDataImporterMapping read(String filename) {
		return read(new File(filename));
	}
}
