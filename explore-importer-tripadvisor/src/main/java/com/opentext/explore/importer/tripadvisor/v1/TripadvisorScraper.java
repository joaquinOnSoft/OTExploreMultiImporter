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
 *     Joaquín Garzón - initial implementation
 * @since 22.09.01
 */
package com.opentext.explore.importer.tripadvisor.v1;

import java.util.LinkedList;
import java.util.List;

import com.opentext.explore.importer.tripadvisor.AbstractTripadvisorScraper;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

public class TripadvisorScraper extends AbstractTripadvisorScraper {
	
	public TripadvisorScraper() {
		super();	
	}
	
	/**
	 * Initialize tripadvisor.com review page scraper 
	 * @param urlBase - URL base
	 * <i>Examples</i>: 
	 * <ul>
	 *    <li>https://www.tripadvisor.com</li>    
	 * </ul>
	 * @param searchTerm - search term in <strong>tripadvisor.com</strong>
	 * <i>Example</i>: clubmed
	 * 
	 */		
	public List<TAReview> getReviews(String searchTerm, boolean exactSearch){
		List<TAReview> reviews = null;
		List<TAReview> reviewsSinglePage = null;

		TripadvisorScraperSearch searchScraper = new TripadvisorScraperSearch();
		//Reach search page with the given search criteria
		List<String> links = searchScraper.search(searchTerm);

		if(links != null && links.size() > 0) {
			TripadvisorScraperFacilities facilitiesScraper = new TripadvisorScraperFacilities();
			
			for(String link: links) {
				if(exactSearch) {
					//Ignore links that not include the search term in the link
					if(!link.toLowerCase().contains(searchTerm.toLowerCase().replace(" ", "_"))) {
						log.info("Skipping link: {}", link);
						continue;
					}
				}
				
				//log.info("Clear coockies and relinitialize web client after search and before next URL read.");
				//resetWebClient();
				reviewsSinglePage = facilitiesScraper.getFacilityReviews(link);
				
				if(reviewsSinglePage != null && reviewsSinglePage.size() > 0) {
					if(reviews == null) {
						reviews = new LinkedList<TAReview>();
					}

					if(reviewsSinglePage != null) {
						for(TAReview rev: reviewsSinglePage) {
							log.debug("Review:{}", rev.toString());
						}
					}
					
					reviews.addAll(reviewsSinglePage);
				}
			}
		}

		return reviews; 
	}
}