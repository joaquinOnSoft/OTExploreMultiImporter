package com.opentext.explore.importer.tripadvisor.multithread;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.util.DateUtil;

public class TripadvisorScraperRestaurantConsumer extends AbstractTripadvisorScraperFacilityConsumer  {


	public TripadvisorScraperRestaurantConsumer(BlockingQueue<TAJobInfo> queue) {
		super(queue);
	}

	/**
	 * Get all the reviews for a given restaurant
	 * @param TripAdvisor restaurant link, usually start with 'https://www.tripadvisor.com/Restaurant_Review', 
	 * i.e. https://www.tripadvisor.com/Restaurant_Review-g667831-d21344910-Reviews-Clubmed-Mangaratiba_State_of_Rio_de_Janeiro.html
	 * @return List of reviews for the given restaurant
	 */	
	protected List<TAReview> getFacilityReviews(HtmlPage page, int reviewsPageNumber){
		List<TAReview> reviews = null;

		//HtmlPage page = readPage(restaurantReviewLink);

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
