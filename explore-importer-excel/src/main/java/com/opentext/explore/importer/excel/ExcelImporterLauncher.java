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
 *     Joaqu�n Garz�n - initial implementation
 *
 */
package com.opentext.explore.importer.excel;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExcelImporterLauncher {
	private static final String DEFAULT_SOLR_URL = "http://localhost:8983";
	private static final String DEFAULT_EXCEL_IMPORT_TAG = "Excel Importer";
	private static final String DEFAULT_LANGUAGE_ENGLISH = "en";
	
	private static final Logger log = LogManager.getLogger(ExcelImporterLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option languageOption = new Option("l", "lang", true, "ISO 639-1 language code. By default English (en)");
		options.addOption(languageOption);			
		
		Option hostOption = new Option("h", "host", true, "Solr URL. Default value: http://localhost:8983");
		options.addOption(hostOption);			
		
		Option tagOption = new Option("t", "tag", true, "Explore tag. Added to each Excel row imported. Default value: Excel Importer");
		options.addOption(tagOption);
		
		Option excelOption = new Option("e", "excel", true, "Excel file to be imported");
		excelOption.setRequired(true);
		options.addOption(excelOption);			

		Option configOption = new Option("c", "config", true, "JSON file that defines the mapping between excel columns and Solr fields");
		configOption.setRequired(true);
		options.addOption(configOption);	
		
		Option contentTypeOption = new Option("k", "contentType", true, "Explore content type, i.e. Ticket, Twitter, Reddit, Survey, Call...");
		contentTypeOption.setRequired(true);
		options.addOption(contentTypeOption);			
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String language = DEFAULT_LANGUAGE_ENGLISH;
			String tag = DEFAULT_EXCEL_IMPORT_TAG;
			String host = DEFAULT_SOLR_URL;
			String excelPath = null;
			String configPath = null;
			String contentType = null;
			
			if (cmd.hasOption("language") || cmd.hasOption("l")) {
				language = cmd.getOptionValue("language");
			}
			
			if (cmd.hasOption("tag") || cmd.hasOption("t")) {
				tag = cmd.getOptionValue("tag");
			}

			if (cmd.hasOption("host") || cmd.hasOption("h")) {
				host = cmd.getOptionValue("host");
			}				
			
			if (cmd.hasOption("excel") || cmd.hasOption("e")) {
				excelPath = cmd.getOptionValue("excel");
				
				if(!fileExists(excelPath)) {
					exitInError(excelPath + " is not a file.");
				}
				
			}		
			
			if (cmd.hasOption("config") || cmd.hasOption("c")) {
				configPath = cmd.getOptionValue("config");
				
				if(!fileExists(configPath)) {
					exitInError(configPath + " is not a file.");
				}
			}		
			
			if (cmd.hasOption("contentType") || cmd.hasOption("k")) {
				contentType = cmd.getOptionValue("contentType");
			}			
						
			ExcelImporter importer = new ExcelImporter(host);
			importer.start(excelPath, configPath, contentType, tag, language);
			
		}
		catch (ParseException e) {
			formatter.printHelp("java -jar OTExploreExcelImporter.21.1.jar --host http://localhost:8983 --tag \"Excel Importer\" --excel input_example.xlsx --config excel_mapping.json", options);

			exitInError(e.getMessage());	
		}
	}
	
	private static void exitInError(String msg) {
		log.error(msg);
		System.err.println(msg);
		System.exit(-1);	
	}	
	
	private static boolean fileExists(String path) {
		File f = new File(path);
		return f.exists();
	}
}
