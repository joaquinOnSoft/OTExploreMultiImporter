package com.opentext.explore.importer.tripadvisor.multithread;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.AbstractTripadvisorScraper;
import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;

public class TripadvisorScraperFacilitiesProducer extends AbstractTripadvisorScraper implements Runnable{
	
	private static final int NO_REVIEWS_PAGE_NUMBER = -1;
	private static final int FIRST_REVIEWS_PAGE_NUMBER = 1;

	private static final String URL_INIT_RESTAURANT_REVIEW = "https://www.tripadvisor.com/Restaurant_Review";
	private static final String URL_INIT_HOTEL_REVIEW = "https://www.tripadvisor.com/Hotel_Review";	

	private List<String> links;
	private BlockingQueue<TAJobInfo> queue;
	private int numConsumers;

	public TripadvisorScraperFacilitiesProducer(List<String> links, BlockingQueue<TAJobInfo> queue, int numConsumers) {
		this.links = links;
		this.queue = queue;
		this.numConsumers = numConsumers;
	}

	@Override	
	public void run() {
        try {
        	for(String link: links) {
        		log.debug("Link to find review pages: {}", link);
        		processFacilityReviews(link);
        	}
        	
        	for(int i=0; i<numConsumers; i++) {
        		//JOB info to terminate the job in the consumer side
        		queue.add(new TAJobInfo());
        	}
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }	
	
	protected void processFacilityReviews(String link) throws InterruptedException{
		if(link.startsWith(URL_INIT_HOTEL_REVIEW)) {						
			processHotelReviews(link);
		}
		else if(link.startsWith(URL_INIT_RESTAURANT_REVIEW)) { 
			//We just want to process Hotel at this time. Ignoring restaurants						
			//reviewsSinglePage = getRestaurantReviews(link);						
		}		
	}
		
	/**
	 * Get all the reviews for a given hotel
	 * @param TripAdvisor hotel link, usually start with 'https://www.tripadvisor.com/Hotel_Review', 
	 * i.e. https://www.tripadvisor.com/Hotel_Review-g666315-d579713-Reviews-Club_Med_Cherating_Malaysia-Cherating_Kuantan_District_Pahang.html
	 * @return List of reviews for the given hotel
	 */
	private void processHotelReviews(String hotelLink) throws InterruptedException{		
		log.info("Review pag # 1 - {}", FIRST_REVIEWS_PAGE_NUMBER, hotelLink);
		
		HtmlPage page = readPage(hotelLink);

		if(page != null) {
			processHotelReviews(hotelLink, page, FIRST_REVIEWS_PAGE_NUMBER, NO_REVIEWS_PAGE_NUMBER);
		}
	}

	private int processHotelReviews(String hotelLink, HtmlPage page, int currentPageNumber, int latestPageNumber) throws InterruptedException{		
		//Add hotel reviews page
		if(currentPageNumber >= latestPageNumber) {
			log.info("\t Adding review pag # {} to queue - {}", latestPageNumber, hotelLink);
			queue.add(new TAJobInfo(hotelLink, page, currentPageNumber));
		}
		
		//Read facility information (Hotel name, address, phone and web page URL)
		List<DomElement> pageNumAs = page.getByXPath("//div[@class='pageNumbers']//a[@class='pageNum ']");
		
		HtmlPage nextPage = null;
		String numPagTxt = null;
		int numPagInt = -1;
		int latestPageNumberTmp = NO_REVIEWS_PAGE_NUMBER;
			
		for(DomElement a: pageNumAs) {
			numPagTxt = a.asNormalizedText();
			numPagInt = Integer.parseInt(numPagTxt);
			
			if(numPagInt > latestPageNumber) {
				log.debug("\t\tProcessing page # {}... ", numPagInt);
				try {
					nextPage = a.click();					
					latestPageNumberTmp = processHotelReviews(hotelLink, nextPage, numPagInt, numPagInt);
					if(latestPageNumberTmp > latestPageNumber) {
						latestPageNumber = latestPageNumberTmp;
					}
				} catch (IOException e) {
					log.error("Processing page # {}/{}:  {}", numPagInt, latestPageNumber, e.getMessage());
				}
			}
			else {
				log.debug("\t\tSkiping page # {} (previously processed)...", numPagInt);
			}
		}	
		
		return latestPageNumber;
	}	
}
