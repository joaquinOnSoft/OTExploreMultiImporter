package com.opentext.explore.importer.trustpilot.pojo; 
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty; 
public class TrustpilotReviewContainer{
    @JsonProperty("@context") 
    public String context;
    @JsonProperty("@graph") 
    public ArrayList<Graph> graph;
}
