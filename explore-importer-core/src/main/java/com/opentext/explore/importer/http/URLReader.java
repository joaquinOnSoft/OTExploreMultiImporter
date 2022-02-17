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
package com.opentext.explore.importer.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class URLReader {

	private HttpGet request;
	
	protected static final Logger log = LogManager.getLogger(URLReader.class);
		
	public URLReader(String url) {		
        request = new HttpGet(url);
        log.info("URL: " + url);        
	}

	public String read() {
		String result = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(request);

			// Get HttpResponse Status
			log.info(response.getProtocolVersion());              // HTTP/1.1
			log.info(response.getStatusLine().getStatusCode());   // 200
			log.info(response.getStatusLine().getReasonPhrase()); // OK
			log.info(response.getStatusLine().toString());        // HTTP/1.1 200 OK

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// return it as a String
				result = EntityUtils.toString(entity);
				log.debug(result);
			}
		}
		catch (IOException e) {
			log.info(" ", e);
		}
		
		return result;
	}
}
