package com.opentext.explore.importer.tripadvisor.v2;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

public class TripadvisorScraperHotelConsumer2Solr extends AbstractTripadvisorScraperHotelConsumer {

	public TripadvisorScraperHotelConsumer2Solr(BlockingQueue<TAJobInfo> queue, String host, String ttag) {
		super(queue, host, ttag);
	}

	@Override
	protected void processReviews(List<TAReview> reviews) {
		TripadvisorSolrAPIWrapper api = new TripadvisorSolrAPIWrapper(host);
		
        if(reviews != null) {
        	//TODO set tag in SOLR insert
        	api.solrBatchUpdate(ttag, reviews);
        }
        else {
        	log.info("No reviews found!");
        }		
	}		
}
