package com.twitterassistant.entity.model.twitter;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.model.social.SocialConnection;

/**
 * Follower entity
 * 
 */
//@formatter:off
@Entity
@NamedNativeQueries(value = {
	@NamedNativeQuery(name = "twitter.getFollowers", query = "{$query: { socialConnectionId : ':socialConnectionId'} }", resultSetMapping = "twitter.setFollowers")
})

@SqlResultSetMappings(value = {
	@SqlResultSetMapping(name="twitter.setFollowers", entities = @EntityResult(entityClass = Follower.class))
})
//@formatter:on
@Table(name = "twitter_follower")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Follower extends TwitterUser  {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(Follower.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	
	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public Follower() {
		
	}
	
	public Follower(String providerUserId, String name, String screenName, String profileImageUrl, int followersCount, int friendsCount) {
		super(providerUserId, name, screenName, profileImageUrl, followersCount, friendsCount);
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public int compareTo(Follower o) {
		return 0;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	


}
