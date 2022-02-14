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
 *     Joaquín Garzón - initial implementation
 *
 */
package com.opentext.explore.importer.excel.configgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.opentext.explore.importer.excel.ISolrFields;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;
import com.opentext.explore.util.TextUtil;

public abstract class AbstractConfigGenerator  implements ISolrFields {	
	protected List<String> defaultSolrFieldNames = null;
	protected static final Logger log = LogManager.getLogger(AbstractConfigGenerator.class);
	
	
	public AbstractConfigGenerator() {
		defaultSolrFieldNames = Arrays.asList(SOLR_FIELD_REFERENCE_ID, 
				SOLR_FIELD_INTERACTION_ID, 
				SOLR_FIELD_TITLE,
				SOLR_FIELD_AUTHOR_NAME, 
				SOLR_FIELD_GROUP_HIERARCHY, 
				SOLR_FIELD_ID, 
				SOLR_FIELD_TYPE,
				SOLR_FIELD_PUBLISHED_DATE, 
				SOLR_FIELD_DATE_TIME, SOLR_FIELD_CONTENT);
	}	
	
	/**
	 * Generate a XML file using the given TextData (Excel or CSV row)
	 * 
	 * @param textDatas - List of TextData (Excel or CSV row)
	 * @param fileName  - File name of the XML that will be generated
	 * @param tag       - Tag to be added to the TextData
	 * @return Absolute path of the XML file created
	 * @throws IOException
	 */
	public String generateConfigFile(TextDataImporterMapping mapping, String fileName, String docType) throws IOException {
		String xmlPath = null;
		Document doc = textDataToDoc(mapping, docType);

		if (doc != null) {
			// Create the XML
			XMLOutputter outter = new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());

			File xmlFile = new File(fileName);
			xmlPath = xmlFile.getAbsolutePath();
			FileWriter fWriter = new FileWriter(xmlFile, StandardCharsets.UTF_8);

			outter.output(doc, fWriter);

			fWriter.close();
		}

		return xmlPath;
	}
	
	protected abstract Document textDataToDoc(TextDataImporterMapping mapping, String docType);

	protected Element createBasicElement(String fieldName, String content) {
		Element elementField = new Element(fieldName);
		elementField.addContent(content);
		return elementField;
	}	
	
	protected Element createBasicElement(String fieldName, CDATA content) {
		Element elementField = new Element(fieldName);
		elementField.addContent(content);
		return elementField;
	}
	
	protected Element createElementWith1Attribute(String fieldName, String attrName, String attrValue) {
		return createElementWith1Attribute(fieldName, attrName, attrValue, null); 
	}
	
	protected Element createElementWith1Attribute(String fieldName, String attrName, String attrValue, CDATA content) {
		Element elementField = new Element(fieldName);
		elementField.setAttribute(attrName, attrValue);
		if(content != null){
			elementField.addContent(content);
		}
		return elementField;
	}	
	
	protected String strToHumanReadable(String str) {
		if(str!= null) {
			str = TextUtil.camelCaseToHumanReadable(str);
			str = TextUtil.snakeCaseToHumanReadable(str);	
			str = str.replaceAll("\\s{2,}", " ").trim();
		}
		return str;
	}	
}
