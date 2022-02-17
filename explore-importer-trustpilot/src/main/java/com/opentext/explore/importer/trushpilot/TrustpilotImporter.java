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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.util.FileUtil;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;

/**
 * Trustpilot importer for OpenText Explore (Voice of the customer solution)
 * @author Joaquín Garzón
 * @since 22.02.16
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
