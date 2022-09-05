package com.opentext.explore.importer.tripadvisor.multithread;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.importer.tripadvisor.TripadvisorTransformer;
import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.util.FileUtil;

public class TripadvisorSolrAPIWrapper {
	/** Solr URL (this Solr instance is used by Explore) */
	private String host = null; 
	
	public TripadvisorSolrAPIWrapper() {		
	}	
	
	/**
	 * @param host - Solr URL (this Solr instance is used by Explore) 
	 */
	public TripadvisorSolrAPIWrapper(String host) {
		this.host = host;		
	}	
	
	protected static final Logger log = LogManager.getLogger(TripadvisorSolrAPIWrapper.class);	
	/**
	 * Call to the /solr/interaction/otcaBatchUpdate 
	 * method provided by Solr in order to insert new content
	 * @param rtag - Reddit Importer tag (used to filter content in Explore)
	 * @param firstPage - List of the latest submissions published in Reddit
	 * @return true if the insertion in Solr was ok, false in other case. 
	 */
	public boolean solrBatchUpdate(String rtag, List<TAReview> reviews) {
		boolean updated = true;
		
		String xmlPath = null;
		String xmlFileName = FileUtil.getRandomFileName(".xml");
		try {
			
			xmlPath = TripadvisorTransformer.reviewsToXMLFile(reviews, xmlFileName, rtag);
			
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
