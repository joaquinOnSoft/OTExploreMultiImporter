package com.opentext.explore.importer.mail;

import com.opentext.explore.util.DateUtil;

public class Eml2Excel {
	
	public String process(String path) {
		String outputFileName = null;
		return process(path, outputFileName);
	}
	
	public String process(String path, String outputFileName) {
		return null;
	}
	
	protected String createOutputFileName() {
		StringBuilder outputFileName = new StringBuilder();
		outputFileName.append("EML2Excel-")
			.append(DateUtil.now("yyyyMMddHHmmss"))
			.append(".xls");
		
		return outputFileName.toString();
	}
}
