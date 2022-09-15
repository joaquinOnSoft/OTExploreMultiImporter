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
package com.opentext.explore.importer.tripadvisor.v2.excel;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.importer.tripadvisor.pojo.TAReview;
import com.opentext.explore.importer.tripadvisor.v2.TripadvisorSolrAPIWrapper;
import com.opentext.explore.util.FileUtil;

/**
 * Tripadvisor importer for OpenText Explore (Voice of the customer solution)
 * 
 * @author Joaquín Garzón
 * @since 22.09.01
 */
public class TripadvisorImporterFromExcel {

	private static final String TRIPADVISOR_IMPORTER_TAG = "Tripadvisor";
	protected static final Logger log = LogManager.getLogger(TripadvisorImporterFromExcel.class);
	private String host;
	
	public TripadvisorImporterFromExcel(String host) {
		this.host = host;
	}

	/**
	 * @param path - Base path to look for .xls file with Tripadvisor reviews
	 */
	public void start(String path) {
		File[] files = FileUtil.filterFilesByExtension(path, ".xls");
		
		List<TAReview> reviews = null;
		ExcelReader reader = new ExcelReader();
		for(File f: files) {
			//Read excel file
			reviews = reader.read(f);
			
			log.info("MS Excell file {} readed.", f.getName());
			
			if(reviews != null) {
				log.info("# of reviews: {}", reviews.size());
				processReviews(reviews);
			}
		}
	}
		
	protected void processReviews(List<TAReview> reviews) {
		TripadvisorSolrAPIWrapper api = new TripadvisorSolrAPIWrapper(host);
		
        if(reviews != null) {
        	//TODO set tag in SOLR insert
        	api.solrBatchUpdate(TRIPADVISOR_IMPORTER_TAG, reviews);
        }
        else {
        	log.info("No reviews found!");
        }		
	}		
}
