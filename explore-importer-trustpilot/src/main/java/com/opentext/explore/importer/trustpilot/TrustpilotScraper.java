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
package com.opentext.explore.importer.trustpilot;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.opentext.explore.importer.http.URLReader;
import com.opentext.explore.importer.trustpilot.pojo.ContactPoint;
import com.opentext.explore.importer.trustpilot.pojo.ContactPointDeserializer;
import com.opentext.explore.importer.trustpilot.pojo.Graph;
import com.opentext.explore.importer.trustpilot.pojo.ItemListElement;
import com.opentext.explore.importer.trustpilot.pojo.ItemListElementDeserializer;
import com.opentext.explore.importer.trustpilot.pojo.TrustpilotReviewContainer;

public class TrustpilotScraper {

	private static final Logger log = LogManager.getLogger(TrustpilotScraper.class);

	private static final String BASE_URL="https://www.trustpilot.com";

	private static final String REVIEW_URL="/review/";

	private String url;	
	private String urlBase;

	/**
	 * Initialize trustpilot.com review page scraper 
	 * @param clientAlias - Client alias in <strong>trustpilot.com</strong>
	 * <i>Example</i>: bancsabadell.com
	 */
	public TrustpilotScraper(String clientAlias) {
		this(BASE_URL, clientAlias);
	}

	/**
	 * Initialize trustpilot.com review page scraper 
	 * @param urlBase - URL base
	 * <i>Examples</i>: 
	 * <ul>
	 *    <li>https://www.trustpilot.com</li>
	 *    <li>https://es.trustpilot.com</li>    
	 * </ul>
	 * @param clienteAlias - Client alias in <strong>trustpilot.com</strong>
	 * <i>Example</i>: bancsabadell.com
	 * 
	 */
	public TrustpilotScraper(String urlBase, String clientAlias) {
		if(urlBase != null) {
			this.urlBase = urlBase;
			this.url = urlBase + REVIEW_URL + clientAlias;
		}
	}

	public List<TrustpilotReview> getReviews(){
		int pageNum = 1;
		String pageURL = url;
		List<TrustpilotReview> reviews = null;
		List<TrustpilotReview> reviewsTmp = null;
		TrustpilotReview review = null;
		TrustpilotReviewContainer reviewContainer = null;
		Document doc = null;

		do {	
			doc = readPage(pageURL);			
			reviewContainer= getReviewsContainer(doc);
			
			if(reviewContainer != null) {
				List<Graph> graphs = reviewContainer.getGraph();
				
				if(graphs != null && graphs.size() > 0) {
					reviewsTmp = new LinkedList<TrustpilotReview>();					
					String type = null;
					
					for(Graph graph: graphs) {
						type = graph.getType();
						
						if(type != null && type.compareToIgnoreCase("Review") == 0) {
							review = new TrustpilotReview();
							review.setId(graph.getId());
							review.setAuthor(graph.getAuthor().getName());
							try {
								review.setDatePublished(graph.getDatePublished());
							} catch (ParseException e) {
								log.error("Invalid date format: " + e.getLocalizedMessage());
							}
							review.setHeadline(graph.getHeadline());
							review.setReviewBody(graph.getReviewBody());
							review.setRating(graph.getReviewRating().getRatingValue());
							review.setLanguage(graph.getInLanguage());
							
							reviewsTmp.add(review);
						}
					}
					
					if(reviewsTmp != null && reviewsTmp.size() > 0) {
						if(reviews == null) {
							reviews = new LinkedList<TrustpilotReview>();
						}
						
						log.debug("Adding review from page: " + pageNum++);
						reviews.addAll(reviewsTmp);
					}
				}
			}

			pageURL = getNextPageURL(doc);
		}
		while(pageURL != null);

		return reviews;
	}

	private Document readPage(String pageURL) {
		log.info("Reading page: " + pageURL);

		URLReader reader = new URLReader(pageURL);		
		return Jsoup.parse(reader.read());
	}

	private String getNextPageURL(Document doc) {
		String nextPageURL = null;

		Element link = doc.select("a[name=pagination-button-next]").first();

		if(link != null) {
			String href = link.attr("href");
			if (href != null) {
				if(href.startsWith("http")) {
					nextPageURL = href;
				}
				else {
					nextPageURL = urlBase + href;
				}
			}
		}
		return nextPageURL;
	}

	/**
	 * Read the given page and return the embedded JSON including the Trushpilot review
	 * @param pageURL - Trustpilot page URL
	 * @return 
	 */
	private TrustpilotReviewContainer getReviewsContainer(Document doc) {
		TrustpilotReviewContainer reviewContainer = null;

		Element script = doc.select("script[data-business-unit-json-ld=true]").first();

		if(script != null) {
			String jsonStr = script.html();
			log.debug("JSON: " + jsonStr);	

			ObjectMapper objectMapper = new ObjectMapper();
			
			// Getting Started with Custom Deserialization in Jackson
			// https://www.baeldung.com/jackson-deserialization
			SimpleModule moduleContactPoint = new SimpleModule();
			moduleContactPoint.addDeserializer(ContactPoint.class, new ContactPointDeserializer());
			objectMapper.registerModule(moduleContactPoint);

			SimpleModule moduleItemListElement = new SimpleModule();
			moduleItemListElement.addDeserializer(ItemListElement.class, new ItemListElementDeserializer());
			objectMapper.registerModule(moduleItemListElement);			
			
			try {
				reviewContainer  = objectMapper.readValue(jsonStr, TrustpilotReviewContainer.class);			
			} catch (IOException e) {
				log.error(e.getMessage());
			}

			log.debug(reviewContainer);
		}

		return reviewContainer;
	}
}
