package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class PotentialAction{
    @JsonProperty("@type") 
    public String type;
    public Target target;
    @JsonProperty("query-input") 
    public String queryInput;
}
