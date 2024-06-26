/*
 *   (C) Copyright 2021 OpenText and others.
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
 *     Joaqu�n Garz�n - initial implementation
 *
 */
package com.opentext.explore.importer.excel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.importer.excel.pojo.TextData;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;
import com.opentext.explore.util.FileUtil;

public class ExcelImporter {
	/** Solr URL (this Solr instance is used by Explore) */
	private String host = null;

	protected static final Logger log = LogManager.getLogger(ExcelImporter.class);

	/**
	 * 
	 * @param host - Solr URL (this Solr instance is used by Explore)
	 */
	public ExcelImporter(String host) {
		this.host = host;
	}
	
	
	public void start(String excelPaht, String configPath, String contentType, String tag, String language) {
		log.debug("Reading config file: " + configPath);
		JSonMappingConfigReader configReader = new JSonMappingConfigReader();
		TextDataImporterMapping mapping = configReader.read(configPath);
		
		if(mapping != null) {
			log.debug("Reading Excel file: " + excelPaht);
			ExcelReader excelReader = new ExcelReader();
			List<TextData> txtDatas = excelReader.read(excelPaht, mapping, contentType);
			
			if(txtDatas != null && txtDatas.size() > 0) {
				log.debug(txtDatas.size() + " excel rows readed");
				log.debug("Calling Solr method: /solr/interaction/otcaBatchUpdate ");
				solrBatchUpdate(tag, language, txtDatas);
			}
			else {
				log.info("Excel file doesn't exists or it doensn't contains data");
			}
		} 
		else {
			log.info("JSON configuration file doesn't exists or contains errors");
		}
	}

	/**
	 * Call to the /solr/interaction/otcaBatchUpdate method provided by Solr in
	 * order to insert new content
	 * 
	 * @param tag      - Excel Importer tag (used to filter content in Explore)
	 * @param language
	 * @param txtDatas - List of Text Data (Excel or CSV row)
	 * @return true if the insertion in Solr was OK, false in other case.
	 */
	protected boolean solrBatchUpdate(String tag, String language, List<TextData> txtDatas) {
		boolean updated = true;

		String xmlPath = null;
		String xmlFileName = FileUtil.getRandomFileName(".xml");
		try {

			xmlPath = ExcelTransformer.textDatasToXMLFile(txtDatas, xmlFileName, tag, language);
			log.debug("Temp XML file generated: " + xmlPath);
			
			SolrAPIWrapper wrapper = null;
			if (host == null)
				wrapper = new SolrAPIWrapper();
			else {
				wrapper = new SolrAPIWrapper(host);
			}
			wrapper.otcaBatchUpdate(new File(xmlPath));
		} catch (IOException e) {
			log.error(e.getMessage());
			updated = false;
		} finally {
			if (xmlPath != null) {
				log.debug("Removing temp XML: " + xmlPath);
				FileUtil.deleteFile(xmlPath);
			}
		}

		return updated;
	}
}
