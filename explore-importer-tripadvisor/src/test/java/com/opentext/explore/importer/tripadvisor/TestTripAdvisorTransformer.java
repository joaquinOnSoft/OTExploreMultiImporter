/*
 *   (C) Copyright 2022 OpenText and others.
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
package com.opentext.explore.importer.tripadvisor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.util.DateUtil;

import junit.framework.TestCase;


public class TestTripAdvisorTransformer extends TestCase {

	List<TAReview> reviews;
	
	private String docXMLFragment = 
			"  <doc>\r\n" + 
			"    <field name=\"language\"><![CDATA[en]]></field>\r\n" +
			"    <field name=\"sentiment\"><![CDATA[neutral]]></field>\r\n" +
			"    <field name=\"summary\"><![CDATA[Good for family]]></field>\r\n" +
			"    <field name=\"reference_id\"><![CDATA[857585531]]></field>\r\n" +
			"    <field name=\"interaction_id\"><![CDATA[857585531]]></field>\r\n" +
			"    <field name=\"title\"><![CDATA[Good for family]]></field>\r\n" +
			"    <field name=\"author_name\"><![CDATA[Renee Nien Yi P]]></field>\r\n" +
			"    <field name=\"ID\"><![CDATA[857585531]]></field>\r\n" +
			"    <field name=\"type\"><![CDATA[TripAdvisor]]></field>\r\n" +
			"    <field name=\"published_date\"><![CDATA[2022-08-01T00:00:00Z]]></field>\r\n" +
			"    <field name=\"date_time\"><![CDATA[2022-08-01T00:00:00Z]]></field>\r\n" +
			"    <field name=\"content\"><![CDATA[Great choice for family trip,  friendly sport / activity teams members that keep you occupied during your stay.  All inclusive with child care provided! Looking forward to join Club Med in different location.]]></field>\r\n" +
			"    <field name=\"ratingValue\"><![CDATA[5]]></field>\r\n" +
			"    <field name=\"ttag\"><![CDATA[TripAdvisor Review]]></field>\r\n" +
			"  </doc>\r\n"; 
	
	@Before
	public void setUp() {
		TAReview review = mock(TAReview.class);
		when(review.getId()).thenReturn("857585531");	
		when(review.getTitle()).thenReturn("Good for family");		
		try {
			when(review.getCreationDate()).thenReturn(DateUtil.strToDate("2022-08-01", "yyyy-MM-dd"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}		
		when(review.getContent()).thenReturn("Great choice for family trip,  friendly sport / activity teams members that keep you occupied during your stay.  All inclusive with child care provided! Looking forward to join Club Med in different location. ");
		when(review.getLanguage()).thenReturn("en");
								
		when(review.getAuthor()).thenReturn("Renee Nien Yi P");			
		when(review.getRating()).thenReturn(5);	
				
		reviews = new LinkedList<TAReview>();
		reviews.add(review);
	}
	
	@Test
	public void testStatusToString() {
		
		String xml = TripadvisorTransformer.reviewsToString(reviews, "TripAdvisor Review");
		
		String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<add>\r\n" +
				docXMLFragment +
				"</add>\r\n";
		
		assertEquals(expectedXML, xml);
	}
	
	@Test 
	public void testStatusToXMLFile() {
		String outputXML = "test_20220901_927653.xml";
				
		try {
			TripadvisorTransformer.reviewsToXMLFile(reviews, outputXML, "TrustPilot Review");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		File xml = new File(outputXML);
		assertTrue(xml.exists());
		
		//Remove test XML
		xml.delete();
	}
}
