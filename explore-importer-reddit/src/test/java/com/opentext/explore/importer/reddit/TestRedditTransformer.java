package com.opentext.explore.importer.reddit;

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
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;


public class TestRedditTransformer extends TestCase {

	Listing<Submission> posts;
	
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
		Submission post = mock(Submission.class);
		when(post.getId()).thenReturn("h10u12");	
		when(post.getTitle()).thenReturn("Any lawyers here willing to help the Canadian public mount a class action against Canada Post ?");	
		when(post.getAuthor()).thenReturn("Imaproholdmybeer");	
		try {
			when(post.getCreated()).thenReturn(DateUtil.utcToDate("2020-06-11T02:55:40Z"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
		when(post.getSelfText()).thenReturn("Hi,\n\nAs you may be aware of, millions of Canadians are experiencing absurd delays with their postal service.\n\nDuring this crisis it became obvious to me that a lot of packages were being delivered on time, while others that are following the same route are accruing late days.\n\nI believe that Canada Post performance management is directly responsible for this situation. It is a way to reduce the charge backs from corporate clients and normal consumers.\n\nA better performance management process would include multiple KPIs and would adapt to ensure that the average wait time of late packs does not exceed reasonable expectations. Canada Post choose not to have such measures in place. And now we're all paying the price.\n\nFurthermore, because of their improper measurement of their network, they had to offer overtime and proceed with new \"emergency hires\". I believe this, along with the fact that their network is overloaded and therefor their surveillance capacity probably diminished, will result in an unprecedented amount of missing / stolen packages.\n\nI believe the Canadian public deserves answer, and that Canada Post management must be made accountable.\n\nManaging Canada Post on behalf of Canadians is a privilege. This is OUR postal service and we entrusted it in the care of these people, now it's no longer working and their management is creating unfair situations and making the delays worse for everybody.\n\nLets do something.\n\n&amp;#x200B;\n\nEDIT: We should send them a legal notice that they need to deliver the packs in order or face legal actions for violation of Canada Post Corporation Act. You can see the laws I believe were violated in the comments below.\n\n&amp;#x200B;");	
		when(post.getSubreddit()).thenReturn("CanadaPost");	
		when(post.getScore()).thenReturn(196);	
		when(post.getPermalink()).thenReturn("/r/CanadaPost/comments/h10u12/any_lawyers_here_willing_to_help_the_canadian/");	
		when(post.getUrl()).thenReturn("https://www.reddit.com/r/CanadaPost/comments/h10u12/any_lawyers_here_willing_to_help_the_canadian/");	
		when(post.getThumbnail()).thenReturn("self");	
		
		
		List<Submission> tempPosts = new LinkedList<Submission>();
		tempPosts.add(post);
		posts = (Listing<Submission>) Listing.create(null, tempPosts);
	}
	
	@Test
	public void testStatusToString() {
		
		String xml = RedditTransformer.submissionsToString(posts, "Canada Post");
		
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
			RedditTransformer.submissionsToXMLFile(posts, outputXML, "Canada Post");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		File xml = new File(outputXML);
		assertTrue(xml.exists());
		
		//Remove test XML
		xml.delete();
	}
}
