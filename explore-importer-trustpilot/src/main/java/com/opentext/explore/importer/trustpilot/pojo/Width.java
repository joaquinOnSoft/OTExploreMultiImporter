package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class Width{
    @JsonProperty("@type") 
    public String type;
    public int value;
    public String unitCode;
    public String unitText;
}