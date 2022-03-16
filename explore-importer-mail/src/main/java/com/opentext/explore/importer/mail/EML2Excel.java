package com.opentext.explore.importer.mail;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.importer.mail.util.File2MimeMessage;
import com.opentext.explore.util.DateUtil;
import com.opentext.explore.util.FileUtil;

public class EML2Excel {
	
	private static final Logger log = LogManager.getLogger(EML2Excel.class);

	public String process(File folder) {		
		return process(folder, createOutputFileName());
	}
	
	/**
	 * Process all the .eml files contained in a given folder and generates 
	 * an excel file which contains a row for each .eml file found.
	 * @param folder - Path (a folder) to be evaluated
	 * @param outputFileName - Name or the Excel file generated. 
	 * @return Name or the Excel file generated, 'null' if is not created
	 */
	public String process(File folder, String outputFileName) {
		List<String> files = null;
		List<MimeMessage> messages = new LinkedList<MimeMessage>();
		
		if(folder != null && folder.isDirectory()) {
			try {
				files = FileUtil.findFiles(folder.toPath(), "eml");
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			
			if(files != null) {
				File2MimeMessage converter = new File2MimeMessage();
				MimeMessage msg = null;
				
				for(String fileName: files) {
					log.debug("Processing e-mail: " + fileName);
					
					msg = converter.convert(new File(fileName));
					if(msg != null) {
						messages.add(msg);
					}
				}
				
				if(messages != null) {
					ExcelWriter writter = new ExcelWriter();
					writter.write(outputFileName, messages);
				}
			}
		}
		
		return outputFileName;
	}
	
	protected String createOutputFileName() {
		StringBuilder outputFileName = new StringBuilder();
		outputFileName.append("EML2Excel-")
			.append(DateUtil.now("yyyyMMddHHmmss"))
			.append(".xlsx");
		
		return outputFileName.toString();
	}
}
