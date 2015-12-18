package com.twitterassistant.web.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.twitterassistant.util.SpringUtils;

@Controller
@RequestMapping("/")
public class DefaultController {

  // Constants ---------------------------------------------------------------------------------------------- Constants

  private static final transient Logger LOG = LoggerFactory.getLogger(DefaultController.class);

  // Instance Variables ---------------------------------------------------------------------------- Instance Variables

  // Constructors ---------------------------------------------------------------------------------------- Constructors

  // Public Methods ------------------------------------------------------------------------------------ Public Methods
  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{view}")
	public String page(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable("view") String view) {
  	return SpringUtils.assertView(request, response, "/twit/index");
  	//return SpringUtils.assertView(request, response, "/twit/" + view);
	} 
  
  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{folder}/{view}")
	public String page(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable("folder") String folder, @PathVariable("view") String view) {
  	return SpringUtils.assertView(request, response, "/" + folder + "/" + view);
	} 


  // Protected Methods ------------------------------------------------------------------------------ Protected Methods

  // Private Methods ---------------------------------------------------------------------------------- Private Methods

  // Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class