package com.twitterassistant.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

public class AuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(AuthenticationFailureHandler.class);

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		
		String status;
		if(exception.getMessage().equalsIgnoreCase("User account is locked"))
			status = "disabled";
		else
			status = "bad-credentials";
		
		//signin request is coming from the dialog window, json result is required
		if(request.getParameter("json") != null)
			setDefaultFailureUrl("/signin/" + status);
		else
			setDefaultFailureUrl("/signin?" + status);
		
		LOG.debug(exception.getMessage());
		
		try {
			super.onAuthenticationFailure(request, response, exception);
		} catch (ServletException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

}
