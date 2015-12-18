package com.twitterassistant.entity.model.social;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * SocialProvider enum 
 */
public enum SocialProvider implements Serializable  {

	// Constants ---------------------------------------------------------------------------------------------- Constants
	TWITTER("Twitter"),
	FACEBOOK("Facebook"),	
	LINKEDIN("LinkedIn");

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	private String name;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	SocialProvider(String name) {
		this.name = name;
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	public static String findByValue(SocialProvider name) {				
		return name.getName();
	}
	
	public static SocialProvider findByName(String name) {
		SocialProvider returnVal = null;

		for (SocialProvider perm : SocialProvider.values()) {
			if (perm.getName().equalsIgnoreCase(name)) {
				returnVal = perm;
			}
		}

		if (returnVal == null) {
			throw new NoSuchElementException(SocialProvider.class.getSimpleName() + "{name:\"" + name + "\"} not found");
		}

		return returnVal;
	}
	
	

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		System.out.println("SET NAME: "  + name);
	}

} // end of enum
