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

package com.opentext.explore.importer.trustpilot.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@type",
    "@id",
    "name",
    "legalName",
    "url",
    "description",
    //"sameAs",
    "logo",
    "email",
    "address",
    "contentUrl",
    "width",
    "height",
    "caption",
    "publisher",
    "copyrightHolder",
    "potentialAction",
    "inLanguage",
    "isPartOf",
    "breadcrumb",
    "about",
    "mainEntity",
    "primaryImageOfPage",
    "hasPart",
    "itemListElement",
    "telephone",
    "image",
    "aggregateRating",
    "review",
    "itemReviewed",
    "author",
    "datePublished",
    "headline",
    "reviewBody",
    "reviewRating"
})
public class Graph {

    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("legalName")
    private String legalName;
    @JsonProperty("url")
    private String url;
    @JsonProperty("description")
    private String description;
    //@JsonProperty("sameAs")
    @JsonIgnore
    private Object sameAs;
    @JsonProperty("logo")
    private Logo logo;
    @JsonProperty("email")
    private String email;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("contentUrl")
    private String contentUrl;
    @JsonProperty("width")
    private Width width;
    @JsonProperty("height")
    private Height height;
    @JsonProperty("caption")
    private String caption;
    @JsonProperty("publisher")
    private Publisher publisher;
    @JsonProperty("copyrightHolder")
    private CopyrightHolder copyrightHolder;
    @JsonProperty("potentialAction")
    private List<PotentialAction> potentialAction = null;
    @JsonProperty("inLanguage")
    private String inLanguage;
    @JsonProperty("isPartOf")
    private IsPartOf isPartOf;
    @JsonProperty("breadcrumb")
    private Breadcrumb breadcrumb;
    @JsonProperty("about")
    private About about;
    @JsonProperty("mainEntity")
    private MainEntity mainEntity;
    @JsonProperty("primaryImageOfPage")
    private PrimaryImageOfPage primaryImageOfPage;
    @JsonProperty("hasPart")
    private HasPart hasPart;
    @JsonProperty("itemListElement")
    private List<ItemListElement> itemListElement = null;
    @JsonProperty("telephone")
    private String telephone;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("aggregateRating")
    private AggregateRating aggregateRating;
    @JsonProperty("review")
    private List<Review> review = null;
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

    @JsonProperty("@id")
    public String getId() {
        return id;
    }

    @JsonProperty("@id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("legalName")
    public String getLegalName() {
        return legalName;
    }

    @JsonProperty("legalName")
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    //@JsonProperty("sameAs")
    public Object getSameAs() {
        return sameAs;
    }

    //@JsonProperty("sameAs")
    public void setSameAs(Object sameAs) {
        this.sameAs = sameAs;
    }

    @JsonProperty("logo")
    public Logo getLogo() {
        return logo;
    }

    @JsonProperty("logo")
    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonProperty("contentUrl")
    public String getContentUrl() {
        return contentUrl;
    }

    @JsonProperty("contentUrl")
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @JsonProperty("width")
    public Width getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Width width) {
        this.width = width;
    }

    @JsonProperty("height")
    public Height getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Height height) {
        this.height = height;
    }

    @JsonProperty("caption")
    public String getCaption() {
        return caption;
    }

    @JsonProperty("caption")
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @JsonProperty("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    @JsonProperty("publisher")
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @JsonProperty("copyrightHolder")
    public CopyrightHolder getCopyrightHolder() {
        return copyrightHolder;
    }

    @JsonProperty("copyrightHolder")
    public void setCopyrightHolder(CopyrightHolder copyrightHolder) {
        this.copyrightHolder = copyrightHolder;
    }

    @JsonProperty("potentialAction")
    public List<PotentialAction> getPotentialAction() {
        return potentialAction;
    }

    @JsonProperty("potentialAction")
    public void setPotentialAction(List<PotentialAction> potentialAction) {
        this.potentialAction = potentialAction;
    }

    @JsonProperty("inLanguage")
    public String getInLanguage() {
        return inLanguage;
    }

    @JsonProperty("inLanguage")
    public void setInLanguage(String inLanguage) {
        this.inLanguage = inLanguage;
    }

    @JsonProperty("isPartOf")
    public IsPartOf getIsPartOf() {
        return isPartOf;
    }

    @JsonProperty("isPartOf")
    public void setIsPartOf(IsPartOf isPartOf) {
        this.isPartOf = isPartOf;
    }

    @JsonProperty("breadcrumb")
    public Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    @JsonProperty("breadcrumb")
    public void setBreadcrumb(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    @JsonProperty("about")
    public About getAbout() {
        return about;
    }

    @JsonProperty("about")
    public void setAbout(About about) {
        this.about = about;
    }

    @JsonProperty("mainEntity")
    public MainEntity getMainEntity() {
        return mainEntity;
    }

    @JsonProperty("mainEntity")
    public void setMainEntity(MainEntity mainEntity) {
        this.mainEntity = mainEntity;
    }

    @JsonProperty("primaryImageOfPage")
    public PrimaryImageOfPage getPrimaryImageOfPage() {
        return primaryImageOfPage;
    }

    @JsonProperty("primaryImageOfPage")
    public void setPrimaryImageOfPage(PrimaryImageOfPage primaryImageOfPage) {
        this.primaryImageOfPage = primaryImageOfPage;
    }

    @JsonProperty("hasPart")
    public HasPart getHasPart() {
        return hasPart;
    }

    @JsonProperty("hasPart")
    public void setHasPart(HasPart hasPart) {
        this.hasPart = hasPart;
    }

    @JsonProperty("itemListElement")
    public List<ItemListElement> getItemListElement() {
        return itemListElement;
    }

    @JsonProperty("itemListElement")
    public void setItemListElement(List<ItemListElement> itemListElement) {
        this.itemListElement = itemListElement;
    }

    @JsonProperty("telephone")
    public String getTelephone() {
        return telephone;
    }

    @JsonProperty("telephone")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @JsonProperty("image")
    public Image getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Image image) {
        this.image = image;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
