package com.opentext.explore.importer.tripadvisor.pojo;

import java.sql.Date;

public class TAReview {
	private String author;
	private String location;
	
	private String title;
	private String content;
	private Date creationDate;
	
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
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("TAReview [author=").append(author)
			.append(", location=").append(location) 
			.append(", title=").append(title) 
			.append(", content=").append(content)
			.append(", creationDate=").append(creationDate).append("]");
		
		return sBuilder.toString();
	}
	
	
}
