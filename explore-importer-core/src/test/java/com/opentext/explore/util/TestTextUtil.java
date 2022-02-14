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
package com.opentext.explore.util;

import org.junit.Test;

import junit.framework.TestCase;

public class TestTextUtil extends TestCase {
	@Test
	public void testCamelCaseToHumanReadable() {
		String str = TextUtil.camelCaseToHumanReadable("ISeeYou");		
		assertEquals("I See You", str);
		
		str = TextUtil.camelCaseToHumanReadable("WasUponATime");
		assertEquals("Was Upon A Time", str);
		
		str=TextUtil.camelCaseToHumanReadable("FarOutInTheUnchartedBackwatersOfTheUnfashionableEndOfTheWesternSpiral");
		assertEquals("Far Out In The Uncharted Backwaters Of The Unfashionable End Of The Western Spiral", str);		
	}
	
	@Test
	public void testSnakelCaseToHumanReadable() {
		String str = TextUtil.snakeCaseToHumanReadable("I_see_you");		
		assertEquals("I see you", str);
		
		str = TextUtil.snakeCaseToHumanReadable("Was_Upon_A_Time");
		assertEquals("Was Upon A Time", str);
		
		str=TextUtil.snakeCaseToHumanReadable("Far_Out_In_The_Uncharted_Backwaters_Of_The_Unfashionable_End_Of_The_Western_Spiral");
		assertEquals("Far Out In The Uncharted Backwaters Of The Unfashionable End Of The Western Spiral", str);		
	}	
}
