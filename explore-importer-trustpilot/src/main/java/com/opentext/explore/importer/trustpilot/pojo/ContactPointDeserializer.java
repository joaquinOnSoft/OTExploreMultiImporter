package com.opentext.explore.importer.trustpilot.pojo;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;


/***
 * 
 * 
 * @see Mapping a Dynamic JSON Object with Jackson
 * https://www.baeldung.com/jackson-mapping-dynamic-object
 * @see Getting Started with Custom Deserialization in Jackson
 * https://www.baeldung.com/jackson-deserialization
 */
public class ContactPointDeserializer extends JsonDeserializer<ContactPoint> {

	@Override
	public ContactPoint deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);

		ContactPoint cp = new ContactPoint();
		if(node.has("@type")){
			cp.setType(node.get("@type").asText());
		}
		
		if(node.has("@id")){
			cp.setId(node.get("@id").asText());
		}
		
		if(node.has("telephone")){
			cp.setTelephone(node.get("telephone").asText());
		}
		
		if(node.has("contactType")){
			cp.setContactType(node.get("contactType").asText());
		}
		
		if (node.get("areaServed").isArray()) {	
			List<String> areas = new LinkedList<String>();
			
			for(Iterator<JsonNode> ite = node.get("areaServed").iterator(); ite.hasNext(); ) {
				areas.add(ite.next().asText());
			}			
			
			cp.setAreaServed(areas);			
		}
		else {
			cp.setAreaServed(node.get("areaServed").asText());
		}
		
		return cp;
	}
}
