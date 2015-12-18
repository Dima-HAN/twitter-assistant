package com.twitterassistant.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CookieUtils
 */
public final class CookieUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(CookieUtils.class);

	public static final String LAST_PAGE_ACCESS_KEY = "TWITTER_SECURITY_SAVED_REQUEST_KEY";

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private CookieUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	public static String getValue(Cookie[] cookies, String cookieName, String defaultValue) {
		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName()))
					return (cookie.getValue());
			}
		return (defaultValue);
	}

	public static void setValue(HttpServletResponse response, String cookieName, String cookieValue) {
		setValue(response, cookieName, cookieValue, null);
	}

	public static void setValue(HttpServletResponse response, String cookieName, String cookieValue, Integer expire) {
		setValue(response, cookieName, cookieValue, expire, "/");
	}
	
	public static void setValue(HttpServletResponse response, String cookieName, String cookieValue, Integer expire, String path) {
		setValue(response, cookieName, cookieValue, expire, null, path);
	}
	
	public static void setValue(HttpServletResponse response, String cookieName, String cookieValue, Integer expire, String domain, String path) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		if(domain != null)
			cookie.setDomain(domain);
		if(path != null)
			cookie.setPath(path);
		if (expire != null)
			cookie.setMaxAge(expire); //expiration in seconds
		response.addCookie(cookie);
	}

	public static void removeValue(HttpServletResponse response, String cookieName) {
		setValue(response, cookieName, null, 0);
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

