/*
 *   (C) Copyright 2022 OpenText and others.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Contributors:
 *     Joaqu�n Garz�n - initial implementation
 *
 */
package com.opentext.explore.importer.trushpilot.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "csvw:name",
    "csvw:datatype",
    "csvw:cells"
})
public class CsvwColumn {

    @JsonProperty("csvw:name")
    private String csvwName;
    @JsonProperty("csvw:datatype")
    private String csvwDatatype;
    @JsonProperty("csvw:cells")
    private List<CsvwCell> csvwCells = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("csvw:name")
    public String getCsvwName() {
        return csvwName;
    }

    @JsonProperty("csvw:name")
    public void setCsvwName(String csvwName) {
        this.csvwName = csvwName;
    }

    @JsonProperty("csvw:datatype")
    public String getCsvwDatatype() {
        return csvwDatatype;
    }

    @JsonProperty("csvw:datatype")
    public void setCsvwDatatype(String csvwDatatype) {
        this.csvwDatatype = csvwDatatype;
    }

    @JsonProperty("csvw:cells")
    public List<CsvwCell> getCsvwCells() {
        return csvwCells;
    }

    @JsonProperty("csvw:cells")
    public void setCsvwCells(List<CsvwCell> csvwCells) {
        this.csvwCells = csvwCells;
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
