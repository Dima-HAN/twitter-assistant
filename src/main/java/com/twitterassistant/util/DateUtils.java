package com.twitterassistant.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateUtils
 */
public final class DateUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(DateUtils.class);

	public static final String DB_FORMAT_DATETIME = "yyyy-M-d HH:mm:ss";
	public static final String STND_FORMAT_DATETIME = "MM/dd/yyyy HH:mm:ss";
	public static final String STND_FORMAT_DATE = "mm/dd/yyyy";
	public static final String FULL_FORMAT_DATE = "MMMM d, yyyy";
	public static final String FULL_TIME_FORMAT_DATE = "MMMM d, yyyy HH:mm";
	public static final String SHORT_FORMAT_DATE = "MMM d, yyyy";
	
	public static final int DAY = 1;
	public static final int HOUR = 2;
	public static final long DAY_IN_MILLIS = 86400000;

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private DateUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	public static int dateDiff(Date date, Date date2, int type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);		
		long difference = cal2.getTimeInMillis() - cal.getTimeInMillis();
		
		switch(type) {
		case DAY:
			return (int) (difference / DAY_IN_MILLIS);
		case HOUR:
			return (int) (difference / 1000 / 60 / 60);				
		}		
		
		return 0;
	}
	
	public static String dateFormat(Date date, String format) {
		if (date == null)
			date = new Date();
		SimpleDateFormat frmt = new SimpleDateFormat(format);
		return frmt.format(date);
	}

	public static String toPeriodAgo(long time) {
		Date date = new Date();
		date.setTime(time);
		return toPeriodAgo(date);
	}

	public static String toPeriodAgo(String dateStr) {
		// The format of your date string assuming the 1 am would read 01:00, not 1:00
		// and Jan 1, 2010 would read 2010-1-1, not 2010-01-01
		final DateFormat formatter = new SimpleDateFormat(DB_FORMAT_DATETIME);
		// The calendar instance which adds a locale to the date
		try {
			// Parse the date string to return a Date object
			return toPeriodAgo(formatter.parse(dateStr));
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
			return "long time ago";
		}
	}

	public static String toPeriodAgo(Date dateObj) {
		if (dateObj == null)
			return "long time ago";
		String agoStr = null;		
		long now = System.currentTimeMillis();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dateObj);
		long then = cal.getTimeInMillis();
		long difference = now - then;
		int ago;

		if (difference >= DAY_IN_MILLIS * 365) { // check for years
			ago = (int) (difference / DAY_IN_MILLIS / 365);
			agoStr = String.format("%d year%s ago", ago, ago == 1 ? "" : "s");
		} else if (difference >= DAY_IN_MILLIS * 30) { // working with month
			agoStr = String.format("%d month ago", (int) (difference / DAY_IN_MILLIS / 30));
		} else if (difference >= DAY_IN_MILLIS * 7) { // working with weeks
			ago = (int) (difference / DAY_IN_MILLIS / 7);
			agoStr = String.format("%d week%s ago", ago, ago == 1 ? "" : "s");
		} else if (difference >= DAY_IN_MILLIS) { // working with days
			ago = (int) (difference / DAY_IN_MILLIS);
			agoStr = String.format("%d day%s ago", ago, ago == 1 ? "" : "s");
		} else if (difference >= 1000 * 60 * 60) { // working with hours
			ago = (int) (difference / 1000 / 60 / 60);
			agoStr = String.format("%d hour%s ago", ago, ago == 1 ? "" : "s");
		} else if (difference >= 1000 * 60) { // working with minutes
			ago = (int) (difference / 1000 / 60);
			agoStr = String.format("%d minute%s ago", ago, ago == 1 ? "" : "s");
		} else { // working with seconds
			ago = (int) (difference / 1000);
			agoStr = String.format("%d second%s ago", ago, ago == 1 ? "" : "s");
		}

		return agoStr;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

