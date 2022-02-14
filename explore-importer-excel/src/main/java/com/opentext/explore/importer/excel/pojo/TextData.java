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
package com.opentext.explore.importer.excel.pojo;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.opentext.explore.util.DateUtil;

/**
 * Text data container (represents a row in an Excel or CSV file)
 * 
 * @author Joaquín Garzón Peña
 */
public class TextData {
	public String referenceId;
	public String interactionId;
	public String title;
	public String authorName;
	public String groupHierarchy;
	public String id;
	public String type;
	public Date publishedDate;
	public Date dateTime;
	public String content;
	public Map<String, String> fields;

	public TextData() {
		fields = new HashMap<String, String>();
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getInteractionId() {
		return interactionId;
	}

	public void setInteractionId(String interactionId) {
		this.interactionId = interactionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getGroupHierarchy() {
		return groupHierarchy;
	}

	public void setGroupHierarchy(String groupHierarchy) {
		this.groupHierarchy = groupHierarchy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public String getPublishedDateAsString() {
		return DateUtil.dateToUTC(publishedDate);
	}

	/**
	 * Set the publication date
	 * 
	 * @param publishedDate - Publication date in UTC format
	 * @throws ParseException - If the date is not in UTC format
	 */
	public void setPublishedDate(String publishedDate) throws ParseException {
		this.publishedDate = DateUtil.utcToDate(publishedDate);
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public String getDateTimeAsString() {
		return DateUtil.dateToUTC(dateTime);
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Set the publication date
	 * 
	 * @param publishedDate - Publication date in UTC format
	 * @throws ParseException - If the date is not in UTC format
	 */
	public void setDateTime(String dateTime) throws ParseException {
		this.dateTime = DateUtil.utcToDate(dateTime);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void addField(String key, String value) {
		fields.put(key, value);
	}

	public String getField(String key) {
		return fields.get(key);
	}
}
