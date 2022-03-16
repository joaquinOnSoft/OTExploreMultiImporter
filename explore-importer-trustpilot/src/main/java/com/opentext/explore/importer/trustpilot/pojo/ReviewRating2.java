package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class ReviewRating2{
    @JsonProperty("@type ") 
    public String type;
    public String bestRating;
    public String worstRating;
    public String ratingValue;
}
