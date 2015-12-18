package com.twitterassistant.web.spring.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bull.javamelody.MonitoredWithSpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twitterassistant.annotation.AutoLogin;
import com.twitterassistant.annotation.UserScope;
import com.twitterassistant.annotation.event.Event;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.exception.RateLimitException;
import com.twitterassistant.service.event.EventServiceImpl;
import com.twitterassistant.service.social.Twitter;
import com.twitterassistant.util.SpringUtils;

@Controller
@RequestMapping(value = "/account")
@MonitoredWithSpring
public class AccountController {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(AccountController.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	@Autowired
	EventServiceImpl eventServiceImpl;

	@Autowired
	Twitter twitter;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@UserScope
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/{view}")
	public String page(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable("view") String view) {
		System.out.println("AccountController");
		return SpringUtils.assertView(request, response, "/account/" + view);
	}

	@UserScope
	@RequestMapping(method = RequestMethod.GET, value = "/switch/{uuid}")
	public String switchaccount(HttpServletRequest request, HttpServletResponse response, @PathVariable("uuid") UUID uuid) {
		User user = (User) request.getAttribute("user");
		// lets check if this account uuid belongs to the user and fire it
		for (SocialConnection sc : user.getSocialConnections())
			if (sc.equals(uuid))
				eventServiceImpl.publish(Event.ACCOUNT_CHANGE_EVENT, sc); // fire

		return "redirect:/account/dashboard";
	}

	@UserScope
	@RequestMapping(method = RequestMethod.GET, value = "/list/{userId}", produces = "application/json; charset=UTF-8")
	public @ResponseBody
	String list(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") String userId) {
		SocialConnection sc = (SocialConnection) request.getAttribute("sc");

		
		try {
			twitter.getUserFollowers(sc, userId, "-1", 5000);
		} catch (RateLimitException e) {
			
		} catch (Exception e) {
			
		}

		return "redirect:/account/dashboard";
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of classss