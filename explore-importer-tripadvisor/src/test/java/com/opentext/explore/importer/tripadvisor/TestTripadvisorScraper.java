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
	private static final String TRIPADVISOR_HOTEL_LINK_WITH_40_REVIEWS = "https://www.tripadvisor.com/Hotel_Review-g298296-d2193909-Reviews-LaVilla_by_Holiday_Villa_Cherating-Kuantan_Kuantan_District_Pahang.html";
	private static final String TRIPADVISOR_HOTEL_LINK_WITH_4900_REVIEWS = "https://www.tripadvisor.com/Hotel_Review-g147293-d149864-Reviews-Club_Med_Punta_Cana-Punta_Cana_La_Altagracia_Province_Dominican_Republic.html";
	
	private TripadvisorScraper scraper = new TripadvisorScraper("clubmed");
	
	@Test
	public void getHotelReviewsWith4900Reviews() {
		List<TAReview> reviews = scraper.getHotelReviews(TRIPADVISOR_HOTEL_LINK_WITH_4900_REVIEWS);

		assertNotNull(reviews);
		// The number of comments can vary between execution.
		// At the moment of writing this test, September 1st 2022, the figures are:
		// Total comments: 4902		
		assertTrue(reviews.size() > 0);
		assertEquals(4902, reviews.size());
		
		assertEquals("857695402", reviews.get(0).getId());
		assertEquals("Simon L", reviews.get(0).getAuthor());
		assertNotNull(reviews.get(0).getTitle());
		assertEquals("Good stay", reviews.get(0).getTitle());
		assertNotNull(reviews.get(0).getContent());
		assertEquals(				
				"Thanks to GOs Vanessa, Nella, Émilie, Morgane, Mélissa, Edwin, Nathy and Soleyman (Zen chief)! The people and the service onsite were awesome. Guillaume is also an excellent future village chief. Thanks to Christelle & Annie from guest relations Canada. -Simon & Jack",
				reviews.get(0).getContent());
		assertEquals(5, reviews.get(0).getRating());
		assertNotNull(reviews.get(0).getCreationDate());
		assertEquals("Mon Aug 01 00:00:00 CEST 2022", reviews.get(0).getCreationDate().toString());
		//assertNotNull(reviews.get(0).getLocation());
	}	

	
	
	@Test
	public void getHotelReviewsWith40Reviews() {
		List<TAReview> reviews = scraper.getHotelReviews(TRIPADVISOR_HOTEL_LINK_WITH_40_REVIEWS);

		assertNotNull(reviews);
		assertTrue(reviews.size() > 0);
		assertEquals("847935416", reviews.get(0).getId());
		assertEquals("Mike W", reviews.get(0).getAuthor());
		// The number of comments can vary between execution.
		// At the moment of writing this test, September 1st 2022, the figures are:
		// Total comments: 36		
		assertEquals(36, reviews.size());
		assertNotNull(reviews.get(0).getTitle());
		assertEquals("Sadly just average…..", reviews.get(0).getTitle());
		assertNotNull(reviews.get(0).getContent());
		assertEquals(				
				"Feel bad about not giving this place a better score. But just can’t. Too many issues we couldnt ignore makes this place “average”……. We were booked into regular poolside villa/room but once we got to it there was a lot wrong with it. Plumbing dodgy, TV didn’t work, generally just in a poor state of repair and generally old and tired On complaining, we were upgraded without fuss or delay to a simply beautiful garden villa with private plunge pool, and that action alone should have guaranteed this place a better review. Sadly too many other things let the place down. The service in the restaurant and room service is frankly awful, you could could go old and grey waiting for your food. And on the one occasion we ordered room service meal to be delivered to the poolside, which was  Went on short trip during Eid holidays with family, 2 nights. Overall trip was good, just the place seems aged a bit and restaurant service really need improvement. Room/Chalet - the house we stayed in named Negeri Sembilan. Two bedroom and one living. Its a chalet by near the beach which I prefer as more convenient compared staying in hotel room. Room is nice, mostly working condition except main bathroom water drainage wasn't good enough. Probably it aged over time. Would really recommend an upgrade here. Living area is small but not much an issue, just AC need servicing I guess. There's small pool size probably 12\" x 9\", its perfect for kids as the depth around 4ft. Location: Great, just by the main federal road. Many nearby local convenient store around. There are also  My Brother (lives in Australia), my cousin n hubby (live in Holland) and i stayed at the LaVilla and we were in awe of the place. Beautiful Chalets and luscious landscape and the warmest service by the staff will linger in our minds for years to come.. Thank You LaVilla for giving my guests a memorable stay. Perfect getaway only 3 hours from KL with plenty of nearby sundry shops and eateries. BYOB suggested though. Beautiful classical styled chalets with clean & huge jacuzzi pool for all 20 of us...we stayed quiet as quiet as possible.Bathrooms are dated but have been reassured that it will be upgraded in the near future for our trip back. Wide grounds and beach were well maintained with plenty of monsoon permissible activities. Revolving breakfast menus, delicious food Western and local cuisine allowed us to just lay by the infinity pool all day while the kids spent their energy at the super fun slides and water fountains. Gloomy monsoon rains for couple of hours were made up by warm and courteous staff particularly Resort GM Siva, F&B manager Bob and staff Ridzuan, Adib and  It’s clear that the property has passed its heyday. The villa looks and feels dated saves the huge internet TVs which are real delights. Staffs didn’t clean the villa properly. Floor felt dusty and toilet bowls were stained and yellowish. Breakfast was chaotic. We waited for tables with no one bother to come to apologize and acknowledge our presence when a few of them walked passed us (including I believed the property manager). I can understand that attitude if it’s a cheap motel but this? Need to train all staffs (and manager?) better. Have to say, the ground is beautiful! Good job on that. But there are lots of mosquitoes so please work on that. Excellent place for a few nights away. LaVilla chalets gives you the \"kampung\" experience in a very cosy way. Go for the Chalets if you can, it comes with a private pool. The swimming pool is very inviting as well, unfortunately i couldn't use it as it was still under MCO period. Looking forward to return just for the swimming pool. What can I say. \"A picture is worth a thousand words\". Unbelievable experience we had over at this beautiful place. Hotel staff are all super friendly and had an opportunity to meet with the Resort GM Mr.Siva who was were helpful throughout our stay. Will definitely recommend to all my contacts of this beautiful resort. Looking forward to another trip over there again. Green and peaceful place...I’ve stayed 2 night with family and it was wonderful..my sisters enjoyed the private pool so much...I went to the yoyoy spa and was great..very relaxing...recommended place to visit The hotel employees was very friendly! As Deaf person the worker went further to communicate with us to make thing go smoothly! Would definitely coming back soon as possible!! Thank you for having us! Beautiful place to stay in Cherating. So far there are many places to choose from if you intend to spend the night in Cherating. Attraction availables from watching fireflies at sg cherating to visit turtle sanctuary center beside clubmed. Beatiful beaches and swimming pools could be the attraction in this villa. Delicious food to try is keropok lekor rebus stall along the road ",
				reviews.get(0).getContent());
		assertEquals(3, reviews.get(0).getRating());
		assertNotNull(reviews.get(0).getCreationDate());
		assertEquals("Fri Jul 01 00:00:00 CEST 2022", reviews.get(0).getCreationDate().toString());
		//assertNotNull(reviews.get(0).getLocation());
	}	

	@Test
	public void getRestaurantReviews() {
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
	
	@Test
	public void getRatingFromClass() {
		assertEquals(1, scraper.getRatingFromClass("ui_bubble_rating bubble_10"));
		assertEquals(2, scraper.getRatingFromClass("ui_bubble_rating bubble_20"));
		assertEquals(3, scraper.getRatingFromClass("ui_bubble_rating bubble_30"));
		assertEquals(4, scraper.getRatingFromClass("ui_bubble_rating bubble_40"));
		assertEquals(5, scraper.getRatingFromClass("ui_bubble_rating bubble_50"));		
	}
}
