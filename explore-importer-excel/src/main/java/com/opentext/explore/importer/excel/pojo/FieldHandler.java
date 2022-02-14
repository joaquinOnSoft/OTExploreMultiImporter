/*

 *   (C) Copyright 2021 OpenText and others.
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
 *     Joaquín Garzón - initial implementation
 *
 */
package com.opentext.explore.importer.excel.pojo;

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
@JsonPropertyOrder({ "inputSolrNames", "outputSolrNames", "javaClass" })
public class FieldHandler {

	@JsonProperty("inputSolrNames")
	private List<String> inputSolrNames = null;
	@JsonProperty("outputSolrNames")
	private List<String> outputSolrNames = null;
	@JsonProperty("javaClass")
	private String javaClass;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("inputSolrNames")
	public List<String> getInputSolrNames() {
		return inputSolrNames;
	}

	@JsonProperty("inputSolrNames")
	public void setInputSolrNames(List<String> inputSolrNames) {
		this.inputSolrNames = inputSolrNames;
	}

	@JsonProperty("outputSolrNames")
	public List<String> getOutputSolrNames() {
		return outputSolrNames;
	}

	@JsonProperty("outputSolrNames")
	public void setOutputSolrNames(List<String> outputSolrNames) {
		this.outputSolrNames = outputSolrNames;
	}

	@JsonProperty("javaClass")
	public String getJavaClass() {
		return javaClass;
	}

	@JsonProperty("javaClass")
	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
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
