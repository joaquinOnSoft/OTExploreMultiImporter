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
package com.opentext.explore.importer.excel;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;
import com.opentext.explore.importer.excel.pojo.Field;
import com.opentext.explore.util.FileUtil;

import junit.framework.TestCase;

public class TestJSonMappingConfigReader extends TestCase {

	private TextDataImporterMapping readConfigFile(String jsonFileName) {
		File jsonConfigFile = FileUtil.getFileFromResources(jsonFileName);
		JSonMappingConfigReader jsonConfigReader = new JSonMappingConfigReader();
		TextDataImporterMapping mapping = jsonConfigReader.read(jsonConfigFile);
		return mapping;
	}

	@Test
	public void testReadFileWithSpecialCharacters() {
		TextDataImporterMapping mapping = readConfigFile("excel_mapping_with_special_characters.json");

		assertNull(mapping);
	}

	@Test
	public void testRead() {
		TextDataImporterMapping mapping = readConfigFile("excel_mapping.json");

		assertNotNull(mapping);
		List<Field> fields = mapping.getFields();
		assertNotNull(fields);
		assertEquals(17, fields.size());

		Field f0 = fields.get(0);
		assertEquals("Identificadorticket", f0.getExcelName());
		assertEquals("reference_id", f0.getSolrName());
		assertEquals("integer", f0.getType());
		assertEquals(false, f0.getSkip().booleanValue());

		Field f16 = fields.get(16);
		assertEquals("Origen_Queja_2", f16.getExcelName());
		assertEquals("origen_queja", f16.getSolrName());
		assertEquals("string", f16.getType());
		assertEquals(false, f16.getSkip().booleanValue());
	}
}
