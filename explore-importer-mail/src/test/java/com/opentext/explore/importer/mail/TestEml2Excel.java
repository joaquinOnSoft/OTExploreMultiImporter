package com.opentext.explore.importer.mail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestEml2Excel {
	private Eml2Excel converter = new Eml2Excel();
			
	@Test
	public void createOutputFileName() {
		String fileName = converter.createOutputFileName();
		assertNotNull(fileName);
		assertTrue(fileName.startsWith("EML2Excel-"));
		assertTrue(fileName.endsWith(".xls"));
	}	
}
