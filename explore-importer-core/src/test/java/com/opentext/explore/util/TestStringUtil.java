package com.opentext.explore.util;

import org.junit.Test;

import junit.framework.TestCase;

public class TestStringUtil extends TestCase{
	@Test
	public void testStringToArrayString() {
		String[] array = StringUtil.stringToArrayString("Hola,Adios");
		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("Hola", array[0]);
		assertEquals("Adios", array[1]);
	}
	
	@Test
	public void testCamelCaseToHumanReadable() {
		String str = StringUtil.camelCaseToHumanReadable("ISeeYou");		
		assertEquals("I See You", str);
		
		str = StringUtil.camelCaseToHumanReadable("WasUponATime");
		assertEquals("Was Upon A Time", str);
		
		str = StringUtil.camelCaseToHumanReadable("FarOutInTheUnchartedBackwatersOfTheUnfashionableEndOfTheWesternSpiral");
		assertEquals("Far Out In The Uncharted Backwaters Of The Unfashionable End Of The Western Spiral", str);		
	}
	
	@Test
	public void testSnakelCaseToHumanReadable() {
		String str = StringUtil.snakeCaseToHumanReadable("I_see_you");		
		assertEquals("I see you", str);
		
		str = StringUtil.snakeCaseToHumanReadable("Was_Upon_A_Time");
		assertEquals("Was Upon A Time", str);
		
		str = StringUtil.snakeCaseToHumanReadable("Far_Out_In_The_Uncharted_Backwaters_Of_The_Unfashionable_End_Of_The_Western_Spiral");
		assertEquals("Far Out In The Uncharted Backwaters Of The Unfashionable End Of The Western Spiral", str);		
	}	
}
