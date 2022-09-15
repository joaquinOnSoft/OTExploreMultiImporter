/*
 *   (C) Copyright 2022 OpenText and others.
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
package com.opentext.explore.importer.tripadvisor.v2.excel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Joaquín Garzón
 * @since 22.09.15
 */
public class TripadvisorImporterFromExcelLauncher {
	private static final String DEFAULT_SOLR_URL = "http://localhost:8983";
		
	private static final Logger log = LogManager.getLogger(TripadvisorImporterFromExcelLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option hostOption = new Option("h", "host", true, "Solr URL. Default value: http://localhost:8983");
		options.addOption(hostOption);					
		
		Option pathOption = new Option("p", "path", true, "Base path to look for .xls file with Tripadvisor reviews");
		pathOption.setRequired(true);
		options.addOption(pathOption);			
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		String path = null; 
		String host = DEFAULT_SOLR_URL;

		try {
			cmd = parser.parse(options, args);
			
			if (cmd.hasOption("path") || cmd.hasOption("p")) {
				path = cmd.getOptionValue("path");
			}
			
			if (cmd.hasOption("host") || cmd.hasOption("h")) {
				host = cmd.getOptionValue("host");
			}				
					
			if(path != null) {
				TripadvisorImporterFromExcel importer = new TripadvisorImporterFromExcel(host);
				importer.start(path);
			}			
		}
		catch (ParseException | NumberFormatException e) {
			formatter.printHelp("java com.opentext.explore.importer.tripadvisor.v2.excel.TripadvisorImporterFromExcelLauncher --path .", options);

			log.error(e.getMessage());
			System.exit(-1);
		}
	}

}
