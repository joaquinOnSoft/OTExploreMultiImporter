package com.opentext.explore.importer.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestURLReader {
	@Test
	public void read() {
		URLReader reader = new URLReader("https://www.google.es");
		String html = reader.read();
		
		assertNotNull(html);
		assertTrue(html.contains("google"));
	}
}
