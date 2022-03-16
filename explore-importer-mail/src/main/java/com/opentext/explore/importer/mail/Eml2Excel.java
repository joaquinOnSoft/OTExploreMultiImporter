package com.opentext.explore.importer.mail;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.util.DateUtil;
import com.opentext.explore.util.FileUtil;

public class Eml2Excel {
	
	private static final Logger log = LogManager.getLogger(Eml2Excel.class);

	public String process(File folder) {		
		return process(folder);
	}
	
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
				for(String fileName: files) {
					
				}
				
				ExcelWriter writter = new ExcelWriter();
				writter.write(outputFileName, null);
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
