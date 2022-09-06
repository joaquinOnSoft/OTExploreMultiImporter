package com.opentext.explore.importer.tripadvisor.v1;

import java.util.LinkedList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.opentext.explore.importer.tripadvisor.AbstractTripadvisorScraper;

public class TripadvisorScraperSearch extends AbstractTripadvisorScraper {
	
	private static final String SEARCH_URL="/Search?q=";
		
	public TripadvisorScraperSearch() {
		super();
	}
	
	public List<String> search(String searchTerm){
		List<String> links = null;
		HtmlPage page = null;
		url = BASE_URL + SEARCH_URL + searchTerm;	//URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);

		//Reach search page with the given search criteria
		page = readSearchPage(url);
		
		if(page != null) {
			//Recover links to hotels, restaurants... that match the search criteria
			links = getListSearchResults(page);
		}		
		
		return links;
	}
	
	/**
	 * Read Tripadvisor search page.
	 * <strong>NOTE</strong>: It requires 2 reads of the page.
	 *    - 1st read doesn't includes the 'searchSessionId'. It's generated in 
	 *      the client browser (executing js code), so we need library that 
	 *      emulates a browser, not just a pure URL reader 
	 *    - 2nd read includes the 'searchSessionId' recovered from the 1st read
	 * This behavior force us to use an additional library called <strong>HTMLUnit</strong>
	 * @param pageURL - Tripadvisor search URL 
	 * @return
	 */
	private HtmlPage readSearchPage(String pageURL) {
		log.info("Read page: " + pageURL);
		HtmlPage page = null;

		if(webClient != null) {		
			int count= 0;
			String searchSessionId = null;			

			do {
				if (searchSessionId != null) {
					pageURL += "&searchSessionId=" + searchSessionId;
				}

				page = readPage(pageURL);

				if(page != null) {
					List<DomElement> nodeList = page.getElementsByName("searchSessionId");
					for (DomElement element : nodeList) {
						log.debug("searchSessionId: " + element.getAttribute("value"));
						searchSessionId = element.getAttribute("value");
					}				
				}
				count++;
			}
			while(count < 2);
		}

		return page;
	}

	private List<String> getListSearchResults(HtmlPage page){
		List<String> searchResults = null;
		if(page != null) {
			int start, end= -1;
			String onClicTxt=null;
			String link = null;

			List<DomElement> resultTitles = page.getByXPath("//div[@class='location-meta-block']/div[@class='result-title']");
			//List<DomElement> showMoreList = page.getByXPath("//a[@class='ui_button nav next primary ']");
			
			for (DomElement element : resultTitles) {
				onClicTxt = element.getAttribute("onclick");

				start = onClicTxt.indexOf("/");
				end = onClicTxt.indexOf("', {type");

				if(start != -1 && end != -1) {
					link = BASE_URL + onClicTxt.substring(start, end);
					log.debug("Search result (link): {}", link);

					if(searchResults == null) {
						searchResults = new LinkedList<String>();
					}

					searchResults.add(link);
				}
			}
			
			/*
			//Manage pagination. Get additional results
			if(showMoreList != null && showMoreList.size() > 0) {
				log.info("Reading addtional links...");
				try {
					HtmlPage nextPage = showMoreList.get(0).click();
					if(nextPage != null) {
						List<String> linksShowMore = getListSearchResults(nextPage);
						if(linksShowMore != null && linksShowMore.size() > 0) {
							searchResults.addAll(linksShowMore);
						}
					}
				} catch (IOException e) {
					log.error("Error reading next search results page: ", e);
				}
			}
			*/
		}
		return searchResults;
	}
}
