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
package com.opentext.explore.importer.tripadvisor.v2;

import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.opentext.explore.importer.tripadvisor.pojo.TAJobInfo;

public class TestTripadvisorScraperHotelConsumer {
	private static final int ONE_MINUTE_IN_MILIS = 60000;

	//private static final String TRIPADVISOR_HOTEL_LINK_WITH_01_REVIEWS = "https://www.tripadvisor.com/Hotel_Review-g147293-d23012756-Reviews-Club_Med-Punta_Cana_La_Altagracia_Province_Dominican_Republic.html";
	private static final String TRIPADVISOR_HOTEL_LINK_WITH_36_REVIEWS = "https://www.tripadvisor.com/Hotel_Review-g298296-d2193909-Reviews-LaVilla_by_Holiday_Villa_Cherating-Kuantan_Kuantan_District_Pahang.html";
	
	private static final int NUM_CONSUMERS = 2;
	
	private static List<String> links;
	private static BlockingQueue<TAJobInfo> queue = null;
	private static TripadvisorScraperFacilitiesProducer producer;
	
	 @BeforeClass
	 public static void init() {
		 links = new LinkedList<String>();		 
		 links.add(TRIPADVISOR_HOTEL_LINK_WITH_36_REVIEWS);
		 		 
		 
		 queue = new LinkedBlockingQueue<TAJobInfo>();		
		 producer = new TripadvisorScraperFacilitiesProducer(links, queue, NUM_CONSUMERS);

	 }
	 
	
	@Test
	public void start() {

		new Thread(producer).start();
		for(int i=0; i<NUM_CONSUMERS; i++) {
			new Thread(new TripadvisorScraperHotelConsumer2Solr(queue, "localhost:8983", "Tripadvisor")).start();
		}
		
		//TODO look for a way to avoid the use of a sleep in the test.
		//The test can fail based on the connection speed. It's not ideal.
		try {
			Thread.sleep(ONE_MINUTE_IN_MILIS);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}			
	}
}
