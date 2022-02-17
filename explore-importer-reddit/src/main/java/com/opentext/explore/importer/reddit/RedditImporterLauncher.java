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
package com.opentext.explore.importer.reddit;

import java.util.Arrays;
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
 * @since 20.2
 */
public class RedditImporterLauncher {
	
	private static final String DEFAULT_REDDIT_THREAD_NAME = "CanadaPost";
	private static final String DEFAULT_SOLR_URL = "http://localhost:8983";
	private static final String DEFAULT_REDDIT_IMPORT_TAG = "Reddit";
	private static final int DEFAULT_POOLING_TIME_IN_SECONDS = 60;
	
	private static final Logger log = LogManager.getLogger(RedditImporterLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option hostOption = new Option("h", "host", true, "Solr URL. Default value: http://localhost:8983");
		options.addOption(hostOption);			
		
		Option rtagOption = new Option("r", "rtag", true, "Explore Reddit Importer tag. Added to each article importer");
		options.addOption(rtagOption);		
		
		Option threadOption = new Option("s", "subreddit", true, "Subreddit thread name");
		threadOption.setRequired(true);
		options.addOption(threadOption);

		Option filterOption = new Option("f", "filter", true, "Filter subreddit messages (Must contains some of the words separated by commas)");
		options.addOption(filterOption);		
		
		Option timeOption = new Option("t", "time", true, "Seconds between each call against Reddit API. Default value 60 secs");
		options.addOption(timeOption);		
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String rtag = DEFAULT_REDDIT_IMPORT_TAG;
			String host = DEFAULT_SOLR_URL;
			String subreddit = DEFAULT_REDDIT_THREAD_NAME;
			List <String> filters = null;
			
			int timeInSeconds = DEFAULT_POOLING_TIME_IN_SECONDS;
			
			if (cmd.hasOption("rtag") || cmd.hasOption("r")) {
				rtag = cmd.getOptionValue("rtag");
			}

			if (cmd.hasOption("host") || cmd.hasOption("h")) {
				host = cmd.getOptionValue("host");
			}				
			
			if (cmd.hasOption("subreddit") || cmd.hasOption("s")) {
				subreddit = cmd.getOptionValue("subreddit");
			}

			if (cmd.hasOption("filter") || cmd.hasOption("f")) {
				String strFilter = cmd.getOptionValue("filter");
				String[] arrayFilter = strFilter.split(",");
				filters = Arrays.asList(arrayFilter);
			}			
			
			if (cmd.hasOption("time") || cmd.hasOption("t")) {
				String strTimeInSeconds = cmd.getOptionValue("time");
				try {
					timeInSeconds = Integer.parseInt(strTimeInSeconds);
				}
				catch(NumberFormatException e) {
					formatter.printHelp("java -jar OTExploreRedditImporter.20.2.4.jar --rtag \"Reddit Canada Post\" --subreddit CanadaPost", options);

					exitInError(e);
				}
			}
					
			RedditImporter importer = new RedditImporter(host);
			importer.start(subreddit, filters, rtag, timeInSeconds);
			
		}
		catch (ParseException e) {
			formatter.printHelp("java -jar OTExploreRedditImporter.20.4.jar --rtag \"Reddit Canada Post\" --subreddit CanadaPost", options);

			exitInError(e);	
		}
	}
	
	private static void exitInError(Exception e) {
		log.error(e.getMessage());
		System.exit(-1);	
	}
}
