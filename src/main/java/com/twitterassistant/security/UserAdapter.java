package com.twitterassistant.security;

import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

/**
 * User Adapter
 * 
 */

public class UserAdapter extends org.springframework.security.core.userdetails.User {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserAdapter.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	private UUID uuid;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	public UserAdapter(String uuid, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		setUuid(UUID.fromString(uuid));
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
