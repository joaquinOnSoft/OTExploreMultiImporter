package com.opentext.explore.importer.reddit;

import org.junit.Test;

import junit.framework.TestCase;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;

public class TestRedditImporter extends TestCase {

	@Test
	public void testCreateMonthlyPaginator() {
		RedditImporter redditImp = new RedditImporter("http://localhost:8983");
		DefaultPaginator<Submission> paginator = redditImp.createMonthlyPaginator("CanadaPost");
		
		assertNotNull(paginator);
		assertTrue(TimePeriod.MONTH == paginator.getTimePeriod());
	}	

	@Test
	public void testCreateHourlyPaginator() {
		RedditImporter redditImp = new RedditImporter("http://localhost:8983");
		DefaultPaginator<Submission> paginator = redditImp.createHourlyPaginator("CanadaPost");
		
		assertNotNull(paginator);
		assertTrue(TimePeriod.HOUR == paginator.getTimePeriod());
	}		
}
