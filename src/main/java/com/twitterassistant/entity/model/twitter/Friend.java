package com.twitterassistant.entity.model.twitter;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Friend entity
 * 
 */
//@formatter:off
@Entity
//@formatter:on
@Table(name = "twitter_friend")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Friend extends TwitterUser {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(Friend.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public Friend() {

	}
	
	public Friend(String providerUserId, String name, String screenName, String profileImageUrl, int followersCount, int friendsCount) {
		super(providerUserId, name, screenName, profileImageUrl, followersCount, friendsCount);
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public int compareTo(Friend o) {
		return 0;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
