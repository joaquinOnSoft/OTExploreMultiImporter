package com.opentext.explore.importer.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
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
