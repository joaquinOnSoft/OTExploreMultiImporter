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
package com.opentext.explore.importer.tripadvisor.v1;

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
 * @since 22.09.01
 */
public class TripadvisorImporterLauncher {
	private static final String DEFAULT_SOLR_URL = "http://localhost:8983";
	private static final String DEFAULT_TRIPADVISOR_IMPORT_TAG = "TripaAdvisor Review";
	private static final int DEFAULT_POOLING_TIME_IN_SECONDS = 300;
	
	private static final Logger log = LogManager.getLogger(TripadvisorImporterLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option hostOption = new Option("h", "host", true, "Solr URL. Default value: http://localhost:8983");
		options.addOption(hostOption);			
		
		Option itagOption = new Option("i", "itag", true, "Explore Importer tag. Added to each article importer. Default value `TripaAdvisor Review`");
		options.addOption(itagOption);
		
		Option exactOption = new Option("e", "exact", false, "Exact match. If set the search term must be contained in the page title or url");
		options.addOption(exactOption);				
				
		Option aliasOption = new Option("s", "search", true, "Search term to look for in tripadvisor.com");
		aliasOption.setRequired(true);
		options.addOption(aliasOption);
		
		Option timeOption = new Option("t", "time", true, "Seconds between each call against Trustpilot.com. Default value 300 secs (5 minutes).");
		options.addOption(timeOption);		
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String itag = DEFAULT_TRIPADVISOR_IMPORT_TAG;
			String host = DEFAULT_SOLR_URL;
			boolean exactMatch = false;
			String searchTerm = null;
						
			int timeInSeconds = DEFAULT_POOLING_TIME_IN_SECONDS;
			
			if (cmd.hasOption("itag") || cmd.hasOption("i")) {
				itag = cmd.getOptionValue("itag");
			}

			if (cmd.hasOption("host") || cmd.hasOption("h")) {
				host = cmd.getOptionValue("host");
			}				

			if (cmd.hasOption("exact") || cmd.hasOption("e")) {
				exactMatch = true;
			}				
			
			if (cmd.hasOption("search") || cmd.hasOption("s")) {
				searchTerm = cmd.getOptionValue("search");
			}	
			
			if (cmd.hasOption("time") || cmd.hasOption("t")) {
				String strTimeInSeconds = cmd.getOptionValue("time");
				try {
					timeInSeconds = Integer.parseInt(strTimeInSeconds);
				}
				catch(NumberFormatException e) {
					formatter.printHelp("java -jar OTExploreTripAdvisorImporter.22.09.01.jar --itag TripAdvisor --search \"club med\"", options);

					exitInError(e);
				}
			}
					
			TripadvisorImporter importer = new TripadvisorImporter(host);
			importer.start(searchTerm, exactMatch, itag, timeInSeconds);
			
		}
		catch (ParseException e) {
			formatter.printHelp("java -jar OTExploreTrustImporter.22.02.16.jar --itag Trustpilot --alias bancsabadell.com", options);

			exitInError(e);	
		}
	}
	
	private static void exitInError(Exception e) {
		log.error(e.getMessage());
		System.exit(-1);	
	}
}
