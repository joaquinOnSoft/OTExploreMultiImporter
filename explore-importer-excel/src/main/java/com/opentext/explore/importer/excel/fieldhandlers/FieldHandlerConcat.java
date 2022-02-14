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
package com.opentext.explore.importer.excel.fieldhandlers;

import java.util.List;

import com.opentext.explore.importer.excel.pojo.TextData;

public class FieldHandlerConcat extends AbstractFieldHandler {

	/**
	 * Concatenate the input fields values and assign the result to the output field
	 * 
	 * @param txtData      - Text data container (represents a row in an Excel or
	 *                     CSV file)
	 * @param inputFields  - Input fields to be concatenated
	 * @param outputFields - Output field where the concatenation value will be
	 *                     assigned
	 */
	@Override
	public TextData handle(TextData txtData, List<String> inputFields, List<String> outputFields) {
		StringBuilder strBuilder = new StringBuilder();

		for (String inputField : inputFields) {

			if (strBuilder.length() > 0) {
				// Add separator between fields
				strBuilder.append(" ");
			}

			switch (inputField) {
			case SOLR_FIELD_REFERENCE_ID:
				strBuilder.append(txtData.getReferenceId());
				break;
			case SOLR_FIELD_INTERACTION_ID:
				strBuilder.append(txtData.getInteractionId());
				break;
			case SOLR_FIELD_TITLE:
				strBuilder.append(txtData.getTitle());
				break;
			case SOLR_FIELD_AUTHOR_NAME:
				strBuilder.append(txtData.getAuthorName());
				break;
			case SOLR_FIELD_GROUP_HIERARCHY:
				strBuilder.append(txtData.getGroupHierarchy());
				break;				
			case SOLR_FIELD_ID:
				strBuilder.append(txtData.getId());
				break;
			case SOLR_FIELD_TYPE:
				strBuilder.append(txtData.getType());
				break;
			case SOLR_FIELD_PUBLISHED_DATE:
				strBuilder.append(txtData.getPublishedDate());
				break;
			case SOLR_FIELD_DATE_TIME:
				strBuilder.append(txtData.getDateTime());
				break;
			case SOLR_FIELD_CONTENT:
				strBuilder.append(txtData.getContent());
				break;
			default:
				strBuilder.append(txtData.getField(inputField));
			}
		}
		
		String fieldValue = strBuilder.toString().replace("NULL", "");
		for (String outputField : outputFields) {
			log.debug("Setting field '" + outputField + "'  value: " + strBuilder.toString());
			txtData = setFieldValueByName(txtData, outputField, fieldValue);
		}		

		return txtData;
	}
}
