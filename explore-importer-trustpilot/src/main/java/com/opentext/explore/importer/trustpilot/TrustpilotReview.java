package com.opentext.explore.importer.trustpilot;

import java.text.ParseException;
import java.util.Date;

import com.opentext.explore.util.DateUtil;

public class TrustpilotReview {

	private String id;
	private String author;
	private Date datePublished;
	private String headline;
	private String reviewBody;
	private int rating;
	private String language;

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
	
	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(String datePublished) throws ParseException {
		//2022-04-20T19:50:47.000Z
		this.datePublished = DateUtil.strToDate(datePublished, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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

	public int getRating() {
		return rating;
	}

	public void setRating(String rating) throws NumberFormatException{
		this.rating = Integer.parseInt(rating);
	}	
	
	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}		
}
