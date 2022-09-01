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
 *
 */
package com.opentext.explore.importer.tripadvisor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.util.FileUtil;



/**
 * Tripadvisor importer for OpenText Explore (Voice of the customer solution)
 * @author Joaquín Garzón
 * @since 22.09.01
 */
public class TripadvisorImporter {

	private static final int MILISECONDS_IN_SECOND = 1000;
	 
	/** Solr URL (this Solr instance is used by Explore) */
	private String host = null; 

	protected static final Logger log = LogManager.getLogger(TripadvisorImporter.class);
	
	/**
	 * @param urlBase - URL base
	 * <i>Examples</i>: 
	 * <ul>
	 *    <li>https://www.trustpilot.com</li>
	 *    <li>https://es.trustpilot.com</li>    
	 * </ul>
	 */
	public TripadvisorImporter(String host) {
		this.host = host;
		
	}

	/**
	 * @param searchTerm - Trushpilot client alias, e.g. in the URL 
	 * https://www.trustpilot.com/review/bancsabadell.com the literal
	 * 'bancsabadell.com' is the client alias
	 * @param tTag - Trustpilot Importer tag
	 * @param timeInSeconds - Seconds between each call against Trustpilot site
	 */
	public void start(String searchTerm, boolean exactSearch, String tTag, int timeInSeconds) {		
		List<TAReview> reviews = null;
		TripadvisorScraper scraper = new TripadvisorScraper(searchTerm, exactSearch);
		
		do {
					
			try {
				reviews = scraper.getReviews();
				if(reviews != null) {
					solrBatchUpdate(tTag, reviews);
				}
				
				log.debug("Sleeping " + timeInSeconds +  " seconds: ZZZZZZZ!");
				Thread.sleep(timeInSeconds * MILISECONDS_IN_SECOND);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
				System.exit(-1);
			}
		}while(true);
	}
	
	/**
	 * Call to the /solr/interaction/otcaBatchUpdate 
	 * method provided by Solr in order to insert new content
	 * @param rtag - Reddit Importer tag (used to filter content in Explore)
	 * @param firstPage - List of the latest submissions published in Reddit
	 * @return true if the insertion in Solr was ok, false in other case. 
	 */
	protected boolean solrBatchUpdate(String rtag, List<TAReview> reviews) {
		boolean updated = true;
		
		String xmlPath = null;
		String xmlFileName = FileUtil.getRandomFileName(".xml");
		try {
			
			xmlPath = TripadvisorTransformer.reviewsToXMLFile(reviews, xmlFileName, rtag);
			
			SolrAPIWrapper wrapper = null;
			if(host == null)
				wrapper = new SolrAPIWrapper();
			else {
				wrapper = new SolrAPIWrapper(host);
			}
			wrapper.otcaBatchUpdate(new File(xmlPath));	
		} catch (IOException e) {
			log.error(e.getMessage());
			updated = false;
		}
		finally {
			if(xmlPath != null) {
				FileUtil.deleteFile(xmlPath);	
			}
		}
		
		return updated;
	}
}
