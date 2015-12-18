package com.twitterassistant.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Example: using in entity 
 * 
 * @JsonSerialize(using = StringSerializer.class)
	 private String firstName;	
 */

public class StringSerializer extends JsonSerializer<Object> {
	
	//Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(StringSerializer.class);
	
	//Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	//Constructors ---------------------------------------------------------------------------------------- Constructors
	
	//Public Methods ------------------------------------------------------------------------------------ Public Methods
	public void serialize(Object value, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException,
			JsonProcessingException {
		if (value != null) {
			jsonGenerator.writeString(value.toString());
		}
	}
	
	//Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
