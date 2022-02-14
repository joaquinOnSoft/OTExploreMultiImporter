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

public class ExploreConfigurationXMLGenerator  extends AbstractConfigGenerator{

	public ExploreConfigurationXMLGenerator() {
		super();
	}

	/**
	 * Generates an XML called <strong>Explore.Configuration.xml</strong> with the 
	 * new content that must be added to the Explore config file with the same name.
	 * 
	 * Let's see and example:
	 *  <Explore>
	 *    <DocTypes>
	 *  
	 *      ...
	 *      
	 *      <DocType>
	 *        <Name>Reddit</Name>
	 *        <GridFields>
	 *          <Field column="Source">
	 *            <Name>Subreddit</Name>
	 *            <Tag>subreddit</Tag>
	 *          </Field>
	 *          <Field column="Source">
	 *            <Name>Score</Name>
	 *            <Tag>score</Tag>
	 *          </Field>
	 *          <Field column="Source">
	 *            <Name>Permalink</Name>
	 *            <Tag>permalink</Tag>
	 *          </Field>
	 *        </GridFields>
	 *      </DocType>	
	 *    
	 *      ...
	 *    
	 *    </DocTypes> 
	 *    
	 *  <CriteriaItems>
	 *  
	 *    ...
	 *      
	 *    <Group name="Reddit">	             
	 *      <CriteriaItem parametric="true" groupBy ="alphabetical" numberBuckets="20">
	 *        <Name>Subreddit</Name>
	 *        <Tag>subreddit</Tag>
	 *        <ComparatorGroup>string</ComparatorGroup>
	 *        <AssociatedDocTypes>
	 *          <DocType>Reddit</DocType>
	 *        </AssociatedDocTypes>
	 *      </CriteriaItem>
	 *
	 *      <CriteriaItem parametric="true" groupBy ="numeric" numberBuckets="10" advancedSearch="true" numericStats="true">
	 *        <Name>Score</Name>
	 *        <Tag>score</Tag>
	 *        <ComparatorGroup>numeric</ComparatorGroup>
	 *        <AssociatedDocTypes>
	 *          <DocType>Reddit</DocType>
	 *        </AssociatedDocTypes>		
	 *      </CriteriaItem>	  
	 *
	 *    </Group>  
	 *  
	 *    ...
	 *  
	 *  </CriteriaItems> 
	 *      
	 *    ...
	 *          
	 *  </Explore>
	 */
	@Override
	protected Document textDataToDoc(TextDataImporterMapping mapping, String docType) {
		Document doc = null;

		if (mapping != null && mapping.getFields() != null && mapping.getFields().size() > 0) {

			doc = new Document();
			// Root Element
			Element eExplore = new Element("Explore");
			Element eDocTypes = new Element("DocTypes");
			Element eDocType = new Element("DocType");
			Element eName = createBasicElement("Name", docType);
			eDocType.addContent(eName);
			
			Element eGridFields = new Element("GridFields");
			
			Element eCriteriaItems = new Element("CriteriaItems");
			Element eGroup  = createElementWith1Attribute("Group", "name", docType);
			
			for (Field field : mapping.getFields()) {
				if(field.getSkip() == false && !defaultSolrFieldNames.contains(field.getSolrName())) {										
					eGridFields.addContent(createElementField(field));	
					eGroup.addContent(createElementCriteriaItem(field, docType));
				}
			}
			
			eDocType.addContent(eGridFields);						
			eDocTypes.addContent(eDocType);
			eExplore.addContent(eDocTypes);
			
			eCriteriaItems.addContent(eGroup);
			eExplore.addContent(eCriteriaItems);			
			
			doc.addContent(eExplore);
		}

		return doc;
	}

	private Element createElementField(Field field) {		
		Element eField = createElementWith1Attribute("Field", "column", "Source");
						
		Element eName2 = createBasicElement("Name", strToHumanReadable(field.getSolrName()));					
		Element eTag = createBasicElement("Tag", field.getSolrName());
		
		eField.addContent(eName2);
		eField.addContent(eTag);
		
		return eField;
	}
	
	private Element createElementCriteriaItem(Field field, String docType) {
		Element eCriteriaItem = new Element("CriteriaItem");

		//Setting attributes
		eCriteriaItem.setAttribute("parametric", "true");
		
		switch(field.getType()) {
		case "integer":
			eCriteriaItem.setAttribute("groupBy", "numeric");
			eCriteriaItem.setAttribute("advancedSearch", "true");
			eCriteriaItem.setAttribute("numericStats", "true");
			break;
		case "date":
			//Intentionally empty
		case "string":
			//Intentionally empty			
		default:
			eCriteriaItem.setAttribute("groupBy", "alphabetical");
		}
		
		eCriteriaItem.setAttribute("numberBuckets", "20");

		// Add children
		Element eName = createBasicElement("Name", strToHumanReadable(field.getSolrName()));					
		Element eTag = createBasicElement("Tag", field.getSolrName());
		
		Element eComparatorGroup = null;
		switch(field.getType()) {
		case "integer":
			eComparatorGroup = createBasicElement("ComparatorGroup", "numeric");
			break;
		case "date":
			//Intentionally empty
		case "string":
			//Intentionally empty			
		default:
			eComparatorGroup = createBasicElement("ComparatorGroup", "string");
		}		
		
		Element eAssociatedDocTypes = new Element("AssociatedDocTypes");
		Element eDocType = createBasicElement("DocType", docType);		
		eAssociatedDocTypes.addContent(eDocType);
		
		eCriteriaItem.addContent(eName);
		eCriteriaItem.addContent(eTag);
		eCriteriaItem.addContent(eComparatorGroup);
		eCriteriaItem.addContent(eAssociatedDocTypes);
		
		return eCriteriaItem;
	}

}
