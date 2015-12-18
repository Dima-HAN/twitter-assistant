package com.twitterassistant.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StringUtils
 */
public final class StringUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger	LOG	= LoggerFactory.getLogger(StringUtils.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private StringUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// check if view does not exists, redirects to 404
	public static String camelize(String str, boolean startWithLowerCase) {
		String[] strings = org.apache.commons.lang.StringUtils.split(str.toLowerCase(), "_");
		for (int i = startWithLowerCase ? 1 : 0; i < strings.length; i++) {
			strings[i] = org.apache.commons.lang.StringUtils.capitalize(strings[i]);
		}
		return org.apache.commons.lang.StringUtils.join(strings);
	}

	public static boolean isLong(String val) {
		try {
			Long.parseLong(val);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean find(String pattern, String str) {
		return Pattern.compile(pattern).matcher(str).find();
	}
	
	public static String substring(String str, int length) {
		return str != null && str.length() > length ? (str.substring(0, length-3) + "...") : str;
	}
	
	public static String getResourceAsString(String resource) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(StringUtils.class.getResourceAsStream(resource), writer, "UTF-8");
			return writer.toString();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}		
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

