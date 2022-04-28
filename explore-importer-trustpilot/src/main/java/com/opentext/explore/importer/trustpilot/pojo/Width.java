
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
    "value",
    "unitCode",
    "unitText"
})
@Generated("jsonschema2pojo")
public class Width {

    @JsonProperty("@type")
    private String type;
    @JsonProperty("value")
    private Integer value;
    @JsonProperty("unitCode")
    private String unitCode;
    @JsonProperty("unitText")
    private String unitText;
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

    @JsonProperty("value")
    public Integer getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Integer value) {
        this.value = value;
    }

    @JsonProperty("unitCode")
    public String getUnitCode() {
        return unitCode;
    }

    @JsonProperty("unitCode")
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    @JsonProperty("unitText")
    public String getUnitText() {
        return unitText;
    }

    @JsonProperty("unitText")
    public void setUnitText(String unitText) {
        this.unitText = unitText;
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
