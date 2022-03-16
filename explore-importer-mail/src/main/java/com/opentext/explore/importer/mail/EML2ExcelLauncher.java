package com.opentext.explore.importer.mail;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EML2ExcelLauncher {
	private static final Logger log = LogManager.getLogger(EML2ExcelLauncher.class);
	
	public static void main(String[] args) {		
		Options options = new Options();

		Option pathOption = new Option("p", "path", true, "Path to folder that contains");
		pathOption.setRequired(true);
		options.addOption(pathOption);			
				
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String path = null;
			if (cmd.hasOption("path") || cmd.hasOption("p")) {
				path = cmd.getOptionValue("path");
			}
			
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
