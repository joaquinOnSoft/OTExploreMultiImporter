/*
 *   (C) Copyright 2021 OpenText and others.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Contributors:
 *     Joaquín Garzón - initial implementation
 *
 */
package com.opentext.explore.importer.excel.configgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
