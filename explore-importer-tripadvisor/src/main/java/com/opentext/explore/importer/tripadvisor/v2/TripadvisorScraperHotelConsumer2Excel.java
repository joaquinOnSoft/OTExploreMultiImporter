package com.opentext.explore.importer.tripadvisor.v2;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.opentext.explore.importer.tripadvisor.TripadvisorTransformer2Excell;
import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

public class TripadvisorScraperHotelConsumer2Excel extends AbstractTripadvisorScraperHotelConsumer {

	public TripadvisorScraperHotelConsumer2Excel(BlockingQueue<TAJobInfo> queue, String host, String ttag) {
		super(queue, host, ttag);
	}

	@Override
	protected void processReviews(List<TAReview> reviews) {		
        if(reviews != null) {
        	TripadvisorTransformer2Excell transformer = new TripadvisorTransformer2Excell();
        	transformer.process(ttag, reviews);
        }
        else {
        	log.info("No reviews found!");
        }		
	}		
}
