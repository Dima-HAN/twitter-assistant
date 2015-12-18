package com.twitterassistant.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.twitterassistant.util.CookieUtils;


public class UserLoginRedirectFilter extends OncePerRequestFilter {

	//This filter is called before spring redirects to signin page
	//class saves that particular request, so we can redirect to the requested page after sign in
	
	// Constants ---------------------------------------------------------------------------------------------- Constants
	private static final transient Logger LOG = LoggerFactory.getLogger(UserLoginRedirectFilter.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
	private RequestMatcher requestMatcher = new AntPathRequestMatcher("/account/**");

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (request.getMethod().equals("GET") && requestMatcher.matches(request) && trustResolver.isAnonymous(authentication)) {
			Object uri = request.getRequestURI();
			Object query = request.getQueryString();
			CookieUtils.setValue(response, CookieUtils.LAST_PAGE_ACCESS_KEY, uri.toString() + (query != null ? ("?" + query.toString()) : ""));
		}
		filterChain.doFilter(request, response);
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}