package com.opentext.explore.importer.trushpilot;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.util.FileUtil;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.DefaultPaginator.Builder;
import net.dean.jraw.pagination.Paginator;
import net.dean.jraw.references.SubredditReference;

/**
 * Trustpilot importer for OpenText Explore (Voice of the customer solution)
 * @author Joaquín Garzón
 * @since 20.2
 */
public class TrustpilotImporter {

	private static final int MILISECONDS_IN_SECOND = 1000;
	
	private String reddit = null; //TODO remove

	/** Solr URL (this Solr instance is used by Explore) */
	private String host = null; 

	protected static final Logger log = LogManager.getLogger(TrustpilotImporter.class);
	
	/**
	 * @see https://mattbdean.gitbooks.io/jraw/quickstart.html
	 */
	public TrustpilotImporter(String host) {
		this.host = host;
		
	}

	/**
	 * 
	 * @param subreddit Reddit's thread name
	 * @param rtag - Reddit Importer tag
	 * @param timeInSeconds - Seconds between each call against Reddit API
	 * @see https://mattbdean.gitbooks.io/jraw/basics.html
	 * @see https://github.com/mattbdean/JRAW/blob/master/exampleScript/src/main/java/net/dean/jraw/example/script/ScriptExample.java
	 */
	public void start(String subreddit, List<String> filters, String rtag, int timeInSeconds) {
		boolean firstTime = true;
		
		if(reddit != null) {
			int nPage = 0;
			DefaultPaginator<Submission> paginator = null;
			
			do {
				nPage = 0;
						
				try {
					log.debug("Sleeping " + timeInSeconds +  " seconds: ZZZZZZZ!");
					Thread.sleep(timeInSeconds * MILISECONDS_IN_SECOND);
				} catch (InterruptedException e) {
					log.warn(e.getMessage());
					System.exit(-1);
				}
			}while(true);
		}
	}


	
	/**
	 * Call to the /solr/interaction/otcaBatchUpdate 
	 * method provided by Solr in order to insert new content
	 * @param rtag - Reddit Importer tag (used to filter content in Explore)
	 * @param firstPage - List of the latest submissions published in Reddit
	 * @return true if the insertion in Solr was ok, false in other case. 
	 */
	protected boolean solrBatchUpdate(String rtag, Listing<Submission> firstPage) {
		boolean updated = true;
		
		String xmlPath = null;
		String xmlFileName = FileUtil.getRandomFileName(".xml");
		try {
			
			xmlPath = TrustpilotTransformer.submissionsToXMLFile(firstPage, xmlFileName, rtag);
			
			SolrAPIWrapper wrapper = null;
			if(host == null)
				wrapper = new SolrAPIWrapper();
			else {
				wrapper = new SolrAPIWrapper(host);
			}
			wrapper.otcaBatchUpdate(new File(xmlPath));	
		} catch (IOException e) {
			log.error(e.getMessage());
			updated = false;
		}
		finally {
			if(xmlPath != null) {
				FileUtil.deleteFile(xmlPath);	
			}
		}
		
		return updated;
	}
}
