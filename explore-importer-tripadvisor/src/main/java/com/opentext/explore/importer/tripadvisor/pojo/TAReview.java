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
package com.opentext.explore.importer.tripadvisor.pojo;

import java.util.Date;

public class TAReview {
	private static final String LANG_ENGLISH = "en";
	
	private String id;
	private String author;
	private String location;
	
	private String title;
	private String content;
	private int rating;
	private Date creationDate;
	
	private String language;
	
	private TAFacility facility;
	
	public TAReview() {
		this.language = LANG_ENGLISH;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
		
	public int getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = Integer.parseInt(rating);
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public TAFacility getFacility() {
		return facility;
	}

	public void setFacility(TAFacility facility) {
		this.facility = facility;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("TAReview [author=").append(author)
			.append(", location=").append(location) 
			.append(", title=").append(title) 
			.append(", content=").append(content)
			.append(", rating=").append(rating)
			.append(", creationDate=").append(creationDate)
			.append(", language=").append(language)
			.append(", facility=").append(facility).append("]");
		
		return sBuilder.toString();
	}
	
	
}
