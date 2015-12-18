package com.twitterassistant.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.model.user.User;
/*
 * Example: using in entity 
 * 
 * @JsonDeserialize(using = UserSerializer.class)
	 public class User extends DateEntity {
 */


public class UserSerializer extends JsonSerializer<User> {
	
	//Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserSerializer.class);
	
	//Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	//Constructors ---------------------------------------------------------------------------------------- Constructors
	
	//Public Methods ------------------------------------------------------------------------------------ Public Methods
  public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
  	if (user != null) {
    	 jsonGenerator.writeStartObject();
       jsonGenerator.writeStringField("firstName", "Test");
       jsonGenerator.writeEndObject();
     }
  }
  
  //Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
