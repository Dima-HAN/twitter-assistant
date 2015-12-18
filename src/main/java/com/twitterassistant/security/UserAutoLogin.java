package com.twitterassistant.security;

import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.service.ServiceLocator;


/**
 * User Auto Login
 * 
 */

@Service("userAutoLogin")
public class UserAutoLogin {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserAdapter.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	@Inject
	private AuthenticationSuccessHandler successHandler;
	

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	public void login(HttpServletRequest request, HttpServletResponse response, SocialConnection sc) {
		UserDetails ud = ServiceLocator.userDetailsService().loadUserFromEntity(sc); 
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
		try {			
			SecurityContextHolder.getContext().setAuthentication(auth);
			// save twitter connection uuid in session
			request.getSession(true).setAttribute("sc-uuid", ((UserAdapter) auth.getPrincipal()).getUuid());
			
			successHandler.onAuthenticationSuccess(request, response, auth);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e); 
		}
	}
	
	public void login(HttpServletRequest request, HttpServletResponse response, UUID scUuid) throws Exception {		
		SocialConnection sc = ServiceLocator.socialConnectionService().getConnectionByUuid(scUuid);
		if(sc != null)
			login(request, response, sc);
		else
			LOG.error("Social connection : " + scUuid + " is not found for AutoLogin!");		
	}
	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
