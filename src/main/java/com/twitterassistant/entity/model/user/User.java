package com.twitterassistant.entity.model.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.DateEntity;
import com.twitterassistant.entity.model.social.SocialConnection;
/**
 * User entity
 * 
 */

// @formatter:off
@Entity
//@Indexed(index="user")
// @formatter:on
@Table(name = "user")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class User extends DateEntity {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(User.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	private String firstName;	
	
	private String lastName;
	
	private Plan plan;
	
	private Set<Permission> permissions;
	
	@JsonIgnore
	private Set<SocialConnection> socialConnections;
	
	private UserStatus userStatus;
	
	private UserLogin userLogin;


	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public User() {
	}

	

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public int compareTo(User o) {
		return 0;
	}
	
	public void addPermission(Permission permission) {
		if (this.permissions != null)
			this.permissions.add(permission);
		else {
			Set<Permission> permissions = new HashSet<Permission>();
			permissions.add(permission);
			this.permissions = permissions;
		}
	}
	
	@Transient
	public boolean isActive() {
		return getUserStatus() == UserStatus.ENABLED;
	}


	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	@NotNull
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Enumerated(EnumType.STRING)
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	
	@ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Set<SocialConnection> getSocialConnections() {
		return socialConnections;
	}

	public void setSocialConnections(Set<SocialConnection> socialConnections) {
		this.socialConnections = socialConnections;
		for(SocialConnection sc : socialConnections)
			sc.setUser(this);
	}
	
	@NotNull
	@Enumerated(EnumType.STRING)
	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public UserLogin getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(UserLogin userLogin) {
		this.userLogin = userLogin;
	}	

}
