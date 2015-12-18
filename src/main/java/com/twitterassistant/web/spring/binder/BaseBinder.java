package com.twitterassistant.web.spring.binder;

import java.beans.PropertyEditorSupport;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*	How to use
 * 
 * 	@InitBinder
 public void setDataBinder(WebDataBinder dataBinder) {
 dataBinder.registerCustomEditor(String.class, new StringEscapeEditor(false, false, false));
 }
 */

public class BaseBinder extends PropertyEditorSupport {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger	LOG								= LoggerFactory.getLogger(BaseBinder.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public BaseBinder() {
		super();
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	public void setAsText(String text) {
	
		if (text == null) {
			setValue(null);
		} else {
			
			text = Jsoup.parse(text.replaceAll("\n", "br2n")).text();
			text = text.replaceAll("br2n", "\n");
			
			setValue(text);
		}
	}

	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}

}