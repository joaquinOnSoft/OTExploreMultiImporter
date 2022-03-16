package com.opentext.explore.importer.trustpilot.pojo; 
import com.fasterxml.jackson.annotation.JsonProperty; 
public class ItemListElement{
    @JsonProperty("@type") 
    public String type;
    public int position;
    public String name;
    public Object item;
    public String sameAs;
}
