package com.twitterassistant.entity.model.twitter;

import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.DateEntity;
import com.twitterassistant.entity.model.social.SocialConnection;

/**
 * TwitterUser entity
 * 
 */
@MappedSuperclass
public abstract class TwitterUser extends DateEntity  {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(TwitterUser.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	private SocialConnection socialConnection;
	
	private String providerUserId;
	
	private String name;
	
	private String screenName;
	
	private String profileImageUrl;
	
	private int followersCount;
	
	private int friendsCount;	

	
	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public TwitterUser() {
	}
	
	public TwitterUser(String providerUserId, String name, String screenName, String profileImageUrl, int followersCount, int friendsCount) {
		setProviderUserId(providerUserId);
		setName(name);
		setScreenName(screenName);
		setProfileImageUrl(profileImageUrl);
		setFollowersCount(followersCount);
		setFriendsCount(friendsCount);
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public int compareTo(TwitterUser o) {
		return 0;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "socialConnectionId")
	public SocialConnection getSocialConnection() {
		return socialConnection;
	}

	public void setSocialConnection(SocialConnection socialConnection) {
		this.socialConnection = socialConnection;
	}
	
	@NotNull
	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	


}
