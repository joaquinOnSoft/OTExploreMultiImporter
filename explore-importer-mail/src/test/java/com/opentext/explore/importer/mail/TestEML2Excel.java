package com.opentext.explore.importer.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.opentext.explore.util.FileUtil;

public class TestEML2Excel {
	private EML2Excel converter = new EML2Excel();
			
	@Test
	public void process() {
		File mail = FileUtil.getFileFromResources("mail-sample-01.eml");
		assertNotNull(mail);
		
		String parentFolder = mail.getParent();
		assertNotNull(parentFolder);
		
		String outputFileName = converter.process(new File(parentFolder));
		assertNotNull(outputFileName);
		assertEquals("", outputFileName);
	}
	
	@Test
	public void createOutputFileName() {
		String fileName = converter.createOutputFileName();
		assertNotNull(fileName);
		assertTrue(fileName.startsWith("EML2Excel-"));
		assertTrue(fileName.endsWith(".xlsx"));
	}	
}
