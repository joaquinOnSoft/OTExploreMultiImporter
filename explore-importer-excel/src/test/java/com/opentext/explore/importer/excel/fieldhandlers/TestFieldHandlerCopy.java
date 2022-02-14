package com.opentext.explore.importer.excel.fieldhandlers;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class TestFieldHandlerCopy extends AbstractTestFieldHandler {
	FieldHandlerCopy fhCopy = new FieldHandlerCopy();
	
	@Test
	public void testGetFieldValueByName() {
		String value = fhCopy.getFieldValueByName(txtData, "reference_id");
		
		assertEquals("1583916", value);
		
		value = fhCopy.getFieldValueByName(txtData, "interaction_id");		
		assertEquals("1583916", value);
		
		value = fhCopy.getFieldValueByName(txtData, "ID");		
		assertEquals("1583916", value);				
	}	

	@Test
	public void testSetFieldValueByName() {
		String value = fhCopy.getFieldValueByName(txtData, "reference_id");
		
		assertEquals("1583916", value);
		
		txtData = fhCopy.setFieldValueByName(txtData, "reference_id", "9999999");		
		assertEquals("9999999", txtData.getReferenceId());		
	}		
	
	@Test
	public void testHandle() {
		List<String> inputFields = new LinkedList<String>();
		inputFields.add("reference_id");
		
		List<String> outputFields = new LinkedList<String>();
		outputFields.add("interaction_id");
		outputFields.add("ID");	
				
		txtData = fhCopy.handle(txtData, inputFields, outputFields);
		
		assertEquals("1583916", txtData.getReferenceId());		
		assertEquals("1583916", txtData.getInteractionId());
		assertEquals("1583916", txtData.getId());
	}
}
