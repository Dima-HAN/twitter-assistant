package com.twitterassistant.entity.model.user;

/**
 * UserStatus enum 
 */
public enum UserStatus {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	DISABLED("Disabled"),
	ENABLED("Enabled"),
	SUSPENDED("Suspended");

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	private String name;

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	
	UserStatus(String name) {
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
