package com.opentext.explore.importer.trustpilot.pojo;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/***
 * A <strong>itemListElement</strong> node can have two
 * different formats with different properties.
 * 
 * Here you can see an example of both formats:
 * 
 * <strong>Format 1 (with additional fields)</strong>
 * <pre>
 * {
 *    ...
 *    
 *    "itemListElement": [{
 *    		"@type": "ListItem",
 *    		"position": 1,
 *    		"name": "Home",
 *    		"item": "https://fr.trustpilot.com/",
 *    		"sameAs": "https://fr.trustpilot.com/"
 *    	}, 
 *    ...
 * }
 * </pre>
 *
 * <strong>Format 2 (with limited number of fields)</strong>
 * <br/>
 * <strong>NOTE:</strong> the <strong>item</strong> field is an Object, not a String 
 * like in the previous format example
 * <pre>
 * {
 *    ...
 *    
 *    "itemListElement": [{
 *    		"@type": "ListItem",
 *    		"item": {
 *    			"@id": "https://www.trustpilot.com/#/schema/Review/www.coriolis.com/6264418c0c4813f76bf2b657"
 *    	}, 
 *    ...
 * }
 * </pre>
 * 
 * @see Mapping a Dynamic JSON Object with Jackson
 * https://www.baeldung.com/jackson-mapping-dynamic-object
 * @see Getting Started with Custom Deserialization in Jackson
 * https://www.baeldung.com/jackson-deserialization
 */
public class ItemListElementDeserializer extends JsonDeserializer<ItemListElement>{

	@Override
	public ItemListElement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);

		ItemListElement itemLE = new ItemListElement();

		if(node.has("@type")){
			itemLE.setType(node.get("@type").asText());
		}

		if(node.has("position")){
			itemLE.setPosition(node.get("position").asInt());
		}
		
		if(node.has("name")){
			itemLE.setType(node.get("name").asText());
		}
				
		if(node.has("item")){
			Item item = new Item();
			if(node.get("item").isTextual()) {
				item.setId(node.get("item").asText());
			}
			else if(node.get("item").isObject()){
				item.setId(node.get("item").get("@id").asText());	
			}
			
			itemLE.setItem(item);
		}
		
		if(node.has("sameAs")){
			itemLE.setType(node.get("sameAs").asText());
		}		
		
		
		
		return itemLE;
	}

}
