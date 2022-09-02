package com.opentext.explore.importer.tripadvisor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class AbstractTripadvisorScraper {

	private static final String LANGUAGE_ENGLISH = "en";
	protected static final Logger log = LogManager.getLogger(TripadvisorScraper.class);
	protected static final String BASE_URL = "https://www.tripadvisor.com";
	protected String url;
	protected WebClient webClient;

	public AbstractTripadvisorScraper() {		
		//Disable logs for 'HTMLUnit' library, is too noisy. 
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);		

		initWebClient();	
	}
	
	protected void initWebClient() {
		//Initialize 'HTMLUnit' web client
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setThrowExceptionOnScriptError(false);		
		webClient.getCookieManager().setCookiesEnabled(true);
		//TODO make language accepted a parameter
		webClient.addRequestHeader(HttpHeader.ACCEPT_LANGUAGE, LANGUAGE_ENGLISH);
	}

	protected void resetWebClient() {
		webClient.getCookieManager().clearCookies();
		webClient.close();
		webClient = null; //Help garbage collector
		
		initWebClient();		
	}

	/**
	 * Read a HTML page using HTMLUnit library
	 * @param pageURL - page URL
	 * @return
	 */
	protected HtmlPage readPage(String pageURL) {
		HtmlPage page = null;
	
		log.info("Reading page: " + pageURL);
	
		try {			
			page = webClient.getPage(pageURL);
		} catch (FailingHttpStatusCodeException | IOException e) {
			log.error("Initializing web client: ", e);
		}
	
		return page;
	}

	@Override
	protected void finalize() throws Throwable {
		if(webClient != null) {
			webClient.close();
		}
	}

	protected String getTextContentFromElementByXPath(DomElement element, String xPahtExpr) {
		List<DomElement> elementsFound = element.getByXPath(xPahtExpr);
		String value = null;
	
		if(elementsFound != null && elementsFound.size() > 0) {
			value = elementsFound.get(0).asNormalizedText();
		}
	
		return value;
	}
}