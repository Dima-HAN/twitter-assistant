package com.twitterassistant.entity.model.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.hibernate.search.annotations.Indexed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.DateEntity;
import com.twitterassistant.entity.model.social.SocialConnection;


/**
 * UserLogin entity
 * 
 */

// @formatter:off
@Entity
@NamedNativeQueries(value = {
	@NamedNativeQuery(name = "login.getUserLoginByUser", query = "{$query: { userId : ':userId'} }", resultSetMapping = "login.setUserLoginByUser")
})

@SqlResultSetMappings(value = {
	@SqlResultSetMapping(name="login.setUserLoginByUser", entities = @EntityResult(entityClass = UserLogin.class))
})
// @formatter:on
@Table(name = "user_login")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UserLogin extends DateEntity {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserLogin.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	private String userAgent;
	
	private String os;
	
	private String remoteAddr;
	
	private User user;


	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public UserLogin() {
	}
	
	public UserLogin(User user) {
		setUser(user);
	}	

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public int compareTo(UserLogin o) {
		return 0;
	}	
	

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}
	
	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	

}
