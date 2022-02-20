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

import java.io.IOException;
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

public class TrustpilotScraper {

	private static final Logger log = LogManager.getLogger(TrustpilotScraper.class);

	private static final String BASE_URL="https://www.trustpilot.com/review/";
	
	private String url;	
	
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
	 * <i>Example</i>: https://www.trustpilot.com/review/
	 * @param clienteAlias - Client alias in <strong>trustpilot.com</strong>
	 * <i>Example</i>: bancsabadell.com
	 * 
	 */
	public TrustpilotScraper(String urlBase, String clientAlias) {
		this.url = urlBase + clientAlias;
	}
	
	public List<Review> getReviews(){
		String pageURL = url;
		List<Review> reviews = null;
		
		
		do {
			TrustpilotReviewContainer[] reviewContainer= readPage(pageURL);
			pageURL = null;
			
			// https://www.trustpilot.com/review/bancsabadell.com?page=2
		}
		while(pageURL != null);
		
		return reviews;
	}

	/**
	 * Read the given page and return the embedded JSON including the Trushpilot review
	 * @param pageURL - Trustpilot page URL
	 * @return 
	 */
	private TrustpilotReviewContainer[] readPage(String pageURL) {
		TrustpilotReviewContainer[] reviewContainer = null;
		
		log.info("Reading page: " + pageURL);
		
		URLReader reader = new URLReader(pageURL);
		String html = reader.read();
		
		Document doc = Jsoup.parse(html);
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
