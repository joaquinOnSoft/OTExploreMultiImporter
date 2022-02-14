/**
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
package com.opentext.explore.importer.excel;

public interface ISolrFields {
	public static final String SOLR_FIELD_REFERENCE_ID = "reference_id";
	public static final String SOLR_FIELD_INTERACTION_ID = "interaction_id";
	public static final String SOLR_FIELD_TITLE = "title";
	public static final String SOLR_FIELD_AUTHOR_NAME = "author_name";
	public static final String SOLR_FIELD_GROUP_HIERARCHY = "Group_Hierarchy";
	public static final String SOLR_FIELD_ID = "ID";
	public static final String SOLR_FIELD_TYPE = "type";
	public static final String SOLR_FIELD_PUBLISHED_DATE = "published_date";
	public static final String SOLR_FIELD_DATE_TIME = "date_time";
	public static final String SOLR_FIELD_CONTENT = "content";
}
