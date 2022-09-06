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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class URLReader {

	private HttpGet request;
	private CookieStore cookieStore;
		
	protected static final Logger log = LogManager.getLogger(URLReader.class);

	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final String HEADER_REFERRER_POLICY = "Referrer Policy";
	
	/**
	 * https://www.whatismybrowser.com/guides/the-latest-user-agent/chrome
	 **/
	public static final String LATEST_CHROME_ON_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";
	public static final String STRICT_ORIGIN_WHEN_CROSS_ORIGIN = "strict-origin-when-cross-origin";
	
	public URLReader(String url) {		
        request = new HttpGet(url);
        log.info("URL: " + url);        
	}
	
	public void setHeader(String name, String value) {
		request.setHeader(name, value);
	}

	/**
	 * @param name - Coockie name
	 * @see Accessing Cookies - https://www.baeldung.com/java-apache-httpclient-cookies#accessing-cookies
	 */
	public Cookie getCoockieByName(String name) throws IllegalStateException{
		Cookie customCookie = cookieStore.getCookies().stream()
	        .peek(cookie -> log.info("cookie name:{}:\t {}", cookie.getName(), cookie.getValue()))
	        .filter(cookie -> name.equals(cookie.getName()))
	        .findFirst()
	        .orElseThrow(IllegalStateException::new);
		
		return customCookie;
	}
	
	public String read() {
		String result = null;

        // How can I get the cookies from HttpClient?
        // https://stackoverflow.com/questions/8733758/how-can-i-get-the-cookies-from-httpclient
        //
        // 3. Accessing Cookies
        // https://www.baeldung.com/java-apache-httpclient-cookies#accessing-cookies
		HttpClientContext context = HttpClientContext.create();
		context.setAttribute(HttpClientContext.COOKIE_STORE, new BasicCookieStore());		
			
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			try {
				try {
					
					response = httpClient.execute(request, context);
		
					cookieStore = context.getCookieStore();
					
					// Get HttpResponse Status
					log.info(response.getProtocolVersion());              // HTTP/1.1
					log.info(response.getStatusLine().getStatusCode());   // 200
					log.info(response.getStatusLine().getReasonPhrase()); // OK
					log.info(response.getStatusLine().toString());        // HTTP/1.1 200 OK
		
					HttpEntity entity = response.getEntity();			
					if (entity != null) {
						// return it as a String
						result = EntityUtils.toString(entity);
						//log.debug(result);
					}
				}
				catch (ClientProtocolException e) {
					log.info("Read URL (Client Protocol Exception: ", e);
				}
				catch (IOException e) {
					log.info("Read URL: ", e);
				}
				finally{
					if(response != null) {
						response.close();
					}
				}		
			}finally{
				if(httpClient != null) {
					httpClient.close();
				}
			}
		}
		catch (IOException e) {
			log.info("Closing connection: ", e);
		}
		
		return result;
	}
}
