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
package com.opentext.explore.importer.tripadvisor.v2;

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
	private static final int DEFAULT_NUM_CONSUMERS = 2;
	
	private static final Logger log = LogManager.getLogger(TripadvisorImporterLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option hostOption = new Option("h", "host", true, "Solr URL. Default value: http://localhost:8983");
		options.addOption(hostOption);			
		
		Option itagOption = new Option("i", "itag", true, "Explore Importer tag. Added to each article importer. Default value `TripaAdvisor Review`");
		options.addOption(itagOption);
		
		Option excellOption = new Option("x", "excel", false, "Use MS Excel output file instead of the default insert in Solr.");
		options.addOption(excellOption);				
		
		Option urlOption = new Option("u", "url", true, "Facility URL in tripadvisor.com. (Incompatible with `search` option)");
		options.addOption(urlOption);		
		
		Option aliasOption = new Option("s", "search", true, "Search term to look for in tripadvisor.com. (Incompatible with `url` option)");
		options.addOption(aliasOption);
	
		Option exactOption = new Option("e", "exact", false, "Exact match. If set the search term must be contained in the page title or url. Used only with `search` option");
		options.addOption(exactOption);			
		
		Option numConsumersOption = new Option("c", "consumers", true, "Number of consumers (threads) used simultaneously to scrap the page.");
		options.addOption(numConsumersOption);			
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		String itag = DEFAULT_TRIPADVISOR_IMPORT_TAG;
		String host = DEFAULT_SOLR_URL;
		String url = null;
		boolean exactMatch = false;
		boolean excellOutput = false;
		String searchTerm = null;		
		int numConsumers = DEFAULT_NUM_CONSUMERS;

		try {
			cmd = parser.parse(options, args);
						
			if (cmd.hasOption("host") || cmd.hasOption("h")) {
				host = cmd.getOptionValue("host");
			}
			
			if (cmd.hasOption("itag") || cmd.hasOption("i")) {
				itag = cmd.getOptionValue("itag");
			}		

			if (cmd.hasOption("excel") || cmd.hasOption("x")) {
				excellOutput = true;
			}				

			if (cmd.hasOption("url") || cmd.hasOption("u")) {
				url = cmd.getOptionValue("url");			
			}				
			
			if (cmd.hasOption("search") || cmd.hasOption("s")) {
				searchTerm = cmd.getOptionValue("search");
			}	
			
			if (cmd.hasOption("exact") || cmd.hasOption("e")) {
				exactMatch = true;
			}				
					
			numConsumers = getNumericParam(cmd, "consumers", "c", DEFAULT_NUM_CONSUMERS);			

			if(searchTerm == null  && url == null) {
				formatter.printHelp("java -jar OTExploreTripadvisorImporter.22.02.19.jar --itag Tripadvisor --search \"Club Med\"", options);
			}
			else {
				TripadvisorImporter importer = new TripadvisorImporter(host, numConsumers);
				if(url != null) {
					importer.start(url, itag, excellOutput);
				}
				else {
					importer.start(searchTerm, exactMatch, itag, excellOutput);
				}
			}			
		}
		catch (ParseException | NumberFormatException e) {
			formatter.printHelp("java -jar OTExploreTripadvisorImporter.22.02.19.jar --itag Tripadvisor  --search \"Club Med\"", options);

			log.error(e.getMessage());
			System.exit(-1);
		}
	}

	private static int getNumericParam(CommandLine cmd, String longParamName, String shortParamName, int defaultValue) throws NumberFormatException {
		int numParam = defaultValue;
					
		if (cmd.hasOption(longParamName) || cmd.hasOption(shortParamName)) {
			String numParamStr = cmd.getOptionValue(longParamName);
			try {
				numParam = Integer.parseInt(numParamStr);
			}
			catch(NumberFormatException e) {
				log.error("Invalid param value for parameter '{}'", longParamName);
				throw e;
			}
		}
		
		return numParam;
	}
}
