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
package com.opentext.explore.importer.trustpilot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.opentext.explore.util.DateUtil;

import junit.framework.TestCase;


public class TestTrustpilotTransformer extends TestCase {

	List<TrustpilotReview> reviews;
	
	private String docXMLFragment = 
			"  <doc>\r\n" + 
			"    <field name=\"language\"><![CDATA[en]]></field>\r\n" +
			"    <field name=\"sentiment\"><![CDATA[neutral]]></field>\r\n" +
			"    <field name=\"summary\"><![CDATA[Banco Sabadell's attitude & 'Customer�]]></field>\r\n" +
			"    <field name=\"reference_id\"><![CDATA[https://www.trustpilot.com/#/schema/Review/www.sabadell.com/5fb22f7e5e693f0acce99fc6]]></field>\r\n" +
			"    <field name=\"interaction_id\"><![CDATA[https://www.trustpilot.com/#/schema/Review/www.sabadell.com/5fb22f7e5e693f0acce99fc6]]></field>\r\n" +
			"    <field name=\"title\"><![CDATA[Banco Sabadell's attitude & 'Customer�]]></field>\r\n" +
			"    <field name=\"author_name\"><![CDATA[Michael Flick]]></field>\r\n" +
			"    <field name=\"ID\"><![CDATA[https://www.trustpilot.com/#/schema/Review/www.sabadell.com/5fb22f7e5e693f0acce99fc6]]></field>\r\n" +
			"    <field name=\"type\"><![CDATA[Trustpilot]]></field>\r\n" +
			"    <field name=\"published_date\"><![CDATA[2022-02-18T12:46:25Z]]></field>\r\n" +
			"    <field name=\"date_time\"><![CDATA[2022-02-18T12:46:25Z]]></field>\r\n" +
			"    <field name=\"content\"><![CDATA[Banco Sabadell's attitude & 'Customer Care Service' is appalling. At today's date, Friday 18/02/2022 we have been advised of the bank's decision, that, after 3 months of correspondence back and forth detailing our situation, we have to present ourselves IN PERSON at our branch of the bank in Spain, if we wish to close our account and transfer the funds to our home bank in Ireland. This 'Customer Care Service' totally disregards our advanced ages, susceptibility to covid, advice against travel, cost of travel & insurance, accommodation, etc.Avoid Banco Sabadell.]]></field>\r\n" +
			"    <field name=\"ratingValue\"><![CDATA[1]]></field>\r\n" +
			"    <field name=\"ttag\"><![CDATA[TrustPilot Review]]></field>\r\n" +
			"  </doc>\r\n"; 
	
	@Before
	public void setUp() {
		TrustpilotReview review = mock(TrustpilotReview.class);
		
		when(review.getId()).thenReturn("https://www.trustpilot.com/#/schema/Review/www.sabadell.com/5fb22f7e5e693f0acce99fc6");	
		when(review.getHeadline()).thenReturn("Banco Sabadell's attitude & 'Customer�");		
		try {
			when(review.getDatePublished()).thenReturn(DateUtil.strToDate("2022-02-18T12:46:25.000Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		} catch (ParseException e) {
			fail(e.getLocalizedMessage());
		}		
		when(review.getReviewBody()).thenReturn("Banco Sabadell's attitude & 'Customer Care Service' is appalling. At today's date, Friday 18/02/2022 we have been advised of the bank's decision, that, after 3 months of correspondence back and forth detailing our situation, we have to present ourselves IN PERSON at our branch of the bank in Spain, if we wish to close our account and transfer the funds to our home bank in Ireland. This 'Customer Care Service' totally disregards our advanced ages, susceptibility to covid, advice against travel, cost of travel & insurance, accommodation, etc.Avoid Banco Sabadell.");
		when(review.getLanguage()).thenReturn("en");							
		when(review.getAuthor()).thenReturn("Michael Flick");						
		when(review.getRating()).thenReturn(1);
		
		reviews = new LinkedList<TrustpilotReview>();
		reviews.add(review);
	}
	
	@Test
	public void testStatusToString() {
		
		String xml = TrustpilotTransformer.reviewsToString(reviews, "TrustPilot Review");
		
		String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<add>\r\n" +
				docXMLFragment +
				"</add>\r\n";
		
		assertEquals(expectedXML, xml);
	}
	
	@Test 
	public void testStatusToXMLFile() {
		String outputXML = "test_20220221_927653.xml";
				
		try {
			TrustpilotTransformer.reviewsToXMLFile(reviews, outputXML, "TrustPilot Review");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		File xml = new File(outputXML);
		assertTrue(xml.exists());
		
		//Remove test XML
		xml.delete();
	}
}
