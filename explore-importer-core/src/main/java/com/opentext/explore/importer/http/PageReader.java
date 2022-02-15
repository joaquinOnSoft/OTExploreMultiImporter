package com.opentext.explore.importer.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PageReader {

	private HttpGet request;

	public PageReader(String ur) {

	}

	public String read() {
		String result = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(request);

			// Get HttpResponse Status
			System.out.println(response.getProtocolVersion());              // HTTP/1.1
			System.out.println(response.getStatusLine().getStatusCode());   // 200
			System.out.println(response.getStatusLine().getReasonPhrase()); // OK
			System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// return it as a String
				result = EntityUtils.toString(entity);
				System.out.println(result);
			}

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
}
