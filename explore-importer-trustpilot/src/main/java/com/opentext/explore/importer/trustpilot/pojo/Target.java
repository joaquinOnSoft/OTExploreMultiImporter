package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class Target{
    @JsonProperty("@type") 
    public String type;
    public String urlTemplate;
}
