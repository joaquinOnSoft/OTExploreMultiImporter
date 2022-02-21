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
package com.opentext.explore.importer.trushpilot;

import org.junit.Test;

import com.opentext.explore.importer.trustpilot.TrustpilotImporter;

public class TestTrustpilotImporter {

	@Test
	public void testCreateMonthlyPaginator() {
		TrustpilotImporter redditImp = new TrustpilotImporter("http://localhost:8983");
		//DefaultPaginator<Submission> paginator = redditImp.createMonthlyPaginator("CanadaPost");
		
		//assertNotNull(paginator);
		//assertTrue(TimePeriod.MONTH == paginator.getTimePeriod());
	}	

	@Test
	public void testCreateHourlyPaginator() {
		TrustpilotImporter redditImp = new TrustpilotImporter("http://localhost:8983");
		//DefaultPaginator<Submission> paginator = redditImp.createHourlyPaginator("CanadaPost");
		
		//assertNotNull(paginator);
		//assertTrue(TimePeriod.HOUR == paginator.getTimePeriod());
	}		
}
