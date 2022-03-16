package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class Address{
    @JsonProperty("@type") 
    public String type;
    @JsonProperty("@id") 
    public String id;
    public String streetAddress;
    public String addressLocality;
    public String addressCountry;
    public String postalCode;
}
