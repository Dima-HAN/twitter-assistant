package com.twitterassistant.entity.model.user;

import java.util.NoSuchElementException;

/**
 * Permission enum 
 */
public enum Permission {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	ROLE_FREE("Freebie"),
	ROLE_PRO("Pro"),
	ROLE_SUPER("Super"),
	ROLE_DEVELOPER("Developer");

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	private String name;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	Permission(String name) {
		this.name = name;
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	public static Permission findByName(String name) {
		Permission returnVal = null;

		for (Permission perm : Permission.values()) {
			if (perm.getName().equals(name)) {
				returnVal = perm;
			}
		}

		if (returnVal == null) {
			throw new NoSuchElementException(Permission.class.getSimpleName() + "{name:\"" + name + "\"} not found");
		}

		return returnVal;
	}
	
	

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	public String getName() {
		return name;
	}

} // end of enum
