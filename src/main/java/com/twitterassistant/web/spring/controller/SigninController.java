package com.twitterassistant.web.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.twitterassistant.annotation.AutoLogin;
import com.twitterassistant.annotation.UserScope;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialProvider;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.entity.model.user.UserStatus;
import com.twitterassistant.security.UserAutoLogin;
import com.twitterassistant.service.social.SocialConnectionServiceImpl;
import com.twitterassistant.service.social.SocialServiceImpl;
import com.twitterassistant.util.SpringUtils;
import com.twitterassistant.web.spring.binder.BaseBinder;

@Controller
@RequestMapping(value = "/signin")
public class SigninController {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SigninController.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	//@Autowired
	//private UserServiceImpl userServiceImpl;
	
	@Autowired
	SocialServiceImpl socialServiceImpl;
	
	@Autowired
	SocialConnectionServiceImpl socialConnectionServiceImpl;
	
	@Autowired
	UserAutoLogin userAutoLogin;
	

	private final String callback = "/signin/callback";

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@InitBinder
	public void setDataBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(String.class, new BaseBinder()); 
	}
	
	//@AutoLogin
	@RequestMapping(method = RequestMethod.GET)
	public String signin(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		return SpringUtils.assertView(request, response, "/twit-page/signin");
	}

	// Twitter Social Signin
	@RequestMapping(method = RequestMethod.POST, value = "/twitter/sessions")
	public String socialPost(HttpServletRequest request, HttpServletResponse response) {
		// get auth url first and redirect
		try {
			String url = socialServiceImpl.getAuthorizationUrl(SocialProvider.TWITTER, String.format(callback), null);
			return "redirect:" + (url != null ? url : ("/signin?connection-failed"));
		} catch (Exception e) {
			return "redirect:/error/accessdenied"; 
		}		
	}
	
	//Twitter signin callback
	@UserScope //this is required to add another account
	@RequestMapping(method = RequestMethod.GET, value = "/callback/{provider}", params = "oauth_token")
	public String socialCallbackOAuth(HttpServletRequest request, HttpServletResponse response, @PathVariable("provider") String provider, @RequestParam("oauth_token") String oAuthToken, @RequestParam("oauth_verifier") String code) {
		
		User user = (User) request.getAttribute("user"); 
		SocialProvider sp = SocialProvider.findByName(provider);
		SocialConnection sc;		
		
		//lets do full connect and return entity of social connection
		try {
			sc = socialServiceImpl.connect(sp, code, callback);
		} catch(Exception e) {
			return ("redirect:/signin?connection-failed");
		}
		
		if(sc == null)
			return ("redirect:/signin?connection-failed");
		
		//lets read if we have current connection already
		SocialConnection conn = socialConnectionServiceImpl.getConnectionByUserId(sp, sc.getProviderUserId());  

		//if connection found lets autologin user
		if(conn != null && conn.getUser() != null && conn.getUser().getUserStatus() == UserStatus.ENABLED) {
			
			userAutoLogin.login(request, response, conn);		 
		
		} else if(sc != null && user != null && user.getUserStatus() == UserStatus.ENABLED) {	

			//if connection is not found but user is logged on, lets add an account to his
			socialServiceImpl.addAccount(sc, user);
			
			return "redirect:/account/dashboard";
			
		} else {				
				
			try {				
				return socialServiceImpl.signup(request, response, sc);	//returns proper view
			}	catch (Exception e) {
				return "redirect:/signin?error";
			}	
			
		}

		return null;
	}

	
	

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class