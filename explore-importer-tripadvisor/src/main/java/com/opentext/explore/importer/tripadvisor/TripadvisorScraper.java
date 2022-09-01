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
 * @since 22.09.01
 */
package com.opentext.explore.importer.tripadvisor;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.util.DateUtil;

public class TripadvisorScraper {

	private static final Logger log = LogManager.getLogger(TripadvisorScraper.class);

	private static final String BASE_URL="https://www.tripadvisor.com";

	private static final String SEARCH_URL="/Search?q=";

	private static final String URL_INIT_RESTAURANT_REVIEW = "https://www.tripadvisor.com/Restaurant_Review";
	private static final String URL_INIT_HOTEL_REVIEW = "https://www.tripadvisor.com/Hotel_Review";

	private static final String RATING_CLASS_1 = "bubble_10"; 
	private static final String RATING_CLASS_2 = "bubble_20";
	private static final String RATING_CLASS_3 = "bubble_30";
	private static final String RATING_CLASS_4 = "bubble_40";
	private static final String RATING_CLASS_5 = "bubble_50";
	
	private String url;	
	private String urlBase;
	private String searchTerm;
	private boolean exactSearch;
	private WebClient webClient;

	/**
	 * Initialize tripadvisor.com review page scraper 
	 * @param searchTearm - search term in <strong>tripadvisor.com</strong>
	 * <i>Example</i>: clubmed or 'club med'
	 */
	public TripadvisorScraper(String searchTearm) {
		this(searchTearm, false);
	}
	
	public TripadvisorScraper(String searchTearm, boolean exactSearch) {
		this(BASE_URL, searchTearm, exactSearch);
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
	public TripadvisorScraper(String urlBase, String searchTerm, boolean exactSearch) {
		if(urlBase != null) {
			this.urlBase = urlBase;
			this.url = urlBase + SEARCH_URL + searchTerm;
			this.searchTerm = searchTerm;
			this.exactSearch = exactSearch;

			//Disable logs for 'HTMLUnit' library, is too noisy. 
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
			java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);		

			//Initialize 'HTMLUnit' web client
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setThrowExceptionOnScriptError(false);		
			webClient.getCookieManager().setCookiesEnabled(true);
			//TODO make language accepted a parameter
			webClient.addRequestHeader(HttpHeader.ACCEPT_LANGUAGE, "en");
		}
	}

	public List<TAReview> getReviews(){
		return getReviews(false);
	}
	
	public List<TAReview> getReviews(boolean mustContainKeyWord){
		String pageURL = url;
		List<TAReview> reviews = null;
		List<TAReview> reviewsSinglePage = null;

		HtmlPage page = null;

		page = readSearchPage(pageURL);
		if(page != null) {
			List<String> links = getListSearchResults(page);

			if(links != null && links.size() > 0) {
				for(String link: links) {
					if(exactSearch) {
						if(link.contains(searchTerm.replace(" ", "_"))) {
							log.info("Skipping link: {}", link);
							continue;
						}
					}
					
					if(link.startsWith(URL_INIT_RESTAURANT_REVIEW)) {
						//Do nothing 
						//We just want to process Hotel at this time.
						
						//reviewsSinglePage = getRestaurantReviews(link);						
					}
					else if(link.startsWith(URL_INIT_HOTEL_REVIEW)) {
						reviewsSinglePage = getHotelReviews(link);
					}

					if(reviewsSinglePage != null && reviewsSinglePage.size() > 0) {
						if(reviews == null) {
							reviews = new LinkedList<TAReview>();
						}

						if(reviewsSinglePage != null) {
							for(TAReview rev: reviewsSinglePage) {
								log.debug("Review:{}", rev.toString());
							}
						}
						
						reviews.addAll(reviewsSinglePage);
					}
				}
			}
		}

		return reviews; 
	}

	protected List<TAReview> getHotelReviews(String hotelLink){
		return getHotelReviews(hotelLink, 1);
	}
	
	/**
	 * Get all the reviews for a given hotel
	 * @param TripAdvisor hotel link, usually start with 'https://www.tripadvisor.com/Hotel_Review', 
	 * i.e. https://www.tripadvisor.com/Hotel_Review-g666315-d579713-Reviews-Club_Med_Cherating_Malaysia-Cherating_Kuantan_District_Pahang.html
	 * @return List of reviews for the given hotel
	 */
	private List<TAReview> getHotelReviews(String hotelLink, int reviewsPageNumber){
		List<TAReview> reviews = null;

		HtmlPage page = readPage(hotelLink);

		reviews = getHotelReviews(page, reviewsPageNumber);

		return reviews;
	}

	private List<TAReview> getHotelReviews(HtmlPage page, int reviewsPageNumber) {
		List<TAReview> reviews = null;
		
		List<DomElement> reviewTabDivs = page.getByXPath("//div[@data-test-target='reviews-tab']");

		if(reviewTabDivs != null && reviewTabDivs.size() > 0) {
			StringBuilder content = new StringBuilder(); 
			String ratingDateStr = null;
			String ratingClassStr = null;
			Date ratingDate = null;			

			List<DomElement> titleList = reviewTabDivs.get(0).getByXPath("//div[@data-test-target='review-title']"); 
			
			if(titleList != null) {
				reviews = new LinkedList<TAReview>();
				TAReview taReview = null;
				
				List<DomElement> idList = reviewTabDivs.get(0).getByXPath("//div[@class='WAllg _T']");
				List<DomElement> contentList = reviewTabDivs.get(0).getByXPath("//q[@class='QewHA H4 _a']//span");
				List<DomElement> ratingList  = reviewTabDivs.get(0).getByXPath("//div[@data-test-target='review-rating']//span");
				List<DomElement> ratingDateList  = reviewTabDivs.get(0).getByXPath("//span[@class='teHYY _R Me S4 H3']");
				List<DomElement> authorList  = reviewTabDivs.get(0).getByXPath("//a[@class='ui_header_link uyyBf']"); 
				//Location is not always provided in the page.
				//List<DomElement> locationList  = reviewTabDivs.get(0).getByXPath("//span[@class='default LXUOn small']");
				List<DomElement> nextList  = reviewTabDivs.get(0).getByXPath("//a[@class='ui_button nav next primary ']");				
				
				int numTitles = titleList.size();
				for(int i=0; i<numTitles; i++) {
					taReview = new TAReview();
					
					taReview.setId(idList.get(i).getAttribute("data-reviewid"));
					
					taReview.setTitle(titleList.get(i).asNormalizedText());

					for(DomElement span: contentList) {
						content.append(span.asNormalizedText()).append(" "); 
					}
					taReview.setContent(content.toString());
					content.setLength(0); //Clear the content
					
					ratingClassStr = ratingList.get(i).getAttribute("class");
					taReview.setRating(getRatingFromClass(ratingClassStr));
										
					ratingDateStr = ratingDateList.get(i).asNormalizedText();
					try {
						ratingDate = DateUtil.strEngToDate(ratingDateStr.replace("Date of stay: ", "").trim(), "MMM yyyy");
						taReview.setCreationDate(ratingDate);
					} catch (ParseException e) {
						log.error("Invalid date format: {}", ratingDateStr);
					}

					taReview.setAuthor(authorList.get(i).asNormalizedText());

					//taReview.setLocation(locationList.get(i).asNormalizedText());
					
					reviews.add(taReview);
				}
				
				log.debug("# reviews found in page {}: {}", reviewsPageNumber, reviews.size());
				int counter = 0;
				for(TAReview rev: reviews) {
					log.debug("\t{}) {}", counter++, rev.getTitle());
				}
				
				if(nextList != null && nextList.size() > 0) {
					try {
						HtmlPage nextPage = nextList.get(0).click();
						//log.debug(nextPage.asXml()) ;
						log.debug("Loading next reviews in page...");
						
						List<TAReview> nextReviews = getHotelReviews(nextPage, ++reviewsPageNumber);
						if(nextReviews != null && nextReviews.size() > 0) {
							reviews.addAll(nextReviews);
						}
					} catch (IOException e) {
						log.error("Error reading next page: ", e);
					}
				}
			}
		}
		return reviews;
	}	

	/**
	 * Get all the reviews for a given restaurant
	 * @param TripAdvisor restaurant link, usually start with 'https://www.tripadvisor.com/Restaurant_Review', 
	 * i.e. https://www.tripadvisor.com/Restaurant_Review-g667831-d21344910-Reviews-Clubmed-Mangaratiba_State_of_Rio_de_Janeiro.html
	 * @return List of reviews for the given restaurant
	 */	
	protected List<TAReview> getRestaurantReviews(String restaurantReviewLink){
		List<TAReview> reviews = null;

		HtmlPage page = readPage(restaurantReviewLink);

		List<DomElement> reviewSelectorDivs = page.getByXPath("//div[@class='reviewSelector']");

		if(reviewSelectorDivs != null && reviewSelectorDivs.size() > 0) {

			int numReviewSelectorDivs = reviewSelectorDivs.size();
			if(numReviewSelectorDivs > 0) {
				String title = null;
				String partialEntryStr= null; 
				String postSnippetStr = null;
				String ratingDateStr = null;
				Date ratingDate = null;
				String memberInfoStr = null;
				String location = null;
				List<DomElement> idDivs = null;

				if(reviews == null) {
					reviews = new LinkedList<TAReview>();								
				}

				TAReview taReview = new TAReview();
				for(int i=0; i<numReviewSelectorDivs; i++) {					
					idDivs = reviewSelectorDivs.get(i).getByXPath("//div[@class='reviewSelector']");
					taReview.setId(idDivs.get(0).getAttribute("data-reviewid"));
						
					title = getTextContentFromElementByXPath(reviewSelectorDivs.get(i), "//div[@class='quote']");					
					taReview.setTitle(title);

					partialEntryStr = getTextContentFromElementByXPath(reviewSelectorDivs.get(i), "//p[@class='partial_entry']");
					if(partialEntryStr != null) {
						partialEntryStr = partialEntryStr.replace("...More", "");
					}								

					postSnippetStr = getTextContentFromElementByXPath(reviewSelectorDivs.get(i), "//span[@class='postSnippet']");

					if(partialEntryStr != null && postSnippetStr != null) {
						taReview.setContent(partialEntryStr + " " + postSnippetStr);
					}

					ratingDateStr = getTextContentFromElementByXPath(reviewSelectorDivs.get(i), "//span[@class='ratingDate']");
					try {
						ratingDate = DateUtil.strEngToDate(ratingDateStr.replace("Reviewed ", "").trim(), "MMM d, yyyy");
						taReview.setCreationDate(ratingDate);
					} catch (ParseException e) {
						log.error("Invalid date format: {}", ratingDateStr);
					}

					memberInfoStr = getTextContentFromElementByXPath(reviewSelectorDivs.get(i), "//div[@class='info_text pointer_cursor']/div");
					taReview.setAuthor(memberInfoStr);

					location = getTextContentFromElementByXPath(reviewSelectorDivs.get(i), "//div[@class='userLoc']/strong");
					taReview.setLocation(location);					
				}

				reviews.add(taReview);
			}
		}

		return reviews;
	}

	private String getTextContentFromElementByXPath(DomElement element, String xPahtExpr) {
		List<DomElement> elementsFound = element.getByXPath(xPahtExpr);
		String value = null;

		if(elementsFound != null && elementsFound.size() > 0) {
			value = elementsFound.get(0).asNormalizedText();
		}

		return value;
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
	
	protected int getRatingFromClass(String ratingClass) {
		int rating = 5;
		if(ratingClass != null) {
			if(ratingClass.contains(RATING_CLASS_1) ) {
				rating = 1;
			}
			else if(ratingClass.contains(RATING_CLASS_2) ) {
				rating = 2;
			}
			else if(ratingClass.contains(RATING_CLASS_3) ) {
				rating = 3;
			}		
			else if(ratingClass.contains(RATING_CLASS_4) ) {
				rating = 4;
			}		
			else if(ratingClass.contains(RATING_CLASS_5) ) {
				rating = 5;
			}					
		}
		return rating;
	}
}