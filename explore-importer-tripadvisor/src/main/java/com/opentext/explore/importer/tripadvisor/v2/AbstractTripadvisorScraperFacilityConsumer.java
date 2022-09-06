package com.opentext.explore.importer.tripadvisor.v2;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;
import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfoType;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

public abstract class AbstractTripadvisorScraperFacilityConsumer implements Runnable  {
		
	private static final String RATING_CLASS_1 = "bubble_10"; 
	private static final String RATING_CLASS_2 = "bubble_20";
	private static final String RATING_CLASS_3 = "bubble_30";
	private static final String RATING_CLASS_4 = "bubble_40";
	private static final String RATING_CLASS_5 = "bubble_50";
	
	private BlockingQueue<TAJobInfo> queue;
	private String host = null;
	private String ttag = null;
	
	protected static final Logger log = LogManager.getLogger(AbstractTripadvisorScraperFacilityConsumer.class);

	public AbstractTripadvisorScraperFacilityConsumer(BlockingQueue<TAJobInfo> queue, String host, String ttag) {
		this.queue = queue;
		this.host = host;
		this.ttag = ttag;
	}
	
	@Override
	public void run() {
		try {
			TripadvisorSolrAPIWrapper api = new TripadvisorSolrAPIWrapper(host);
			List<TAReview> reviews = null;
            while (true) {
            	TAJobInfo jobInfo = queue.take();
                if (jobInfo.getType() == TAJobInfoType.TERMINATOR) {
                    log.info("Consumer ending...");
                    return;
                }
                log.debug("{} - # pag reviews: {} - URL: {}: ", Thread.currentThread().getName(), jobInfo.getPageNumber(), jobInfo.getUrl());
                
                reviews = getFacilityReviews(jobInfo.getPage(), jobInfo.getPageNumber());
                
                if(reviews != null) {
                	//TODO set tag in SOLR insert
                	api.solrBatchUpdate(ttag, reviews);
                }
                else {
                	log.info("No reviews found!");
                }
            }
        } catch (InterruptedException e) {
        	log.error("Thread interrupted: ", e);
            Thread.currentThread().interrupt();
        }			
	}		

	protected abstract  List<TAReview> getFacilityReviews(HtmlPage page, int reviewsPageNumber);
	
	protected String getTextContentFromElementByXPath(DomElement element, String xPahtExpr) {
		List<DomElement> elementsFound = element.getByXPath(xPahtExpr);
		String value = null;
	
		if(elementsFound != null && elementsFound.size() > 0) {
			value = elementsFound.get(0).asNormalizedText();
		}
	
		return value;
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
