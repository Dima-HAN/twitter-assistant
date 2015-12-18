package com.twitterassistant.entity.model.twitter;

/**
 * TwitterUserType enum 
 */
public enum TwitterUserType {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	FOLLOWER("Follower"),
	FRIEND("Friend");

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	private String name;

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	
	TwitterUserType(String name) {
		this.name = name;
	}
	

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	public String getName() {
		return name;
	}

} // end of enum
