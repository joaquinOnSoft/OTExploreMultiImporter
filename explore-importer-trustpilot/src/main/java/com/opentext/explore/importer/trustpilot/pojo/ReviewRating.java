
package com.opentext.explore.importer.trustpilot.pojo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@type",
    "bestRating",
    "worstRating",
    "ratingValue"
})
@Generated("jsonschema2pojo")
public class ReviewRating {

    @JsonProperty("@type")
    private String type;
    @JsonProperty("bestRating")
    private String bestRating;
    @JsonProperty("worstRating")
    private String worstRating;
    @JsonProperty("ratingValue")
    private String ratingValue;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    @JsonProperty("@type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("bestRating")
    public String getBestRating() {
        return bestRating;
    }

    @JsonProperty("bestRating")
    public void setBestRating(String bestRating) {
        this.bestRating = bestRating;
    }

    @JsonProperty("worstRating")
    public String getWorstRating() {
        return worstRating;
    }

    @JsonProperty("worstRating")
    public void setWorstRating(String worstRating) {
        this.worstRating = worstRating;
    }

    @JsonProperty("ratingValue")
    public String getRatingValue() {
        return ratingValue;
    }

    @JsonProperty("ratingValue")
    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
