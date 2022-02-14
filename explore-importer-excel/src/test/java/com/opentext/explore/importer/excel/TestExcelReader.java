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

import com.opentext.explore.importer.excel.fieldhandlers.IFieldHandler;
import com.opentext.explore.importer.excel.pojo.TextData;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;
import com.opentext.explore.util.FileUtil;

import junit.framework.TestCase;

public class TestExcelReader extends TestCase {
	private static final String CONTENT_TYPE_TICKET = "Ticket";
	
	private ExcelReader reader = new ExcelReader();
	private TextDataImporterMapping mapping;
	
	@Override
	protected void setUp() {
		File jsonConfigFile = FileUtil.getFileFromResources("excel_mapping.json");
		JSonMappingConfigReader jsonConfigReader = new JSonMappingConfigReader();
		mapping = jsonConfigReader.read(jsonConfigFile);	
	}
	
	@Test
	public void testReadNullPaht() {
		String excelFilePath = null;
		
		assertNotNull(mapping);
				
		List<TextData> txtDataList = reader.read(excelFilePath, mapping, CONTENT_TYPE_TICKET);
		assertNull(txtDataList);
	}	
	
	@Test
	public void testRead() {
		File excelFile = FileUtil.getFileFromResources("input_example.xlsx");
		
		assertNotNull(excelFile);
		assertNotNull(mapping);
				
		List<TextData> txtDataList = reader.read(excelFile, mapping, CONTENT_TYPE_TICKET);
		
		assertNotNull(txtDataList);
		assertTrue(txtDataList.size() > 0);
		assertEquals(3, txtDataList.size());
		
		//Check first row imported
		TextData txtData0 = txtDataList.get(0);
		assertEquals(CONTENT_TYPE_TICKET, txtData0.getType());	
		assertEquals("1583916", txtData0.getReferenceId());		
		assertEquals("1583916", txtData0.getInteractionId());
		assertEquals("1583916", txtData0.getId());
		assertEquals("1583916 Protección Hogar (OFERTABLE) Quejas de siniestros/prestaciones", txtData0.getTitle());		
		assertEquals("2020-12-01T00:00:00Z", txtData0.getPublishedDateAsString());
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer finibus leo purus, quis porttitor quam aliquet ut. " +
				"Curabitur ullamcorper erat at facilisis luctus. Nunc facilisis interdum vestibulum. Nulla auctor ante sed purus scelerisque, tincidunt sollicitudin erat vulputate. " +
				"Nam risus massa, ullamcorper a aliquam at, blandit in ante. Nam vestibulum, metus eu lacinia fermentum, felis massa venenatis leo, in ornare nibh eros eu odio. " +
				"Ut sit amet egestas enim. Quisque justo urna, porttitor sed condimentum vitae, malesuada quis arcu. Donec eget lacinia lacus. Duis sagittis id nisl eget porttitor. " +
				"Aliquam interdum vel ipsum ut blandit. Fusce sed eros a mi tincidunt malesuada." +
				" Fusce lobortis massa at volutpat vulputate. Praesent vulputate quam vel turpis pharetra, dignissim sodales leo lobortis. " + 
				"Etiam diam ex, mollis eu fringilla eget, finibus at lorem. Vivamus posuere neque magna, sodales maximus ligula placerat eu. " + 
				"Maecenas diam orci, viverra vitae aliquam id, egestas et lorem. Sed ut ultricies eros, id ullamcorper urna. " + 
				"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. " + 
				"Suspendisse ac mauris vel metus porta faucibus. Sed gravida est sit amet sapien venenatis, at fermentum lacus bibendum. " + 
				"Nullam quis magna et augue tempor semper sit amet eu nulla. Sed at elementum sapien. Sed lacinia vulputate mi. " + 
				"Sed metus risus, mattis vel vehicula ut, pellentesque vel dui.", 
				txtData0.getContent());		
		assertEquals("Demora del Reparador", txtData0.getField("motivo_queja"));
		
		//Check last row imported
		TextData txtData2 = txtDataList.get(2);
		assertEquals(CONTENT_TYPE_TICKET, txtData2.getType());	
		assertEquals("1584006", txtData2.getReferenceId());
		assertEquals("1584006", txtData2.getInteractionId());
		assertEquals("1584006", txtData2.getId());
		assertEquals("1584006 Protección Salud (No Ofertable) Quejas de siniestros/prestaciones", txtData2.getTitle());
		assertEquals("2020-12-01T00:00:00Z", txtData2.getPublishedDateAsString());
		assertEquals("12/3/20", txtData2.getField("fecha_cierre"));
		assertEquals("N", txtData2.getField("es_contactar"));
		assertEquals("7011", txtData2.getField("cpi"));
		assertEquals("Protección Salud (No Ofertable)", txtData2.getField("producto"));
		assertEquals("Quejas de siniestros/prestaciones", txtData2.getField("tipo_peticion"));
		assertEquals("Quisque laoreet scelerisque convallis. Aliquam porttitor finibus diam et mollis. " + 
				"Mauris maximus augue et maximus lacinia. Cras accumsan ullamcorper ex ac lobortis. Nulla vitae quam nulla. " + 
				"Maecenas id mauris augue. Maecenas eget enim euismod, consequat nunc sed, faucibus felis. " + 
				"Aliquam varius blandit semper. Integer pharetra ante turpis, feugiat ultrices arcu varius a. " + 
				"Vivamus sollicitudin ligula molestie, lobortis neque ut, ornare tortor. Nulla auctor varius sodales. ", 
				txtData2.getContent());
		assertEquals("NULL", txtData2.getField("motivo_queja"));
		
	}
	
	@Test
	public void testInstanciateFieldHandler() {		
		IFieldHandler instance = null;
		
		String javaClasses[] = {
				"com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerConcat",
				"com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerCopy"
		};
		
		for(String javaClass: javaClasses) {
			instance = reader.instanciateFieldHandler(javaClass);
			
			assertNotNull(instance);
		}
	}
}
