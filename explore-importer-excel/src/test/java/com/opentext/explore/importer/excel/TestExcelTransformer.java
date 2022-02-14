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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.opentext.explore.importer.excel.pojo.TextData;
import com.opentext.explore.util.DateUtil;

import junit.framework.TestCase;


public class TestExcelTransformer extends TestCase {

	private List<TextData> txtDatas;
	
	private String docXMLFragment = 
			"  <doc>\r\n" +
			"    <field name=\"reference_id\"><![CDATA[1583916]]></field>\r\n" +
			"    <field name=\"interaction_id\"><![CDATA[1583916]]></field>\r\n" +
			"    <field name=\"title\"><![CDATA[1583916 Quejas de siniestros/prestaciones]]></field>\r\n" +
			"    <field name=\"author_name\"><![CDATA[ExcelImporter]]></field>\r\n" +
			"    <field name=\"ID\"><![CDATA[1583916]]></field>\r\n" +
			"    <field name=\"type\"><![CDATA[Ticketing]]></field>\r\n" +
			"    <field name=\"published_date\"><![CDATA[2020-12-01T00:00:00Z]]></field>\r\n" +
			"    <field name=\"date_time\"><![CDATA[2020-12-01T00:00:00Z]]></field>\r\n" +
			"    <field name=\"content\"><![CDATA[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer finibus leo purus, quis porttitor quam aliquet ut. Curabitur ullamcorper erat at facilisis luctus. Nunc facilisis interdum vestibulum. Nulla auctor ante sed purus scelerisque, tincidunt sollicitudin erat vulputate. Nam risus massa, ullamcorper a aliquam at, blandit in ante. Nam vestibulum, metus eu lacinia fermentum, felis massa venenatis leo, in ornare nibh eros eu odio. Ut sit amet egestas enim. Quisque justo urna, porttitor sed condimentum vitae, malesuada quis arcu. Donec eget lacinia lacus. Duis sagittis id nisl eget porttitor. Aliquam interdum vel ipsum ut blandit. Fusce sed eros a mi tincidunt malesuada.]]></field>\r\n" +
			"    <field name=\"EsContactar\"><![CDATA[S]]></field>\r\n" +
			"    <field name=\"ComentariosOficina\"><![CDATA[Fusce lobortis massa at volutpat vulputate. Praesent vulputate quam vel turpis pharetra, dignissim sodales leo lobortis. Etiam diam ex, mollis eu fringilla eget, finibus at lorem. Vivamus posuere neque magna, sodales maximus ligula placerat eu. Maecenas diam orci, viverra vitae aliquam id, egestas et lorem. Sed ut ultricies eros, id ullamcorper urna. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Suspendisse ac mauris vel metus porta faucibus. Sed gravida est sit amet sapien venenatis, at fermentum lacus bibendum. Nullam quis magna et augue tempor semper sit amet eu nulla. Sed at elementum sapien. Sed lacinia vulputate mi. Sed metus risus, mattis vel vehicula ut, pellentesque vel dui.]]></field>\r\n" +
			"    <field name=\"TipoPeticion\"><![CDATA[Quejas de siniestros/prestaciones]]></field>\r\n" +
			"    <field name=\"reference_id\"><![CDATA[1583916]]></field>\r\n" +
			"    <field name=\"Producto\"><![CDATA[Protección Hogar (OFERTABLE)]]></field>\r\n" +
			"    <field name=\"Familia\"><![CDATA[Protección Diversos]]></field>\r\n" +
			"    <field name=\"OrigenQueja\"><![CDATA[NULL]]></field>\r\n" +
			"    <field name=\"Departamento\"><![CDATA[Quejas y reclamaciones Siniestros BSSG]]></field>\r\n" +
			"    <field name=\"MotivoQueja\"><![CDATA[Demora del Reparador]]></field>\r\n" +
			"    <field name=\"CPI\"><![CDATA[201]]></field>\r\n" +
			"    <field name=\"FechaCierre\"><![CDATA[01/12/2020]]></field>\r\n" +
			"    <field name=\"Estado\"><![CDATA[finalizada]]></field>\r\n" +
			"    <field name=\"etag\"><![CDATA[Insurance]]></field>\r\n" +
			"  </doc>\r\n";


			
	@Before
	public void setUp() {
		TextData txtData = mock(TextData.class);
		when(txtData.getId()).thenReturn("1583916");
		when(txtData.getReferenceId()).thenReturn("1583916");	
		when(txtData.getInteractionId()).thenReturn("1583916");
		when(txtData.getAuthorName()).thenReturn("ExcelImporter");
		when(txtData.getTitle()).thenReturn("1583916 Quejas de siniestros/prestaciones");
		when(txtData.getType()).thenReturn("Ticketing");
		when(txtData.getPublishedDateAsString()).thenReturn("2020-12-01T00:00:00Z");	
		when(txtData.getDateTimeAsString()).thenReturn("2020-12-01T00:00:00Z");
		when(txtData.getContent()).thenReturn("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer finibus leo purus, quis porttitor quam aliquet ut. Curabitur ullamcorper erat at facilisis luctus. Nunc facilisis interdum vestibulum. Nulla auctor ante sed purus scelerisque, tincidunt sollicitudin erat vulputate. Nam risus massa, ullamcorper a aliquam at, blandit in ante. Nam vestibulum, metus eu lacinia fermentum, felis massa venenatis leo, in ornare nibh eros eu odio. Ut sit amet egestas enim. Quisque justo urna, porttitor sed condimentum vitae, malesuada quis arcu. Donec eget lacinia lacus. Duis sagittis id nisl eget porttitor. Aliquam interdum vel ipsum ut blandit. Fusce sed eros a mi tincidunt malesuada.");	
		try {
			when(txtData.getPublishedDate()).thenReturn(DateUtil.utcToDate("2020-12-01T00:00:00Z"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
				
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("reference_id", "1583916");
		fields.put("FechaCierre", "01/12/2020");
		fields.put("EsContactar", "S");
		fields.put("CPI", "201");
		fields.put("Producto", "Protección Hogar (OFERTABLE)");
		fields.put("TipoPeticion", "Quejas de siniestros/prestaciones");
		fields.put("ComentariosOficina", "Fusce lobortis massa at volutpat vulputate. Praesent vulputate quam vel turpis pharetra, dignissim sodales leo lobortis. Etiam diam ex, mollis eu fringilla eget, finibus at lorem. Vivamus posuere neque magna, sodales maximus ligula placerat eu. Maecenas diam orci, viverra vitae aliquam id, egestas et lorem. Sed ut ultricies eros, id ullamcorper urna. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Suspendisse ac mauris vel metus porta faucibus. Sed gravida est sit amet sapien venenatis, at fermentum lacus bibendum. Nullam quis magna et augue tempor semper sit amet eu nulla. Sed at elementum sapien. Sed lacinia vulputate mi. Sed metus risus, mattis vel vehicula ut, pellentesque vel dui.");
		fields.put("Departamento", "Quejas y reclamaciones Siniestros BSSG");
		fields.put("Estado", "finalizada");
		fields.put("CPI", "201");
		fields.put("Familia", "Protección Diversos");
		fields.put("Producto", "Protección Hogar (OFERTABLE)");		
		fields.put("TipoPeticion", "Quejas de siniestros/prestaciones");
		fields.put("MotivoQueja", "Demora del Reparador");
		fields.put("OrigenQueja", "NULL");
									
		when(txtData.getFields()).thenReturn(fields);	
				
		txtDatas= new LinkedList<TextData>();	
		txtDatas.add(txtData);
	}
	
	@Test
	public void testTextDataToString() {
		
		String xml = ExcelTransformer.textDataToString(txtDatas, "Insurance");
		
		String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<add>\r\n" +
				docXMLFragment +
				"</add>\r\n";
		
		assertEquals(expectedXML, xml);
	}
	
	@Test 
	public void testStatusToXMLFile() {
		String outputXML = "test_h10u12.xml";
				
		try {
			ExcelTransformer.textDatasToXMLFile(txtDatas, outputXML, "Insurance");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		File xml = new File(outputXML);
		assertTrue(xml.exists());
		
		//Remove test XML
		xml.delete();
	}	
}
