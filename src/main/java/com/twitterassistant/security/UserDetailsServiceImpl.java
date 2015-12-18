package com.twitterassistant.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.user.Permission;

/**
 * Spring security authentication-provider
 * 
 * @param <SecurityRoleEntity>
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService { //UserDetailsServiceImpl<SecurityRoleEntity>

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors	

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	//checks if user has role
	public boolean hasRole(Permission role) {
		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (role.toString().equals(authority.getAuthority()))
				return true;
		}
		return false;
	}
	
	
	@Transactional(readOnly = true)
	public UserDetails loadUserFromEntity(SocialConnection sc) {
		com.twitterassistant.entity.model.user.User userEntity = sc.getUser();
		// lets create Spring security user object
		String username = sc.getProviderUserId();
		String password = UUID.randomUUID().toString();
		boolean enabled = userEntity.isActive();
		boolean accountNonExpired = true;// not in use for now
		boolean credentialsNonExpired = true;// not in use for now
		boolean accountNonLocked = userEntity.isActive();

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if(userEntity.getPermissions() != null)
			for (Permission role : userEntity.getPermissions()) {
				authorities.add(new SimpleGrantedAuthority(role.name()));
			}

		User user = new UserAdapter(sc.getUuid().toString(), username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		
		LOG.info(user.toString());
		return user;
	}


	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters


} // end of class