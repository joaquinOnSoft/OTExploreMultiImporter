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
		List<Review> reviews = null;
		
		URLReader reader = new URLReader(url);
		String html = reader.read();
		
		Document doc = Jsoup.parse(html);
		Element script = doc.select("script[data-business-unit-json-ld=true]").first();
		
		if(script != null) {
			System.out.print("script: " + script.html());	
			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				TrustpilotReviewContainer[] reviewContainer  = objectMapper.readValue(html, TrustpilotReviewContainer[].class);
				System.out.print(reviewContainer);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
				
		return reviews;
	}
}
