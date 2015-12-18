package com.twitterassistant.aop;

import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.twitterassistant.annotation.UserScope;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.service.ServiceLocator;
import com.twitterassistant.service.social.SocialConnectionServiceImpl;
import com.twitterassistant.util.CookieUtils;

@Component
@Aspect
@Order(2)
public class AccountAspect {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(AccountAspect.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Inject
	private SocialConnectionServiceImpl socialConnectionServiceImpl;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	@Before("@annotation(annotation)")
	public void account(JoinPoint joinPoint, UserScope annotation) {
		HttpSession session = ServiceLocator.sessionService();
		HttpServletRequest request = ServiceLocator.requestService();
		UUID uuid;
		if (request.getAttribute("user") == null && session != null && session.getAttribute("sc-uuid") != null && (uuid = UUID.fromString(session.getAttribute("sc-uuid").toString()) ) != null) {
			SocialConnection sc = socialConnectionServiceImpl.getConnectionByUuid(uuid);
			if (sc != null) {
				request.setAttribute("sc", sc);		
				request.setAttribute("user", sc.getUser());				
				//if (session.getAttribute("data") == null)
					//session.setAttribute("data", new SessionModel(user));
			}
		}
	}	

	// saved last visited page
	@Before("@annotation(annotation)")
	public void lastVisited(JoinPoint joinPoint, UserScope annotation) {		
		// parameters must be REQUEST and RESPONSE atleast
		if (joinPoint.getArgs().length >= 2 && joinPoint.getArgs()[0] instanceof HttpServletRequest && joinPoint.getArgs()[1] instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];	
			if (request.getMethod().equals("GET")) {				
				Object uri = request.getRequestURI();
				Object query = request.getQueryString();
				if (uri != null && (uri.toString().startsWith("/account/") == true) && uri.toString().contains("/signin") == false && uri.toString().contains("/signup") == false && uri.toString().contains("/dialog") == false) {
					HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[1];					
					CookieUtils.setValue(response, CookieUtils.LAST_PAGE_ACCESS_KEY, uri.toString() + (query != null ? ("?" + query.toString()) : ""));
				}				
			}
		}		
	}	

}
