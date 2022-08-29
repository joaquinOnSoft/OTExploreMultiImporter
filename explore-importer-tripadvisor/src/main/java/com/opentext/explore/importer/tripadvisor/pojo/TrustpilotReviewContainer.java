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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Classes generated using 
 * <a href="https://www.jsonschema2pojo.org/">jsonschema2pojo </a> 
 * which generates Plain Old Java Objects from JSON or JSON-Schema.
 * @author Joaquín Garzón
 * @since  22.02.18
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@context",
    "@type",
    "@id",
    "url",
    "aggregateRating",
    "review",
    "name",
    "description",
    "publisher",
    "mainEntity",
    "itemListElement"
})
public class TrustpilotReviewContainer {

    @JsonProperty("@context")
    private String context;
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("aggregateRating")
    private AggregateRating aggregateRating;
    @JsonProperty("review")
    private List<Review> review = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("publisher")
    private Publisher__1 publisher;
    @JsonProperty("mainEntity")
    private MainEntity mainEntity;
    @JsonProperty("itemListElement")
    private List<ItemListElement> itemListElement = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("@context")
    public String getContext() {
        return context;
    }

    @JsonProperty("@context")
    public void setContext(String context) {
        this.context = context;
    }

    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    @JsonProperty("@type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("@id")
    public String getId() {
        return id;
    }

    @JsonProperty("@id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("aggregateRating")
    public AggregateRating getAggregateRating() {
        return aggregateRating;
    }

    @JsonProperty("aggregateRating")
    public void setAggregateRating(AggregateRating aggregateRating) {
        this.aggregateRating = aggregateRating;
    }

    @JsonProperty("review")
    public List<Review> getReview() {
        return review;
    }

    @JsonProperty("review")
    public void setReview(List<Review> review) {
        this.review = review;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("publisher")
    public Publisher__1 getPublisher() {
        return publisher;
    }

    @JsonProperty("publisher")
    public void setPublisher(Publisher__1 publisher) {
        this.publisher = publisher;
    }

    @JsonProperty("mainEntity")
    public MainEntity getMainEntity() {
        return mainEntity;
    }

    @JsonProperty("mainEntity")
    public void setMainEntity(MainEntity mainEntity) {
        this.mainEntity = mainEntity;
    }

    @JsonProperty("itemListElement")
    public List<ItemListElement> getItemListElement() {
        return itemListElement;
    }

    @JsonProperty("itemListElement")
    public void setItemListElement(List<ItemListElement> itemListElement) {
        this.itemListElement = itemListElement;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
