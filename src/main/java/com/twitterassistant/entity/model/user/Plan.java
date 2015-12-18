package com.twitterassistant.entity.model.user;

/**
 * Plan enum 
 */
public enum Plan {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	FREE("Freebie"),	
	PRO("Pro");

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	private String name;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	Plan(String name) {
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
