package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class ContactPoint{
    @JsonProperty("@type") 
    public String type;
    @JsonProperty("@id") 
    public String id;
    public String telephone;
    public String contactType;
    public Object areaServed;
}
