package com.opentext.explore.importer.tripadvisor;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.util.DateUtil;

public class TripadvisorScraperFacilities extends AbstractTripadvisorScraper {
	
	private static final int FIRST_REVIEWS_PAGE_NUMBER = 1;

	private static final String RATING_CLASS_1 = "bubble_10"; 
	private static final String RATING_CLASS_2 = "bubble_20";
	private static final String RATING_CLASS_3 = "bubble_30";
	private static final String RATING_CLASS_4 = "bubble_40";
	private static final String RATING_CLASS_5 = "bubble_50";

	private static final String URL_INIT_RESTAURANT_REVIEW = "https://www.tripadvisor.com/Restaurant_Review";
	private static final String URL_INIT_HOTEL_REVIEW = "https://www.tripadvisor.com/Hotel_Review";	
	
	public List<TAReview> getFacilityReviews(String link){
		List<TAReview> reviewsSinglePage = null;
		
		if(link.startsWith(URL_INIT_HOTEL_REVIEW)) {						
			reviewsSinglePage = getHotelReviews(link);
		}
		else if(link.startsWith(URL_INIT_RESTAURANT_REVIEW)) { 
			//We just want to process Hotel at this time. Ignoring restaurants						
			//reviewsSinglePage = getRestaurantReviews(link);						
		}		
		
		return reviewsSinglePage;
	}
	
	protected List<TAReview> getHotelReviews(String hotelLink){
		return getHotelReviews(hotelLink, FIRST_REVIEWS_PAGE_NUMBER);
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
}
