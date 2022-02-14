package com.opentext.explore.importer.excel.fieldhandlers;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class TestFieldHandlerConcat extends AbstractTestFieldHandler {
	@Test
	public void testHandle() {	
		List<String> inputFields = new LinkedList<String>();
		inputFields.add("content");
		inputFields.add("ComentariosOficina");
		
		List<String> outputFields = new LinkedList<String>();
		outputFields.add("content");	
		
		FieldHandlerConcat fhConcat = new FieldHandlerConcat();
		txtData = fhConcat.handle(txtData, inputFields, outputFields);
		
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer finibus leo purus, quis porttitor quam aliquet ut.", txtData.getContent());
	}
}
