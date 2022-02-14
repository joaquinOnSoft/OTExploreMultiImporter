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

import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.importer.excel.ISolrFields;
import com.opentext.explore.importer.excel.pojo.TextData;

public abstract class AbstractFieldHandler implements IFieldHandler, ISolrFields {
	protected static final Logger log = LogManager.getLogger(AbstractFieldHandler.class);

	protected String getFieldValueByName(TextData txtData, String key) {

		switch (key) {
		case SOLR_FIELD_REFERENCE_ID:
			return txtData.getReferenceId();
		case SOLR_FIELD_INTERACTION_ID:
			return txtData.getInteractionId();
		case SOLR_FIELD_TITLE:
			return txtData.getTitle();
		case SOLR_FIELD_AUTHOR_NAME:
			return txtData.getAuthorName();
		case SOLR_FIELD_GROUP_HIERARCHY:
			return txtData.getGroupHierarchy();			
		case SOLR_FIELD_ID:
			return txtData.getId();
		case SOLR_FIELD_TYPE:
			return txtData.getType();
		case SOLR_FIELD_PUBLISHED_DATE:
			return txtData.getPublishedDateAsString();
		case SOLR_FIELD_DATE_TIME:
			return txtData.getDateTimeAsString();
		case SOLR_FIELD_CONTENT:
			return txtData.getContent();
		default:
			return txtData.getField(key);
		}
	}

	protected TextData setFieldValueByName(TextData txtData, String key, String value) {

		switch (key) {
		case SOLR_FIELD_REFERENCE_ID:
			txtData.setReferenceId(value);
			break;
		case SOLR_FIELD_INTERACTION_ID:
			txtData.setInteractionId(value);
			break;
		case SOLR_FIELD_TITLE:
			txtData.setTitle(value);
			break;
		case SOLR_FIELD_AUTHOR_NAME:
			txtData.setAuthorName(value);
			break;
		case SOLR_FIELD_GROUP_HIERARCHY:
			txtData.setGroupHierarchy(value);
			break;			
		case SOLR_FIELD_ID:
			txtData.setId(value);
			break;
		case SOLR_FIELD_TYPE:
			txtData.setType(value);
			break;
		case SOLR_FIELD_PUBLISHED_DATE:
			try {
				txtData.setPublishedDate(value);
			} catch (ParseException ex) {
				log.error("setPublishedDate(). Invalid UTC date format: ", ex);
			}
			break;
		case SOLR_FIELD_DATE_TIME:
			try {
				txtData.setDateTime(value);
			} catch (ParseException e) {
				log.error("setDateTime(). Invalid UTC date format: ", e);
			}
			break;
		case SOLR_FIELD_CONTENT:
			txtData.setContent(value);
			break;
		default:
			txtData.addField(key, value);
			break;
		}

		return txtData;
	}
}