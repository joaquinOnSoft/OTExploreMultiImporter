/*
 *   (C) Copyright 2022 OpenText and others.
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
 *     Joaqu�n Garz�n - initial implementation
 *
 */
package com.opentext.explore.importer.trushpilot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.opentext.explore.importer.trushpilot.pojo.TrustpilotReview;

public class TestTrustpilotScraper {

	@Test
	public void getReviews() {
		TrustpilotScraper scraper = new TrustpilotScraper("bancsabadell.com");
		List<TrustpilotReview> reviews = scraper.getReviews();
		
		assertNotNull(reviews);
		assertTrue(reviews.size() > 0);	
		assertEquals(654, reviews.size());
	}
}
