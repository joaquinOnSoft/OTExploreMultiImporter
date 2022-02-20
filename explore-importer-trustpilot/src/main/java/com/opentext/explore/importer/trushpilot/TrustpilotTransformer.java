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
import com.opentext.explore.importer.trushpilot.pojo.Review;
import com.opentext.explore.util.Hash;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

/**
 * 
 * @author Joaquín Garzón
 * @since 22.02.16 
 */
public class TrustpilotTransformer extends AbstractTransformer {
	
	private static long generateId(Review review) {
		StringBuffer id = new StringBuffer();
		id.append(review.getAuthor().getName());
		id.append(review.getHeadline());
		id.append(review.getDatePublished());
		
		return Hash.hash(id.toString());		
	}
	
	private static Document reviewsToDoc(List<Review> reviews, String tag) {
		Document doc = null;
		long id = -1;
		
		if(reviews != null && reviews.size() > 0) {
			
			doc=new Document();
			//Root Element
			Element root=new Element("add");
			
			for (Review review : reviews) {
				Element eDoc = new Element("doc");
						
				id = generateId(review);

				//Field required since Qfiniti 20.4: START
				eDoc.addContent(createElementField("language", review.getInLanguage()));
				//TODO: Avoid hardcoding sentiment as neutral
				eDoc.addContent(createElementField("sentiment", "neutral"));				
				eDoc.addContent(createElementField("summary", review.getHeadline()));
				//Field required since Qfiniti 20.4: END				
				
				eDoc.addContent(createElementField("reference_id", id));
				eDoc.addContent(createElementField("interaction_id", id));
				eDoc.addContent(createElementField("title", review.getHeadline()));
				eDoc.addContent(createElementField("author_name", review.getAuthor().getName()));
				eDoc.addContent(createElementField("ID", id));
				eDoc.addContent(createElementField("type", "Review"));	
				eDoc.addContent(createElementField("published_date", review.getDatePublished()));
				eDoc.addContent(createElementField("date_time", review.getDatePublished()));
				eDoc.addContent(createElementField("content", new CDATA(review.getReviewBody())));				
				eDoc.addContent(createElementField("bestRating", review.getReviewRating().getBestRating()));
				eDoc.addContent(createElementField("worstRating", review.getReviewRating().getWorstRating()));				
				eDoc.addContent(createElementField("ratingValue", review.getReviewRating().getRatingValue()));
			
				eDoc.addContent(createElementField("ttag", tag));

				root.addContent(eDoc);
			}
			
			doc.addContent(root);
		}
		
		return doc;
	}	
	
	
	/**
	 * Generate a XML file using the given Reddit Submission 
	 * @param review - Reddit Submission
	 * @param fileName - File name of the XML that will be generated
	 * @return path of the XML file created
	 * @throws IOException
	 */	
	public static String reviewsToXMLFile(Review review, String fileName, String tag) throws IOException {
		List<Review> reviews = new LinkedList<Review>();
		reviews.add(review);
	
		return reviewsToXMLFile(reviews, fileName, tag);
	}	
	
	/**
	 * Generate a XML file using the given Reddit Submissions 
	 * @param reviews - List of reviews
	 * @param fileName - File name of the XML that will be generated
	 * @param tag - Tag to be added to the Submission
	 * @return Absolute path of the XML file created
	 * @throws IOException
	 */
	public static String reviewsToXMLFile(List<Review> reviews, String fileName, String tag) throws IOException {
		String xmlPath = null;
		Document doc = reviewsToDoc(reviews, tag);

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
	public static String reviewsToString(Listing<Submission> posts, String tag) {
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
