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
 *     Joaqu�n Garz�n - initial implementation
 *
 */
package com.opentext.explore.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestStringUtil{
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
