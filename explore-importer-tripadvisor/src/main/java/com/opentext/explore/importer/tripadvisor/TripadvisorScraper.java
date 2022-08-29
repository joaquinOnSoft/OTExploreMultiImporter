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
 * @since 22.08.25
 */
package com.opentext.explore.importer.tripadvisor;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.explore.importer.http.URLReader;
import com.opentext.explore.importer.tripadvisor.pojo.Review;
import com.opentext.explore.importer.tripadvisor.pojo.TrustpilotReviewContainer;
import com.opentext.explore.importer.tripadvisor.pojo.TrustpilotReviewContainerType;

public class TripadvisorScraper {

	private static final Logger log = LogManager.getLogger(TripadvisorScraper.class);

	private static final String BASE_URL="https://www.tripadvisor.com";

	private static final String SEARCH_URL="/Search?q=";

	private String url;	
	private String urlBase;

	/**
	 * Initialize tripadvisor.com review page scraper 
	 * @param searchTearm - search term in <strong>tripadvisor.com</strong>
	 * <i>Example</i>: clubmed
	 */
	public TripadvisorScraper(String searchTearm) {
		this(BASE_URL, searchTearm);
	}

	/**
	 * Initialize tripadvisor.com review page scraper 
	 * @param urlBase - URL base
	 * <i>Examples</i>: 
	 * <ul>
	 *    <li>https://www.tripadvisor.com</li>    
	 * </ul>
	 * @param clienteAlias - Client alias in <strong>trustpilot.com</strong>
	 * <i>Example</i>: bancsabadell.com
	 * 
	 */
	public TripadvisorScraper(String urlBase, String searchTerm) {
		if(urlBase != null) {
			this.urlBase = urlBase;
			this.url = urlBase + SEARCH_URL + searchTerm;
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
			if(doc != null) {
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
			else {
				pageURL = null;
			}
		}
		while(pageURL != null);

		return reviews;
	}

	private Document readPage(String pageURL) {
		log.info("Reading page: " + pageURL);

		/*
		URLReader reader = new URLReader(pageURL);
		reader.setHeader(URLReader.HEADER_USER_AGENT, URLReader.LATEST_CHROME_ON_WINDOWS);
		reader.setHeader(URLReader.HEADER_REFERRER_POLICY, URLReader. STRICT_ORIGIN_WHEN_CROSS_ORIGIN);
		
		String html = reader.read();
		
		Cookie coockie = reader.getCoockieByName("OptanonConsent");
		log.debug("\t OptanonConsent: " + coockie.getValue());
		*/
		Document doc;
		try {
			int calls = 0;
			String sid = null;
			do {
				pageURL = pageURL + "&searchSessionId=nullsession" + Instant.now().toEpochMilli();
				if(sid != null) {
					pageURL = pageURL + "&sid=" + sid;
				}
				
				log.info("URL({}): {}", calls, pageURL);
				Response res = Jsoup
					    .connect(pageURL)				    
					    .method(Method.GET)
					    .cookie("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
					    .followRedirects(true)
					    .execute();
	
				log.debug("\t ----------------- BODY:\n\n");
				//log.debug(res.body());
	
				//This will get you cookies
				Map<String, String> cookies = res.cookies();
				
				log.debug("\t ----------------- COOKIES:");			
				log.debug(cookies.toString());
				sid = cookies.get("TASID");
				
				calls++;
			}while(calls < 2);
			
			/*
			String url2= pageURL + "&sid=" + cookies.get("TASID") + "&searchSessionId=nullsession" + Instant.now().toEpochMilli() ;
			log.info("Reading page: " + url2);
			res = Jsoup
				    .connect(url2)				    
				    .method(Method.GET)
				    .cookie("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")				    
				    .execute();
			log.debug("\t ----------------- BODY 2:\n\n");
			log.debug("\t ----------------- COOKIES:");			
			log.debug(cookies.toString());			
			//log.debug(res.body());			
			*/
		} catch (IOException e) {
			log.debug( e );
		}
				
		return null;
		//return Jsoup.parse(html);
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
