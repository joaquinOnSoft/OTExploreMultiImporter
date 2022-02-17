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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.opentext.explore.importer.excel.JSonMappingConfigReader;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;
import com.opentext.explore.util.FileUtil;

public abstract class TestAbstractXMLGenerator {

	protected TextDataImporterMapping mapping = null;
	protected String expectedContent;

	@Before
	public void setUp() {
		File jsonConfigFile = FileUtil.getFileFromResources("excel_mapping.json");
		JSonMappingConfigReader jsonConfigReader = new JSonMappingConfigReader();
		mapping = jsonConfigReader.read(jsonConfigFile);	
				
		File referenceXMLFile = FileUtil.getFileFromResources(getReferenceXMLFilePath());
		expectedContent = fileToString(referenceXMLFile.getAbsolutePath());			
	}

	private String fileToString(String referenceXMLFilePath) {
		String str = null;
		try {
            // default StandardCharsets.UTF_8
			str = Files.readString(Paths.get(referenceXMLFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
		
		return str;
	}

	protected abstract String getXMLOutputFileName();
	
	protected abstract String getReferenceXMLFilePath();
	
	protected abstract AbstractConfigGenerator getXMLGenerator();
	
	@Test
	public void testGenerateConfigFile() {				
		assertNotNull(mapping);
		
		AbstractConfigGenerator generator = getXMLGenerator();
		String path = null;
		
		try {
			path = generator.generateConfigFile(mapping, getXMLOutputFileName(), "Ticket");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertNotNull(path);
		
		File f = new File(path);
		Path p = Paths.get(f.getAbsolutePath());
		assertTrue(Files.exists(p));	
				
		assertEquals(expectedContent, fileToString(f.getAbsolutePath()));
	}	
}
