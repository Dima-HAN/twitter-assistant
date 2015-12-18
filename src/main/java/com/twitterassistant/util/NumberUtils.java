package com.twitterassistant.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NumberUtils
 */
public final class NumberUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger	LOG	= LoggerFactory.getLogger(NumberUtils.class);
	
	private static Random random = new Random();
	
	private static String[] suffix = new String[]{"","K", "M", "B", "T"};
	
	private static int MAX_LENGTH = 4;

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private NumberUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	public static int rand(int max) {
		return random.nextInt(max);
	}	

	public static String priceShort(BigDecimal bigDecimal) {
    String r = new DecimalFormat("##0E0").format(bigDecimal);
    r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length()-1))/3]);
    return r.length() > MAX_LENGTH ?  r.replaceAll("\\.[0-9]+", "") : r;
	}
	
	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

