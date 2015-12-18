package com.twitterassistant.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SpringUtils
 */
public final class SpringUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger	LOG	= LoggerFactory.getLogger(SpringUtils.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private SpringUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// check if view does not exists, redirects to 404
	public static boolean isView(HttpServletRequest request, HttpServletResponse response, String view) {
		File file = new File(request.getServletContext().getRealPath("/WEB-INF/jsp" + view + ".jsp"));
		return (file.exists()) ? true : false;
	}

	public static String assertView(HttpServletRequest request, HttpServletResponse response, String view) {
		return assertView(request, response, view, "/error/404");
	}
	
	public static void accessDenied(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("/account/error/401").forward(request, response);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static String assertView(HttpServletRequest request, HttpServletResponse response, String view, String defaultView) {
		File file = new File(request.getServletContext().getRealPath("/WEB-INF/jsp" + view + ".jsp"));
		if (file.exists())
			return view;
		else {
			try {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				request.getRequestDispatcher(defaultView).forward(request, response);
				return defaultView;
			} catch (IllegalStateException ei) {
				//do nothing
				return defaultView;
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				return defaultView;
			}
		}
	}

	public static String bindPath(String path) {

		try {
			path = URLDecoder.decode(path.toLowerCase(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
			path = null;
		}

		if (path != null)
			path = Jsoup.parse(path).text();

		return path;
	}

	public static String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_CLIENT_IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();
		return ip;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

