package com.twitterassistant.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RateLimitException extends Exception {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(RateLimitException.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	private String message;
	private int limit; //total limit allowed
	private int remaining;  //how much left, mostly it will be 0
	private Date resetDate; //date when it will be reset

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	
	public RateLimitException(String limit, String remaining, String reset) {
		super();		
		setLimit(Integer.parseInt(limit));
		setRemaining(Integer.parseInt(remaining));
		setResetDate(new Date(Long.parseLong(reset)));
		message = "Rate limit exceeded: Limit: " + getLimit() + ", Remaining: " + getRemaining() + ", Reset at: " + getResetDate();
	}

	public RateLimitException(String err) {
		super(err);
		message = err;
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	public String getError() {
		return message;
	}
	
	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public Date getResetDate() {
		return resetDate;
	}

	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}
}
