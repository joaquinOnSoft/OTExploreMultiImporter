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

import org.jdom2.Document;
import org.jdom2.Element;

import com.opentext.explore.importer.excel.pojo.Field;
import com.opentext.explore.importer.excel.pojo.TextDataImporterMapping;

public class SolrSchemaXMLGenerator extends AbstractConfigGenerator{

	private static final String SUFFIX_SEARCH = "_search";

	public SolrSchemaXMLGenerator() {
		super();
	}
	
	/**
	 * <strong>schema.xml (Solr)</strong>
	 * The Solr configuration file schema.xml is located at 
	 * <SOLR_HOME>\solr-7.3.1\server\solr\configsets\interaction_config e.g.
	 * 
	 *    D:\SolrCloud\solr-7.3.1\server\solr\configsets\interaction_config
	 * 
	 * <strong>New fields on Solr</strong>
	 * We must define new fields to be able to import extra metadata related with each input.
	 * Let's see an example:
	 * <code>
	 *   <!-- ADD YOUR CUSTOM FIELDS HERE -->
	 * 
	 *   <field name="subreddit" type="string" indexed="true" stored="false" docValues="true" />
	 *   <field name="subreddit_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
	 *   <copyField source="subreddit" dest="subreddit_search" />
	 * 
	 *   <field name="score" type="pint" indexed="true" stored="false" docValues="true" />
	 *   <field name="score_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
	 *   <copyField source="score" dest="score_search" />
	 * 
	 *   <field name="permalink" type="string" indexed="true" stored="false" docValues="true" />
	 *   <field name="permalink_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
	 *   <copyField source="permalink" dest="permalink_search" />
	 *   
	 *   <field name="url" type="string" indexed="true" stored="false" docValues="true" />
	 *   <field name="url_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
	 *   <copyField source="url" dest="url_search" />
	 *   
	 *   <field name="thumbnail" type="string" indexed="true" stored="false" docValues="true" />
	 *   <field name="thumbnail_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
	 *   <copyField source="thumbnail" dest="thumbnail_search" />   
	 *   
	 *   <field name="rtag" type="string" indexed="true" stored="false" docValues="true" />
	 *   <field name="rtag_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
	 *   <copyField source="rtag" dest="rtag_search" />
	 * 
	 * 
	 *   <!-- END CUSTOM FIELDS -->
	 * </code>
	 * Generates an XML called <strong>schema.xml</strong> with the 
	 * new content that must be added to the Solr configuration file with the same name.
	 */
	@Override
	protected Document textDataToDoc(TextDataImporterMapping mapping, String tag) {
		Document doc = null;

		if (mapping != null && mapping.getFields() != null && mapping.getFields().size() > 0) {
			doc = new Document();
			
			Element eSchema = new Element("schema");
			eSchema.setAttribute("name", "default-config");
			eSchema.setAttribute("version", "1.6");
			
			for (Field field : mapping.getFields()) {
				if(field.getSkip() == false && !defaultSolrFieldNames.contains(field.getSolrName())) {										
					eSchema.addContent(createElementField(field));
					eSchema.addContent(createElementFieldSearch(field));
					eSchema.addContent(createElementCopyField(field));
				}
			}

			doc.addContent(eSchema);
		}

		return doc;
	}
	
	/**
	 * Generates and element that looks like this:
	 * <code>
	 *    <field name="rtag" type="string" indexed="true" stored="false" docValues="true" />
	 * </code>   
	 * @param field - Input field configuration
	 * @return XML element
	 */
	protected Element createElementField(Field field) {
		Element eField = new Element("field");
		
		//Set attributes
		eField.setAttribute("name", field.getSolrName());
		
		if(field.getType() != null && field.getType().compareToIgnoreCase("integer") == 0) {
			eField.setAttribute("type", "pint");	
		}
		else {
			//string or date
			eField.setAttribute("type", "string");
		}
		
		eField.setAttribute("indexed", "true");
		eField.setAttribute("stored", "false");
		eField.setAttribute("docValues", "true");
		
		return eField;
	}
	
	/**
	 * Generates and element that looks like this:
	 * <code>
	 *    <field name="subreddit_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
	 * </code>   
	 * @param field - Input field configuration
	 * @return XML element
	 */	
	protected Element createElementFieldSearch(Field field) {
		Element eField = new Element("field");
		
		//Set attributes
		eField.setAttribute("name", field.getSolrName() + SUFFIX_SEARCH);
		eField.setAttribute("type", "explore_filter_text");
		eField.setAttribute("indexed", "true");
		eField.setAttribute("stored", "false");
		eField.setAttribute("multiValued", "true");
		
		return eField;
	}
	
	/**
	 * Generates and element that looks like this:
	 * <code>
	 *    <copyField source="subreddit" dest="subreddit_search" />
	 * </code>   
	 * @param field - Input field configuration
	 * @return XML element
	 */	
	protected Element createElementCopyField(Field field) {
		Element eField = new Element("copyField");
		
		//Set attributes
		eField.setAttribute("source", field.getSolrName());
		eField.setAttribute("dest", field.getSolrName() + SUFFIX_SEARCH);
		
		return eField;
	}	
}
