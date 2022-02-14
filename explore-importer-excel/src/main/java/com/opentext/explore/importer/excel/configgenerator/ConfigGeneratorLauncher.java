package com.opentext.explore.importer.excel.configgenerator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.importer.excel.JSonMappingConfigReader;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;

public class ConfigGeneratorLauncher {
	
	private static final String CONFIG_FILE_SCHEMA_XML = "schema.xml";
	private static final String CONFIG_FILE_EXPLORE_CONFIGURATION_XML = "Explore.Configuration.xml";
	private static final Logger log = LogManager.getLogger(ConfigGeneratorLauncher.class);
	
	public static void main(String[] args) {		
		Options options = new Options();
	
		Option configOption = new Option("c", "config", true, "JSON file that defines the mapping between excel columns and Solr fields");
		configOption.setRequired(true);
		options.addOption(configOption);				

		Option docTypeOption = new Option("d", "doctype", true, "Document type to be created, e.g. Ticket");
		docTypeOption.setRequired(true);
		options.addOption(docTypeOption);			
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String configPath = null;
			String docType = null;
			
			if (cmd.hasOption("config") || cmd.hasOption("c")) {
				configPath = cmd.getOptionValue("config");
				
				if(!fileExists(configPath)) {
					exitInError(configPath + " is not a file.");
				}
			}					
						
			if (cmd.hasOption("doctype") || cmd.hasOption("d")) {
				docType = cmd.getOptionValue("doctype");
			}
		
			//Read the configuration
			JSonMappingConfigReader configReader = new JSonMappingConfigReader();
			TextDataImporterMapping mapping =  configReader.read(configPath);
			
			//Generate Explore.Configuration.xml (Explore configuration)
			ExploreConfigurationXMLGenerator eGenerator = new ExploreConfigurationXMLGenerator();
			eGenerator.generateConfigFile(mapping, CONFIG_FILE_EXPLORE_CONFIGURATION_XML, docType);
			log.info(CONFIG_FILE_EXPLORE_CONFIGURATION_XML + " created");
			
			//Generate schema.xml (Solr configuration)
			SolrSchemaXMLGenerator sGenerator = new SolrSchemaXMLGenerator();
			sGenerator.generateConfigFile(mapping, CONFIG_FILE_SCHEMA_XML, docType);
			log.info(CONFIG_FILE_SCHEMA_XML + " created");
		}
		catch (ParseException e) {
			formatter.printHelp("java ConfigGeneratorLauncher --config excel_mapping.json --doctype Ticket", options);

			exitInError(e.getMessage());	
		} catch (IOException e) {
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
