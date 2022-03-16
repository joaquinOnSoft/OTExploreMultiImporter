package com.opentext.explore.importer.mail;

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
			
			if(path != null) {
				File parentFolder = new File(path);
				if(parentFolder.isDirectory()) {
					EML2Excel converter = new EML2Excel();
					String outputFile = converter.process(parentFolder);
					
					System.out.println("Output file generated: " + outputFile);
				}
				else {
					System.err.println("'path' should be a directory");
				}
			}
			
		}
		catch (ParseException e) {
			formatter.printHelp("java -jar OTExploreRedditImporter.20.4.jar --rtag \"Reddit Canada Post\" --subreddit CanadaPost", options);

			log.error(e.getMessage());
			System.exit(-1);	
		}
	}
}
