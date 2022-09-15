package com.opentext.explore.importer.tripadvisor;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.importer.tripadvisor.v2.ExcelWriter;
import com.opentext.explore.util.FileUtil;

public class TripadvisorTransformer2Excell {
		
	public TripadvisorTransformer2Excell() {		
	}	
		
	protected static final Logger log = LogManager.getLogger(TripadvisorTransformer2Excell.class);	
	/**
	 * Call to the /solr/interaction/otcaBatchUpdate 
	 * method provided by Solr in order to insert new content
	 * @param rtag - Tripadvisor Importer tag (used to filter content in Explore)
	 * @param firstPage - List of the latest submissions published in Reddit
	 * @return true if the insertion in Solr was ok, false in other case. 
	 */
	public boolean process(String rtag, List<TAReview> reviews) {
		boolean updated = true;
		
		String xmlPath = null;
		String xlsFileName = FileUtil.getRandomFileName(".xls");
		try {
			
			ExcelWriter writer = new ExcelWriter();
			writer.write(rtag, reviews, xlsFileName);
			

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
