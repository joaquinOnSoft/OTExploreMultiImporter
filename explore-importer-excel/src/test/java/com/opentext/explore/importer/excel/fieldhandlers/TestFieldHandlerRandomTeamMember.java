package com.opentext.explore.importer.excel.fieldhandlers;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class TestFieldHandlerRandomTeamMember extends AbstractTestFieldHandler {
	@Test
	public void testHandle() {	
		List<String> outputFields = new LinkedList<String>();
		outputFields.add("author_name");	
		
		FieldHandlerRandomTeamMember fhRandomTeamMember = new FieldHandlerRandomTeamMember();
		txtData = fhRandomTeamMember.handle(txtData, null, outputFields);
		
		String teamMember = txtData.getAuthorName();
		assertNotNull(teamMember);
		assertTrue(teamMember.contains(","));
	}
}
