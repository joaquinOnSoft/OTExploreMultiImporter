
package com.opentext.explore.importer.trustpilot.pojo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    "@id",
    "telephone",
    "contactType",
    "areaServed"
})
@Generated("jsonschema2pojo")
public class ContactPoint {

    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("telephone")
    private String telephone;
    @JsonProperty("contactType")
    private String contactType;
    @JsonProperty("areaServed")
    private List<String> areaServed;
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

    @JsonProperty("@id")
    public String getId() {
        return id;
    }

    @JsonProperty("@id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("telephone")
    public String getTelephone() {
        return telephone;
    }

    @JsonProperty("telephone")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @JsonProperty("contactType")
    public String getContactType() {
        return contactType;
    }

    @JsonProperty("contactType")
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    @JsonProperty("areaServed")
    public List<String> getAreaServed() {
        return areaServed;
    }

    @JsonProperty("areaServed")
    public void setAreaServed(String areaServed) {
        this.areaServed = new LinkedList<String>();
        this.areaServed.add(areaServed);
    }    
    
    @JsonProperty("areaServed")
    public void setAreaServed(List<String> areaServed) {
        this.areaServed = areaServed;
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
