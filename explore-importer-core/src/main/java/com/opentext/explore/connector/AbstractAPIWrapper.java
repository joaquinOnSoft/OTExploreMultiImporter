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
 *     Joaqu�n Garz�n - initial implementation
 *
 */
package com.opentext.explore.connector;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Joaqu�n Garz�n
 * @since 20.2
 */
public abstract class AbstractAPIWrapper {

	protected static final Logger log = LogManager.getLogger(AbstractAPIWrapper.class);

	protected String apiKey;

	public AbstractAPIWrapper() {
	}

	/**
	 * SEE: Apache HttpClient Examples
	 * https://mkyong.com/java/apache-httpclient-examples/
	 * @param request
	 * @return
	 */
	protected String execute(HttpRequestBase request) {
		String result = null;
		CloseableHttpResponse response = null;

		CloseableHttpClient httpClient = HttpClients.createDefault();
				
		try {			
			response = httpClient.execute(request);

			// Get HttpResponse Status
			log.debug(response.getProtocolVersion());              // HTTP/1.1
			log.debug(response.getStatusLine().getStatusCode());   // 200
			log.debug(response.getStatusLine().getReasonPhrase()); // OK
			log.debug(response.getStatusLine().toString());        // HTTP/1.1 200 OK

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// return it as a String
				result = EntityUtils.toString(entity);
				log.info(result);
			}

		} 
		catch (IOException e) {
			log.error(e.toString());
		}
		finally {
			try {
				if(response != null) {
					response.close();
				}
				httpClient.close();
			}
			catch(IOException e) {
				log.error(e.toString());				
			}
		}

		return result;
	}


	protected Object jsonStringToObject(String json, Class<?> toClass) {
		if(json == null) {
			return null;	
		}
		else {		
			// Jackson 2 � Convert Java Object to / from JSON
			// https://www.mkyong.com/java/jackson-2-convert-java-object-to-from-json/
			Object obj = null;

			ObjectMapper mapper = new ObjectMapper();
			//JSON string to Java Object
			try {
				obj = mapper.readValue(json.toString(), toClass);
			} catch (IOException e) {
				log.warn("API response (JSON Str to Object): " + e.getMessage(), e);
				return null;
			}
			return obj;
		}
	}

}