package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class AggregateRating{
    @JsonProperty("@type") 
    public String type;
    public String bestRating;
    public String worstRating;
    public String ratingValue;
    public String reviewCount;
}
