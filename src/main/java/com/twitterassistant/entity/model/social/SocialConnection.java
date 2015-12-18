package com.twitterassistant.entity.model.social;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.DateEntity;
import com.twitterassistant.entity.model.twitter.Follower;
import com.twitterassistant.entity.model.twitter.Friend;
import com.twitterassistant.entity.model.user.User;

/**
 * SocialConnection entity
 * 
 */
//@formatter:off
@Entity
//@Indexed(index="social_connection")
@NamedNativeQueries(value = {
	@NamedNativeQuery(name = "social.getConnectionUserById", query = "{$query: { provider : ':provider', providerUserId: ':providerUserId'} }", resultSetMapping = "social.setConnectionUserById")
})

@SqlResultSetMappings(value = {
	@SqlResultSetMapping(name="social.setConnectionUserById", entities = @EntityResult(entityClass = SocialConnection.class))
})
//@formatter:on
@Table(name = "social_connection")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class SocialConnection extends DateEntity  {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SocialConnection.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	private SocialProvider provider;

	private String displayName;

	private String providerUserId;
	
	private String pageId;

	private String profileUrl;
	
	private String accessToken;

	private String pageAccessToken;

	private String secret;

	private String refreshToken;
	
	@JsonIgnore
	private User user;
	
	@JsonIgnore
	private Set<Follower> followers;
	
	@JsonIgnore
	private Set<Friend> friends;

	@Transient
	private String firstName;

	@Transient
	private String lastName;

	@Transient
	private String email;

	@Transient
	private String photoUrl;

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public SocialConnection() {
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public int compareTo(SocialConnection o) {
		return 0;
	}
	
	@PreRemove
	public void onPreRemove() {
		LOG.info("onPreRemove: removing followers and friends of the social connection: " + getId());
		
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	
	@Field(analyze = Analyze.NO)
	@Enumerated(EnumType.STRING)
	@NotNull
	public SocialProvider getProvider() {
		return provider;
	}

	public void setProvider(SocialProvider provider) {
		this.provider = provider;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@NotNull
	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}
	
	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}	
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getPageAccessToken() {
		return pageAccessToken;
	}

	public void setPageAccessToken(String pageAccessToken) {
		this.pageAccessToken = pageAccessToken;
	}
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Transient
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Transient
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<Follower> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<Follower> followers) {
		this.followers = followers;
	}
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<Friend> getFriends() {
		return friends;
	}

	public void setFriends(Set<Friend> friends) {
		this.friends = friends;
	}

}
