package com.opentext.explore.importer.tripadvisor.v2;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.pojo.TAFacility;
import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.util.DateUtil;

public class TripadvisorScraperHotelConsumer extends AbstractTripadvisorScraperFacilityConsumer {

	public TripadvisorScraperHotelConsumer(BlockingQueue<TAJobInfo> queue, String host, String ttag) {
		super(queue, host, ttag);
	}
	
	protected List<TAReview> getFacilityReviews(HtmlPage page, int reviewsPageNumber) {
		TAFacility facility = null;
		List<TAReview> reviews = null;
		
		//Read facility information (Hotel name, address, phone and web page URL)
		List<DomElement> hotelDivs = page.getByXPath("//div[@id='taplc_hotel_review_atf_hotel_info_web_component_0']");
		if(hotelDivs != null && hotelDivs.size() > 0) {
			facility = new TAFacility();
			facility.setName(getTextContentFromElementByXPath(hotelDivs.get(0), "//h1[@id='HEADING']"));
			facility.setAddress(getTextContentFromElementByXPath(hotelDivs.get(0), "//span[@class='fHvkI PTrfg']"));
			facility.setPhone(getTextContentFromElementByXPath(hotelDivs.get(0), "//span[@class='zNXea NXOxh NjUDn']"));
			facility.setWeb(getTextContentFromElementByXPath(hotelDivs.get(0), "//a[@class='YnKZo Ci Wc _S C pInXB _S ITocq jNmfd']"));
		}
		
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
					
					if(facility != null) {
						taReview.setFacility(facility);
					}
					
					reviews.add(taReview);
				}
				
				log.debug("# reviews found in page {}: {}", reviewsPageNumber, reviews.size());
				int counter = 0;
				for(TAReview rev: reviews) {
					log.debug("\t{}) {}", counter++, rev.getTitle());
				}
			}
		}
		return reviews;
	}	
}
