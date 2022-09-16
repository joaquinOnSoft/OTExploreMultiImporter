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
package com.opentext.explore.importer.tripadvisor.v2;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.importer.tripadvisor.TripadvisorScraperSearch;
import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;

/**
 * Tripadvisor importer for OpenText Explore (Voice of the customer solution)
 * 
 * @author Joaqu�n Garz�n
 * @since 22.09.01
 */
public class TripadvisorImporter {

	/** Solr URL (this Solr instance is used by Explore) */
	private String host = null;
	private int numComsumers;

	protected static final Logger log = LogManager.getLogger(TripadvisorImporter.class);

	/**
	 * @param host - Solr URL (this Solr instance is used by Explore)
	 */
	public TripadvisorImporter(String host, int numComsumers) {
		this.host = host;
		this.numComsumers = numComsumers;
	}

	/**
	 * @param searchTerm    - search term to look for in tripadvisor.com
	 * @param tTag          - Tripadvisor Importer tag
	 * @param timeInSeconds - Seconds between each call against Tripadvisor site
	 */
	public void start(String searchTerm, boolean exactSearch, String tTag, boolean excellOutput) {
		List<String> links = null;
		TripadvisorScraperSearch scraper = new TripadvisorScraperSearch();
		BlockingQueue<TAJobInfo> queue = new LinkedBlockingQueue<TAJobInfo>();

		links = scraper.search(searchTerm, exactSearch);
		if (links != null) {
			log.info("# links recovered: {}", links.size());
			
			new Thread(new TripadvisorScraperFacilitiesProducer(links, queue, numComsumers)).start();
			for (int i = 0; i < numComsumers; i++) {
				log.info("new consumer thread #{}", i);
				if(excellOutput) {
					new Thread(new TripadvisorScraperHotelConsumer2Excel(queue, host, tTag)).start();
				}
				else {
					new Thread(new TripadvisorScraperHotelConsumer2Solr(queue, host, tTag)).start();
				}
			}			
		}
		else {
			log.info("No hotel links found!!!");			
		}
	}
}
