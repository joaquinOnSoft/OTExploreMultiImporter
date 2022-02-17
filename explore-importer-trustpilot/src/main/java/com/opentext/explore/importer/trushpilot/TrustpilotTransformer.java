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
 *     Joaquín Garzón - initial implementation
 *
 */
package com.opentext.explore.importer.trushpilot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.opentext.explore.importer.AbstractTransformer;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

/**
 * 
 * @author Joaquín Garzón
 * @since 22.02.16 
 */
public class TrustpilotTransformer extends AbstractTransformer {
	
	private static Document submissionsToDoc(Listing<Submission> posts, String tag) {
		Document doc = null;
		
		if(posts != null && posts.size() > 0) {
			
			doc=new Document();
			//Root Element
			Element root=new Element("add");
			
			for (Submission post : posts) {
				Element eDoc = new Element("doc");
							
				eDoc.addContent(createElementField("reference_id", post.getId()));
				eDoc.addContent(createElementField("interaction_id", post.getId()));
				eDoc.addContent(createElementField("title", post.getTitle()));
				eDoc.addContent(createElementField("author_name", post.getAuthor()));
				eDoc.addContent(createElementField("ID", post.getId()));
				eDoc.addContent(createElementField("type", "Reddit"));	
				eDoc.addContent(createElementField("published_date", post.getCreated()));
				eDoc.addContent(createElementField("date_time", post.getCreated()));
				eDoc.addContent(createElementField("content", new CDATA(post.getSelfText())));				
				eDoc.addContent(createElementField("subreddit", post.getSubreddit()));
				eDoc.addContent(createElementField("score", post.getScore()));				
				eDoc.addContent(createElementField("permalink", post.getPermalink()));
				eDoc.addContent(createElementField("url", post.getUrl()));				
				eDoc.addContent(createElementField("thumbnail", post.getThumbnail()));
			
				eDoc.addContent(createElementField("rtag", tag));

				root.addContent(eDoc);
			}
			
			doc.addContent(root);
		}
		
		return doc;
	}	
	
	
	/**
	 * Generate a XML file using the given Reddit Submission 
	 * @param post - Reddit Submission
	 * @param fileName - File name of the XML that will be generated
	 * @return path of the XML file created
	 * @throws IOException
	 */	
	public static String submissionToXMLFile(Submission post, String fileName, String tag) throws IOException {
		List<Submission> tempPosts = new LinkedList<Submission>();
		tempPosts.add(post);
		
		Listing<Submission> posts = Listing.create(null, tempPosts);
		
		return submissionsToXMLFile(posts, fileName, tag);
	}	
	
	/**
	 * Generate a XML file using the given Reddit Submissions 
	 * @param posts - List of Reddit Submissions
	 * @param fileName - File name of the XML that will be generated
	 * @param tag - Tag to be added to the Submission
	 * @return Absolute path of the XML file created
	 * @throws IOException
	 */
	public static String submissionsToXMLFile(Listing<Submission> posts, String fileName, String tag) throws IOException {
		String xmlPath = null;
		Document doc = submissionsToDoc(posts, tag);

		if(doc != null) {
			//Create the XML
			XMLOutputter outter=new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());
			
			File xmlFile = new File(fileName);
			xmlPath = xmlFile.getAbsolutePath();
			FileWriter fWriter = new FileWriter(xmlFile, StandardCharsets.UTF_8);
			
			outter.output(doc, fWriter);
			
			fWriter.close();
		}		
		
		return xmlPath;		
	}	
	
	/**
	 * Generate a XML string using the given Reddit submission
	 * SEE: How to create XML file with specific structure in Java 
	 * https://stackoverflow.com/questions/23520208/how-to-create-xml-file-with-specific-structure-in-java
	 * @param statuses
	 * @return
	 */
	public static String submissionsToString(Listing<Submission> posts, String tag) {
		String xml = null;
		Document doc = submissionsToDoc(posts, tag);

		if(doc != null) {
			//Create the XML
			XMLOutputter outter=new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());			
			xml = outter.outputString(doc);
		}
		
		return xml;
	}
}
