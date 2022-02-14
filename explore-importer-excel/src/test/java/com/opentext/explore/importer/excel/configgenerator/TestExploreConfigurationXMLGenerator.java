package com.opentext.explore.importer.excel.configgenerator;

import org.junit.Test;

public class TestExploreConfigurationXMLGenerator extends TestAbstractXMLGenerator {

	@Override
	protected String getXMLOutputFileName() {		
		return "Explore.Configuration.xml";
	}

	@Override
	protected AbstractConfigGenerator getXMLGenerator() {
		return new ExploreConfigurationXMLGenerator();
	}
	
	@Override
	protected String getReferenceXMLFilePath() {
		return "Explore.Configuration.xml";
	}
	
	@Test 
	public void strToHumanReadable() {
		AbstractConfigGenerator generator = getXMLGenerator();
		String str = generator.strToHumanReadable("fecha_cierre");
		
		assertNotNull(str);
		assertEquals("fecha cierre", str);
		
		str = generator.strToHumanReadable("fechaCierre");
		
		assertNotNull(str);
		assertEquals("fecha cierre", str);
		
		str = generator.strToHumanReadable("fecha cierre");
		
		assertNotNull(str);
		assertEquals("fecha cierre", str);
		
	}
}
