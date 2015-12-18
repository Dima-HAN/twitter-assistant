package com.twitterassistant.security;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.entity.model.user.UserLogin;
import com.twitterassistant.service.social.SocialConnectionServiceImpl;
import com.twitterassistant.service.user.UserLoginServiceImpl;
import com.twitterassistant.service.user.UserServiceImpl;
import com.twitterassistant.util.CookieUtils;
import com.twitterassistant.util.SpringUtils;

public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger	LOG						= LoggerFactory.getLogger(User.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Inject
	private SocialConnectionServiceImpl								socialConnectionServiceImpl;

	@Inject
	UserLoginServiceImpl									userLoginServiceImpl;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		// get uuid from the user adapter
		UUID uuid = ((UserAdapter) authentication.getPrincipal()).getUuid();

		// save user uuid in session
		request.getSession(true).setAttribute("sc-uuid", uuid);

		SocialConnection sc = socialConnectionServiceImpl.getConnectionByUuid(uuid);
		
		User user = sc.getUser();
		
		LOG.info("User '" + user.getFirstName() + " " + user.getLastName() + "' (Twitter UUID: " + uuid + ") successfully signed in with Twitter ID: " + authentication.getName());
		
		// save user object in the request
		request.setAttribute("user", sc.getUser());

		// save current twitter account into the request also
		request.setAttribute("sc", sc);

		// lets save last login date
		try {
			UserLogin login = userLoginServiceImpl.getUserLoginByUser(sc.getUser().getId());
			if(login == null) 
				login = new UserLogin(sc.getUser());			
			//lets find the user agent
			UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
			ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));			
			login.setUserAgent(agent.getName() + " " + agent.getVersionNumber().getMajor());
			login.setOs(agent.getOperatingSystem().getName());
			login.setRemoteAddr(SpringUtils.getRemoteAddress(request));			
			login.setModified(new Date()); //this is required for the update to happen
			
			//update
			userLoginServiceImpl.update(login);
		} catch (Exception e) {
			LOG.error("Cannot save user's last login date", e);
		}

		if (sc.getUser() == null) {
			setDefaultTargetUrl("/signin?disabled");
			LOG.error("User (" + sc.getUser().getId() + ") is not defined.");
		} else {
			
			
			if(response != null) {			
			
				setDefaultTargetUrl(CookieUtils.getValue(request.getCookies(), CookieUtils.LAST_PAGE_ACCESS_KEY, "/account/dashboard"));
				
				try {
					super.onAuthenticationSuccess(request, response, authentication);
				} catch (ServletException e) {
					LOG.error(e.getMessage());
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
				
			}
			
		}

	}

}
