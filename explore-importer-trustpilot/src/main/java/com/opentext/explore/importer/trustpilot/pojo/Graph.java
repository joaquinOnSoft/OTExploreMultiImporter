package com.opentext.explore.importer.trustpilot.pojo; 
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opentext.explore.util.Hash; 
public class Graph{
    @JsonProperty("@type") 
    public String type;
    @JsonProperty("@id") 
    public String id;
    public String name;
    public String legalName;
    public String url;
    public String description;
    public Object sameAs;
    public Logo logo;
    public Founder founder;
    public String foundingDate;
    public String email;
    public Address address;
    public ArrayList<ContactPoint> contactPoint;
    public String contentUrl;
    public Width width;
    public Height height;
    public String caption;
    public Publisher publisher;
    public CopyrightHolder copyrightHolder;
    public ArrayList<PotentialAction> potentialAction;
    public String inLanguage;
    public IsPartOf isPartOf;
    public Breadcrumb breadcrumb;
    public About about;
    public PrimaryImageOfPage primaryImageOfPage;
    public HasPart hasPart;
    public ArrayList<ItemListElement> itemListElement;
    public String telephone;
    public Image image;
    public AggregateRating aggregateRating;
    public ArrayList<Review> review;
    public ItemReviewed itemReviewed;
    public Author author;
    public Date datePublished;
    public String headline;
    public String reviewBody;
    public ReviewRating reviewRating;
    
	public long generateId() {
		StringBuffer id = new StringBuffer();
		id.append(author.name);
		id.append("-");
		id.append(headline);
		id.append("-");
		id.append(datePublished);
		
		return Hash.hash(id.toString());		
	}

	/* Getter and setters created to be use in mock objects */
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getSameAs() {
		return sameAs;
	}

	public void setSameAs(Object sameAs) {
		this.sameAs = sameAs;
	}

	public Logo getLogo() {
		return logo;
	}

	public void setLogo(Logo logo) {
		this.logo = logo;
	}

	public Founder getFounder() {
		return founder;
	}

	public void setFounder(Founder founder) {
		this.founder = founder;
	}

	public String getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(String foundingDate) {
		this.foundingDate = foundingDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ArrayList<ContactPoint> getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(ArrayList<ContactPoint> contactPoint) {
		this.contactPoint = contactPoint;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public Width getWidth() {
		return width;
	}

	public void setWidth(Width width) {
		this.width = width;
	}

	public Height getHeight() {
		return height;
	}

	public void setHeight(Height height) {
		this.height = height;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public CopyrightHolder getCopyrightHolder() {
		return copyrightHolder;
	}

	public void setCopyrightHolder(CopyrightHolder copyrightHolder) {
		this.copyrightHolder = copyrightHolder;
	}

	public ArrayList<PotentialAction> getPotentialAction() {
		return potentialAction;
	}

	public void setPotentialAction(ArrayList<PotentialAction> potentialAction) {
		this.potentialAction = potentialAction;
	}

	public String getInLanguage() {
		return inLanguage;
	}

	public void setInLanguage(String inLanguage) {
		this.inLanguage = inLanguage;
	}

	public IsPartOf getIsPartOf() {
		return isPartOf;
	}

	public void setIsPartOf(IsPartOf isPartOf) {
		this.isPartOf = isPartOf;
	}

	public Breadcrumb getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(Breadcrumb breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public About getAbout() {
		return about;
	}

	public void setAbout(About about) {
		this.about = about;
	}

	public PrimaryImageOfPage getPrimaryImageOfPage() {
		return primaryImageOfPage;
	}

	public void setPrimaryImageOfPage(PrimaryImageOfPage primaryImageOfPage) {
		this.primaryImageOfPage = primaryImageOfPage;
	}

	public HasPart getHasPart() {
		return hasPart;
	}

	public void setHasPart(HasPart hasPart) {
		this.hasPart = hasPart;
	}

	public ArrayList<ItemListElement> getItemListElement() {
		return itemListElement;
	}

	public void setItemListElement(ArrayList<ItemListElement> itemListElement) {
		this.itemListElement = itemListElement;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public AggregateRating getAggregateRating() {
		return aggregateRating;
	}

	public void setAggregateRating(AggregateRating aggregateRating) {
		this.aggregateRating = aggregateRating;
	}

	public ArrayList<Review> getReview() {
		return review;
	}

	public void setReview(ArrayList<Review> review) {
		this.review = review;
	}

	public ItemReviewed getItemReviewed() {
		return itemReviewed;
	}

	public void setItemReviewed(ItemReviewed itemReviewed) {
		this.itemReviewed = itemReviewed;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getReviewBody() {
		return reviewBody;
	}

	public void setReviewBody(String reviewBody) {
		this.reviewBody = reviewBody;
	}

	public ReviewRating getReviewRating() {
		return reviewRating;
	}

	public void setReviewRating(ReviewRating reviewRating) {
		this.reviewRating = reviewRating;
	}    	
}
