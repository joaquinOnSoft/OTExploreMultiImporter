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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.opentext.explore.importer.tripadvisor.pojo.TAReview;

public class TestTripadvisorScraper {

	private static final String TRIPADVISOR_RESTAURANT_LINK = "https://www.tripadvisor.com/Restaurant_Review-g667831-d21344910-Reviews-Clubmed-Mangaratiba_State_of_Rio_de_Janeiro.html";
	private static final String TRIPADVISOR_HOTEL_LINK = "https://www.tripadvisor.com/Hotel_Review-g666315-d579713-Reviews-Club_Med_Cherating_Malaysia-Cherating_Kuantan_District_Pahang.html";

	//@Test
	public void getReviews() {
		TripadvisorScraper scraper = new TripadvisorScraper("clubmed");
		List<TAReview> reviews = scraper.getReviews();

		assertNotNull(reviews);
		assertTrue(reviews.size() > 0);
		// The number of comments can vary between execution.
		// At the moment of writing this test, February 21th 2022, the figures are:
		// Total comments: 656
		// Comments in English: 306
		assertEquals(1, reviews.size());
		assertNotNull(reviews.get(0).getTitle());
		assertNotNull(reviews.get(0).getContent());
		assertNotNull(reviews.get(0).getCreationDate());
		//assertNotNull(reviews.get(0).getLocation());
	}


	@Test
	public void getHotelReviews() {
		TripadvisorScraper scraper = new TripadvisorScraper("clubmed");
		List<TAReview> reviews = scraper.getHotelReviews(TRIPADVISOR_HOTEL_LINK);

		assertNotNull(reviews);
		assertTrue(reviews.size() > 0);
		// The number of comments can vary between execution.
		// At the moment of writing this test, August 31st 2022, the figures are:
		// Total comments: 10
		assertEquals(10, reviews.size());
		assertNotNull(reviews.get(0).getTitle());
		assertEquals("Good family holiday. drop in food quality.", reviews.get(0).getTitle());
		assertNotNull(reviews.get(0).getContent());
		assertEquals(				
				"Lower quality food ingredients vs my previous 2 visits. Everything else still good. The Suite is worth the price with room service breakfast. Kids enjoyed kids club. Attentive staff and friendly. Check-in with baby should have been smoother directly in the room. We stayed for a week in May and enjoyed a perfect holiday with incredible service and the friendliest staff you can think of. We were stunned by the availability of all GOs to make our stay as comfortable and fun as possible. Despite the situation (COVID related), all shows were on and all activities were available. We were concerned that with less attendance than usual, the staff would be taking it a bit easier but it was the exact opposite: everyone went the extra mile to make our stay an unbelievable experience: great customer centricity and professionalism from everyone. Special thanks to Jenny, Manveer, Johnson, Daniel, Gabriel, Chef Laurent, Monica, Mitsuko! You guys rock! We hope to see you again soon! My family and I just finished our vacation at Club Med Cherating and everything about this place will amaze you. The views are just breathtaking and the food and drinks will keep you coming back for more. The most amazing thing about the place are the people working there especially Melissa from reception. Thank you so much for making this trip amazing Melissa and we hope to see you again soon. Visited Club med early August this year and it is a perfect getaway for my family and I! Loved the idea of being an all-inclusive resort and my wife also really enjoyed the scenery and relaxed atmosphere in ClubMed, especially the sunrise. My son also really enjoyed the free flow of drinks and food. Of course, Club med would not be the perfect vacation spot if it wasn’t for the members of staff there! On the first day upon arrival, we went to the lovely private beach in Club med and were greeted by Niki who was very friendly. She immediately helped my family and I to register for sailing. She also introduced me to Max, Lim and Ali, the managers of this ClubMed village. I could tell that Max, Ali and Lim were all very passionate about their job and never failed to entertain and  We really enjoyed ourselves here and its an amazing vacation spot. The sceneries are beautiful and the staff are just the lovliest people available. A special thanks to the receptionist Melissa for making our trip so enjoyablr The rooms were smaller than expected but that isnt the downside. The toilets in the room do need to be better maintained and so does the air conditioning unit. Slightly dusty and defo need more sprucing up. However, big up to the staff and crew for a very welcoming and ever helpful nature. they will greet and help you at all times.definitely coming back for more! had a great time with the GOs and really enjoyed the facilities. huge thanks to the circus team GOs, Mad, Niki, Keven and Lim for being the nicest people to talk to and learn from :) the tennis class and the trapeze were such an experience and food was good too. We have been to several Club Meds in both Asia and the Caribbean and must say Cherating has been one of our favourites! The resort is beautiful, and the nature is simply stunning. We really appreciated the care all the staff has for the environment. The assistant General Manager, Mr. Lim, is an amazing guy, super fun to chat to and incredibly passionate about the work he does. The guys from rock climbing are also great, really knowledgeable and friendly. We also had the pleasure to meet Melissa from reception which made un feel at home and it was always a pleasure to go say hi to her throughout the day and have a laugh as if we’d been friends forever. We are now back in Europe and feel very nostalgic about our trip. Massive thanks to all the incredible staff. Always a fantastic getaway here at club med. Food was superb, especially the lamb. Sailing was much fun.. Friendly instructors.. A special shout out to Lim & the team on the night parties.. You guys are awesome! This was my 2nd time here and it was shooooo good to be back!!! Thanks lot to the super duper awesome, lovely and friendly GOs that doing their good parts, from the bar, to the kitchen, to event, the circus team and even the reception team! My team and I enjoyed the stay very much! Definitely worth coming to chill and relax! And definitely will be back again soon!!! Cheers!!! ",
				reviews.get(0).getContent());
		assertNotNull(reviews.get(0).getCreationDate());
		assertEquals("Mon Aug 01 00:00:00 CEST 2022", reviews.get(0).getCreationDate().toString());
		//assertNotNull(reviews.get(0).getLocation());
	}

	@Test
	public void getRestaurantReviews() {
		TripadvisorScraper scraper = new TripadvisorScraper("clubmed");
		List<TAReview> reviews = scraper.getRestaurantReviews(TRIPADVISOR_RESTAURANT_LINK);

		assertNotNull(reviews);
		assertTrue(reviews.size() > 0);
		// The number of comments can vary between execution.
		// At the moment of writing this test, August 31st 2022, the figures are:
		// Total comments: 1
		assertEquals(1, reviews.size());
		assertNotNull(reviews.get(0).getTitle());
		assertEquals("Greed and Disrespect", reviews.get(0).getTitle());
		assertNotNull(reviews.get(0).getContent());
		assertEquals(
				"I would give zero if it were possible... I live in São Paulo but returned to the UK to visit family. Whilst in the UK, I was admitted to hospital and required emergency surgery, and I was unable to fly for 6 weeks, thus unable ",
				reviews.get(0).getContent());
		assertNotNull(reviews.get(0).getCreationDate());
		assertEquals("Thu Jun 02 00:00:00 CEST 2022", reviews.get(0).getCreationDate().toString());
		//assertNotNull(reviews.get(0).getLocation());
		//assertEquals("Chester, United Kingdom", reviews.get(0).getLocation());
	}
}
