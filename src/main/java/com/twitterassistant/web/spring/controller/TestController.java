package com.twitterassistant.web.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bull.javamelody.MonitoredWithSpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twitterassistant.annotation.AutoLogin;
import com.twitterassistant.annotation.UserScope;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.service.user.UserServiceImpl;
import com.twitterassistant.util.JsonUtils;
import com.twitterassistant.util.SpringUtils;

@Controller
@RequestMapping(value = "/test")
@MonitoredWithSpring
public class TestController {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(TestController.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	@Autowired
	private UserServiceImpl userServiceImpl;


	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody String post(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
  	
		userServiceImpl.update(user);
		
		System.out.println(user);
  	System.out.println(user.getFirstName());
  	System.out.println(user.getLastName());
  	return JsonUtils.toJson(user);
	} 
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}", produces = "application/json; charset=UTF-8")
	public @ResponseBody String get(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
  	
		User user = userServiceImpl.getById(id);
		
		System.out.println(user);
  	System.out.println(user.getFirstName());
  	System.out.println(user.getLastName());
  	return JsonUtils.toJson(user);
	} 
	

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of classss