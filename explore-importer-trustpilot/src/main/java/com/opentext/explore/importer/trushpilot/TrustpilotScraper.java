package com.opentext.explore.importer.trushpilot;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.opentext.explore.importer.http.URLReader;
import com.opentext.explore.importer.trushpilot.pojo.TrustpilotReview;

public class TrustpilotScraper {

	private static final String BASE_URL="https://www.trustpilot.com/review/";
	
	private String url;
	
	/**
	 * Initialize trustpilot.com review page scraper 
	 * @param clientAlias - Client alias in <strong>trustpilot.com</strong>
	 * <i>Example</i>: bancsabadell.com
	 */
	public TrustpilotScraper(String clientAlias) {
		this(BASE_URL, clientAlias);
	}
	
	/**
	 * Initialize trustpilot.com review page scraper 
	 * @param urlBase - URL base
	 * <i>Example</i>: https://www.trustpilot.com/review/
	 * @param clienteAlias - Client alias in <strong>trustpilot.com</strong>
	 * <i>Example</i>: bancsabadell.com
	 * 
	 */
	public TrustpilotScraper(String urlBase, String clientAlias) {
		this.url = urlBase + clientAlias;
	}
	
	public List<TrustpilotReview> getReviews(){
		List<TrustpilotReview> reviews = new LinkedList<TrustpilotReview>();
		
		URLReader reader = new URLReader(url);
		String html = reader.read();
		
		Document doc = Jsoup.parse(html);
		Element script = doc.select("script[data-business-unit-json-ld=true]").first();
		
		if(script != null) {
			System.out.print("script: " + script.text());	
		}
				
		return reviews;
	}
}
