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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.pojo.Review;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

public class TripadvisorScraper {

	private static final Logger log = LogManager.getLogger(TripadvisorScraper.class);

	private static final String BASE_URL="https://www.tripadvisor.com";

	private static final String SEARCH_URL="/Search?q=";

	private String url;	
	private String urlBase;
	private WebClient webClient;

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
	 * @param searchTerm - search term in <strong>tripadvisor.com</strong>
	 * <i>Example</i>: clubmed
	 * 
	 */
	public TripadvisorScraper(String urlBase, String searchTerm) {
		if(urlBase != null) {
			this.urlBase = urlBase;
			this.url = urlBase + SEARCH_URL + searchTerm;
			
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
			java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);		
			
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setThrowExceptionOnScriptError(false);		
			webClient.getCookieManager().setCookiesEnabled(true);
			//TODO make language accepted a parameter
			webClient.addRequestHeader(HttpHeader.ACCEPT_LANGUAGE, "en");
		}
	}

	public List<Review> getReviews(){
		String pageURL = url;
		List<TAReview> reviews = null;

		HtmlPage page = null;

		page = readSearchPage(pageURL);
		if(page != null) {
			List<String> hotelLinks = getListSearchResults(page);
			
			if(hotelLinks != null && hotelLinks.size() > 0) {
				Document hotelPage = null;
				for(String link: hotelLinks) {
					page = readPage(link);
					
					log.debug("----------------------------------");
					log.debug(link);
					log.debug("----------------------------------");
					log.debug(page.getDocumentElement().getElementsByTagName("title"));
					log.debug("----------------------------------");
					
					//List<DomElement> reviewCards = page.getByXPath("//div[@class='member_info']");
					List<DomElement> quoteDivs = page.getByXPath("//div[@class='quote']");
					List<DomElement> entryDivs = page.getByXPath("//div[@class='entry']");
					
					if(quoteDivs != null) {
						int numQuotes = quoteDivs.size();
						if(numQuotes > 0) {
							List<DomElement> partialEntryP = null; 
							List<DomElement> postSnippetSpan = null;
							String partialEntryStr= null; 
							String postSnippetStr = null;
							
							if(reviews == null) {
								reviews = new LinkedList<TAReview>();								
							}
							TAReview taReview = new TAReview();
							for(int i=0; i<numQuotes; i++) {
								taReview.setTitle(quoteDivs.get(i).asNormalizedText());

								partialEntryP = entryDivs.get(i).getByXPath("//p[@class='partial_entry']");
								if(partialEntryP != null && partialEntryP.size() > 0) {
									partialEntryStr = partialEntryP.get(0).asNormalizedText().replace("...More", "");
								}								
								
								postSnippetSpan = entryDivs.get(i).getByXPath("//span[@class='postSnippet']");
								if(postSnippetSpan != null && postSnippetSpan.size() > 0) {
									postSnippetStr = postSnippetSpan.get(0).asNormalizedText();
								}
								
								if(partialEntryStr != null && postSnippetStr != null) {
									taReview.setContent(partialEntryStr + " " + postSnippetStr);
								}
							}
							
							reviews.add(taReview);
						}
					}
					
					for(TAReview rev: reviews) {
						log.debug("Review:{}", rev.toString());
					}
					
					return null;
				}
			}
		}
		
		return null; //TODO change for reviews
	}

	/**
	 * Read Tripadvisor search page.
	 * <strong>NOTE</strong>: It requires 2 reads of the page.
	 *    - 1st read doesn't includes the 'searchSessionId'. It's generated in 
	 *      the client browser (executing js code), so we need library that 
	 *      emulates a browser, not just a pure URL reader 
	 *    - 2nd read includes the 'searchSessionId' recovered from the 1st read
	 * This behavior force us to use an additional library called <strong>HTMLUnit</strong>
	 * @param pageURL - Tripadvisor search URL 
	 * @return
	 */
	private HtmlPage readSearchPage(String pageURL) {
		log.info("Read page: " + pageURL);
		HtmlPage page = null;
		
		if(webClient != null) {		
			int count= 0;
			String searchSessionId = null;			
			
			do {
				if (searchSessionId != null) {
					pageURL += "&searchSessionId=" + searchSessionId;
				}
											
				page = readPage(pageURL);
				
				if(page != null) {
					List<DomElement> nodeList = page.getElementsByName("searchSessionId");
					for (DomElement element : nodeList) {
						log.debug("searchSessionId: " + element.getAttribute("value"));
						searchSessionId = element.getAttribute("value");
					}				
				}
				count++;
			}
			while(count < 2);
			
			webClient.close();
		}

		return page;
	}

	private List<String> getListSearchResults(HtmlPage page){
		List<String> searchResults = null;
		if(page != null) {
			int start, end= -1;
			String onClicTxt=null;
			String link = null;
			
			List<DomElement> resultTitles = page.getByXPath("//div[@class='location-meta-block']/div[@class='result-title']");
			for (DomElement element : resultTitles) {
				log.debug(element.getAttribute("onclick"));
				onClicTxt = element.getAttribute("onclick");
				
				start = onClicTxt.indexOf("/");
				end = onClicTxt.indexOf("', {type");
				
				if(start != -1 && end != -1) {
					link = urlBase + onClicTxt.substring(start, end);
					log.debug(link);
					
					if(searchResults == null) {
						searchResults = new LinkedList<String>();
					}
					
					searchResults.add(link);
				}
			}			
		}
		return searchResults;
	}
	
	/**
	 * Read a HTML page using HTMLUnit library
	 * @param pageURL - page URL
	 * @return
	 */
	private HtmlPage readPage(String pageURL) {
		HtmlPage page = null;
		
		log.info("Reading page: " + pageURL);
		
		try {			
			page = webClient.getPage(pageURL);
		} catch (FailingHttpStatusCodeException | IOException e) {
			log.error("Initializing web client: ", e);
		}
		
		return page;
	}	
}
