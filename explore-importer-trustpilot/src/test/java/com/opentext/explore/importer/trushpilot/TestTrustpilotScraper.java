package com.opentext.explore.importer.trushpilot;

import junit.framework.TestCase;

import java.util.List;

import org.junit.Test;

import com.opentext.explore.importer.trushpilot.pojo.TrustpilotReview;

public class TestTrustpilotScraper extends TestCase{

	@Test
	public void getReviews() {
		TrustpilotScraper scraper = new TrustpilotScraper("bancsabadell.com");
		List<TrustpilotReview> reviews = scraper.getReviews();
		
		assertNotNull(reviews);
		assertTrue(reviews.size() > 0);	
		assertEquals(654, reviews.size());
	}
}
