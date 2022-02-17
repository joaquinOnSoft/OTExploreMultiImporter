/*
 *   (C) Copyright 2021 OpenText and others.
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
 *
 */
package com.opentext.explore.importer.reddit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.dean.jraw.models.Submission;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;

public class TestRedditImporter {

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
