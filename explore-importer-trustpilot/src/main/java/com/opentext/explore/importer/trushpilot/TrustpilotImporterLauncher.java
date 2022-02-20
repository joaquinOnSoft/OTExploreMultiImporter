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
 *     Joaqu�n Garz�n - initial implementation
 *
 */
package com.opentext.explore.importer.trushpilot;

import java.util.List;

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
 * @since 22.02.16
 */
public class TrustpilotImporterLauncher {
	
	private static final String DEFAULT_TRUSTPILOT_THREAD_NAME = "bancsabadell.com";
	private static final String DEFAULT_SOLR_URL = "http://localhost:8983";
	private static final String DEFAULT_TRUSTPILOT_IMPORT_TAG = "Trustpilot";
	private static final int DEFAULT_POOLING_TIME_IN_SECONDS = 300;
	
	private static final Logger log = LogManager.getLogger(TrustpilotImporterLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option hostOption = new Option("h", "host", true, "Solr URL. Default value: http://localhost:8983");
		options.addOption(hostOption);			
		
		Option itagOption = new Option("i", "itag", true, "Explore Importer tag. Added to each article importer");
		options.addOption(itagOption);		
		
		Option aliasOption = new Option("a", "alias", true, "Trustpilot client alias");
		aliasOption.setRequired(true);
		options.addOption(aliasOption);
		
		Option timeOption = new Option("t", "time", true, "Seconds between each call against Trustpilot.com. Default value 300 secs (5 minutes).");
		options.addOption(timeOption);		
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String itag = DEFAULT_TRUSTPILOT_IMPORT_TAG;
			String host = DEFAULT_SOLR_URL;
			String alias = DEFAULT_TRUSTPILOT_THREAD_NAME;
			List <String> filters = null;
			
			int timeInSeconds = DEFAULT_POOLING_TIME_IN_SECONDS;
			
			if (cmd.hasOption("itag") || cmd.hasOption("i")) {
				itag = cmd.getOptionValue("itag");
			}

			if (cmd.hasOption("host") || cmd.hasOption("h")) {
				host = cmd.getOptionValue("host");
			}				
			
			if (cmd.hasOption("alias") || cmd.hasOption("a")) {
				alias = cmd.getOptionValue("alias");
			}	
			
			if (cmd.hasOption("time") || cmd.hasOption("t")) {
				String strTimeInSeconds = cmd.getOptionValue("time");
				try {
					timeInSeconds = Integer.parseInt(strTimeInSeconds);
				}
				catch(NumberFormatException e) {
					formatter.printHelp("java -jar OTExploreTrustImporter.22.02.16.jar --itag Trustpilot --alias bancsabadell.com", options);

					exitInError(e);
				}
			}
					
			TrustpilotImporter importer = new TrustpilotImporter(host);
			importer.start(null, alias, itag, timeInSeconds);
			
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
