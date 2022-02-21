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
package com.opentext.explore.importer.trushpilot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.opentext.explore.importer.trushpilot.pojo.Author;
import com.opentext.explore.importer.trushpilot.pojo.Review;
import com.opentext.explore.importer.trushpilot.pojo.ReviewRating;

import junit.framework.TestCase;


public class TestTrustpilotTransformer extends TestCase {

	List<Review> reviews;
	
	private String docXMLFragment = 
			"  <doc>\r\n" + 
			"    <field name=\"reference_id\"><![CDATA[h10u12]]></field>\r\n" +
			"    <field name=\"interaction_id\"><![CDATA[h10u12]]></field>\r\n" +
			"    <field name=\"title\"><![CDATA[Any lawyers here willing to help the Canadian public mount a class action against Canada Post ?]]></field>\r\n" +
			"    <field name=\"author_name\"><![CDATA[Imaproholdmybeer]]></field>\r\n" + 
			"    <field name=\"ID\"><![CDATA[h10u12]]></field>\r\n" + 
			"    <field name=\"type\"><![CDATA[Reddit]]></field>\r\n" +
			"    <field name=\"published_date\"><![CDATA[2020-06-11T02:55:40Z]]></field>\r\n" + 
			"    <field name=\"date_time\"><![CDATA[2020-06-11T02:55:40Z]]></field>\r\n" + 				
			"    <field name=\"content\"><![CDATA[Hi,\n\nAs you may be aware of, millions of Canadians are experiencing absurd delays with their postal service.\n\nDuring this crisis it became obvious to me that a lot of packages were being delivered on time, while others that are following the same route are accruing late days.\n\nI believe that Canada Post performance management is directly responsible for this situation. It is a way to reduce the charge backs from corporate clients and normal consumers.\n\nA better performance management process would include multiple KPIs and would adapt to ensure that the average wait time of late packs does not exceed reasonable expectations. Canada Post choose not to have such measures in place. And now we're all paying the price.\n\nFurthermore, because of their improper measurement of their network, they had to offer overtime and proceed with new \"emergency hires\". I believe this, along with the fact that their network is overloaded and therefor their surveillance capacity probably diminished, will result in an unprecedented amount of missing / stolen packages.\n\nI believe the Canadian public deserves answer, and that Canada Post management must be made accountable.\n\nManaging Canada Post on behalf of Canadians is a privilege. This is OUR postal service and we entrusted it in the care of these people, now it's no longer working and their management is creating unfair situations and making the delays worse for everybody.\n\nLets do something.\n\n&amp;#x200B;\n\nEDIT: We should send them a legal notice that they need to deliver the packs in order or face legal actions for violation of Canada Post Corporation Act. You can see the laws I believe were violated in the comments below.\n\n&amp;#x200B;]]></field>\r\n" +
			"    <field name=\"subreddit\"><![CDATA[CanadaPost]]></field>\r\n" +
			"    <field name=\"score\"><![CDATA[196]]></field>\r\n" +
			"    <field name=\"permalink\"><![CDATA[/r/CanadaPost/comments/h10u12/any_lawyers_here_willing_to_help_the_canadian/]]></field>\r\n" +
			"    <field name=\"url\"><![CDATA[https://www.reddit.com/r/CanadaPost/comments/h10u12/any_lawyers_here_willing_to_help_the_canadian/]]></field>\r\n" +
			"    <field name=\"thumbnail\"><![CDATA[self]]></field>\r\n" +
			"    <field name=\"rtag\"><![CDATA[Canada Post]]></field>\r\n" +
			"  </doc>\r\n"; 
	
	@Before
	public void setUp() {
		Review review = mock(Review.class);
		when(review.generateId()).thenReturn(123456789l);	
		when(review.getHeadline()).thenReturn("Banco Sabadell's attitude & 'Customer�");		
		when(review.getDatePublished()).thenReturn("2022-02-18T12:46:25.000Z");		
		when(review.getReviewBody()).thenReturn("Banco Sabadell's attitude & 'Customer Care Service' is appalling. At today's date, Friday 18/02/2022 we have been advised of the bank's decision, that, after 3 months of correspondence back and forth detailing our situation, we have to present ourselves IN PERSON at our branch of the bank in Spain, if we wish to close our account and transfer the funds to our home bank in Ireland. This 'Customer Care Service' totally disregards our advanced ages, susceptibility to covid, advice against travel, cost of travel & insurance, accommodation, etc.Avoid Banco Sabadell.");
						
		Author author = mock(Author.class);
		when(review.getAuthor()).thenReturn(author);
		when(review.getAuthor().getName()).thenReturn("Michael Flick");	
				
		ReviewRating rating = mock(ReviewRating.class);
		when(review.getReviewRating()).thenReturn(rating);		
		when(review.getReviewRating().getRatingValue()).thenReturn("1");	
				
		reviews = new LinkedList<Review>();
		reviews.add(review);
	}
	
	@Test
	public void testStatusToString() {
		
		String xml = TrustpilotTransformer.reviewsToString(reviews, "Canada Post");
		
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
			TrustpilotTransformer.reviewsToXMLFile(reviews, outputXML, "Canada Post");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		File xml = new File(outputXML);
		assertTrue(xml.exists());
		
		//Remove test XML
		xml.delete();
	}
}
