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
import com.opentext.explore.importer.trushpilot.pojo.Review;
import com.opentext.explore.importer.trushpilot.pojo.TrustpilotReviewContainer;
import com.opentext.explore.importer.trushpilot.pojo.TrustpilotReviewContainerType;

public class TrustpilotScraper {

	private static final Logger log = LogManager.getLogger(TrustpilotScraper.class);

	private static final String BASE_URL="https://www.trustpilot.com";

	private static final String REVIEW_URL="/review/";

	private String url;	

	/**
	 * Initialize trustpilot.com review page scraper 
	 * @param clientAlias - Client alias in <strong>trustpilot.com</strong>
	 * <i>Example</i>: bancsabadell.com
	 */
	public TrustpilotScraper(String clientAlias) {
		this(BASE_URL + REVIEW_URL, clientAlias);
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
			if(urlBase.contains(REVIEW_URL)) {
				this.url = urlBase + clientAlias;
			}
			else {
				this.url = urlBase + REVIEW_URL + clientAlias;
			}
		}
	}

	public List<Review> getReviews(){
		String pageURL = url;
		List<Review> reviews = null;
		List<Review> reviewsTmp = null;
		TrustpilotReviewContainer[] reviewContainers = null;
		Document doc = null;

		do {	
			doc = readPage(pageURL);			
			reviewContainers= getReviewsContainer(doc);

			TrustpilotReviewContainerType type = null;
			for (TrustpilotReviewContainer container : reviewContainers) {

				type = TrustpilotReviewContainerType.getContainerTypeFromString(container.getType());

				switch(type) {
				case LOCAL_BUSINESS:
					reviewsTmp = container.getReview();
					if(reviewsTmp != null && reviewsTmp.size() > 0 ) {
						if (reviews == null) {
							reviews = new LinkedList<Review>();
						}

						reviews.addAll(reviewsTmp);
					}
					break;
				case BREADCRUMB_LIST:
					//Intentionally empty
				case DATASET:
					//Intentionally empty
					break;	
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
					nextPageURL = BASE_URL + href;
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
	private TrustpilotReviewContainer[] getReviewsContainer(Document doc) {
		TrustpilotReviewContainer[] reviewContainer = null;

		Element script = doc.select("script[data-business-unit-json-ld=true]").first();

		if(script != null) {
			String jsonStr = script.html();
			log.debug("JSON: " + jsonStr);	



			ObjectMapper objectMapper = new ObjectMapper();
			try {
				reviewContainer  = objectMapper.readValue(jsonStr, TrustpilotReviewContainer[].class);			
			} catch (IOException e) {
				log.error(e.getMessage());
			}

			log.debug(reviewContainer);
		}

		return reviewContainer;
	}
}
