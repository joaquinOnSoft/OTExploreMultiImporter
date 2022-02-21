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
package com.opentext.explore.importer.trushpilot.pojo;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.opentext.explore.util.Hash;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@type",
    "itemReviewed",
    "author",
    "datePublished",
    "headline",
    "reviewBody",
    "reviewRating",
    "publisher",
    "inLanguage"
})
public class Review {
	
    @JsonProperty("@type")
    private String type;
    @JsonProperty("itemReviewed")
    private ItemReviewed itemReviewed;
    @JsonProperty("author")
    private Author author;
    @JsonProperty("datePublished")
    private String datePublished;
    @JsonProperty("headline")
    private String headline;
    @JsonProperty("reviewBody")
    private String reviewBody;
    @JsonProperty("reviewRating")
    private ReviewRating reviewRating;
    @JsonProperty("publisher")
    private Publisher publisher;
    @JsonProperty("inLanguage")
    private String inLanguage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    @JsonProperty("@type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("itemReviewed")
    public ItemReviewed getItemReviewed() {
        return itemReviewed;
    }

    @JsonProperty("itemReviewed")
    public void setItemReviewed(ItemReviewed itemReviewed) {
        this.itemReviewed = itemReviewed;
    }

    @JsonProperty("author")
    public Author getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(Author author) {
        this.author = author;
    }

    @JsonProperty("datePublished")
    public String getDatePublished() {
        return datePublished;
    }

    @JsonProperty("datePublished")
    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    @JsonProperty("headline")
    public String getHeadline() {
        return headline;
    }

    @JsonProperty("headline")
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @JsonProperty("reviewBody")
    public String getReviewBody() {
        return reviewBody;
    }

    @JsonProperty("reviewBody")
    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    @JsonProperty("reviewRating")
    public ReviewRating getReviewRating() {
        return reviewRating;
    }

    @JsonProperty("reviewRating")
    public void setReviewRating(ReviewRating reviewRating) {
        this.reviewRating = reviewRating;
    }

    @JsonProperty("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    @JsonProperty("publisher")
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @JsonProperty("inLanguage")
    public String getInLanguage() {
        return inLanguage;
    }

    @JsonProperty("inLanguage")
    public void setInLanguage(String inLanguage) {
        this.inLanguage = inLanguage;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    
	public long generateId() {
		StringBuffer id = new StringBuffer();
		id.append(author.getName());
		id.append("-");
		id.append(headline);
		id.append("-");
		id.append(datePublished);
		
		return Hash.hash(id.toString());		
	}
}
