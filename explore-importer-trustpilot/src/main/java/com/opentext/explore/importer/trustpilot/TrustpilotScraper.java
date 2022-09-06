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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.explore.importer.http.URLReader;
import com.opentext.explore.importer.trustpilot.pojo.Graph;
import com.opentext.explore.importer.trustpilot.pojo.GraphType;
import com.opentext.explore.importer.trustpilot.pojo.TrustpilotReview;
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
		String pageURL = url;
		List<TrustpilotReview> reviews = null;
		TrustpilotReview reviewTmp = null;
		TrustpilotReviewContainer container = null;
		Document doc = null;

		do {	
			doc = readPage(pageURL);			
			container= getReviewsContainer(doc);

			if(container != null) {				
				GraphType type = null;
			
				for(Graph graph: container.getGraph()) {
					type = GraphType.getGraphTypeFromString(graph.getType());

					if(type != null) {
						if (type == GraphType.REVIEW) {
							reviewTmp = new TrustpilotReview();
							if (reviews == null) {
								reviews = new LinkedList<TrustpilotReview>();
							}
	
							reviewTmp.setType(graph.getType());
							reviewTmp.setId(graph.getId());
							reviewTmp.setAuthor(graph.getAuthor());
							reviewTmp.setDatePublished(graph.getDatePublished());
							reviewTmp.setHeadline(graph.getHeadline());
							reviewTmp.setReviewBody(graph.getReviewBody());
							reviewTmp.setReviewRating(graph.getReviewRating());
							reviewTmp.setPublisher(graph.getPublisher());
							reviewTmp.setInLanguage(graph.getInLanguage());
							
							reviews.add(reviewTmp);								
						}
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
			try {
				reviewContainer  = objectMapper.readValue(jsonStr, TrustpilotReviewContainer.class);			
			} catch (IOException e) {
				log.error(e.getMessage());
			}

			if(reviewContainer.getGraph() != null) {
				log.debug("# graphs in page: ", reviewContainer.getGraph().size());
			}
		}

		return reviewContainer;
	}
}
