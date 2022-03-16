package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class ReviewRating{
    @JsonProperty("@type") 
    public String type;
    public String bestRating;
    public String worstRating;
    public String ratingValue;
    
    /* Getter and setters created to be use in mock objects */
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getBestRating() {
		return bestRating;
	}
	
	public void setBestRating(String bestRating) {
		this.bestRating = bestRating;
	}
	
	public String getWorstRating() {
		return worstRating;
	}
	
	public void setWorstRating(String worstRating) {
		this.worstRating = worstRating;
	}
	
	public String getRatingValue() {
		return ratingValue;
	}
	
	public void setRatingValue(String ratingValue) {
		this.ratingValue = ratingValue;
	}    
}
