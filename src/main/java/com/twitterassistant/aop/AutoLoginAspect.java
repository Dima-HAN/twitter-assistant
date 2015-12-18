package com.twitterassistant.aop;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.twitterassistant.annotation.AutoLogin;
import com.twitterassistant.security.UserAutoLogin;

@Component
@Aspect
@Order(1)
public class AutoLoginAspect {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(AutoLoginAspect.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Autowired
	private UserAutoLogin userAutoLogin;

	@Value("#{twitProperties['project.environment']}")
	private String environment;

	@Value("#{twitProperties['dev.autologin']}")
	private boolean autologin;

	@Value("#{twitProperties['dev.autologin-scuuid']}")
	private UUID autoScUuid;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	/*@Before("@annotation(annotation)")
	public void autologin(JoinPoint joinPoint, AutoLogin annotation) throws Throwable {		
		if (environment.equals("dev") && autologin) {
			LOG.info("Autologin...only only for dev environment");
			userAutoLogin.login(autoUserId);
		}
	}*/
	
	@Before("@annotation(annotation) &&  args(request,response,model)")
	public void autologin(JoinPoint joinPoint, AutoLogin annotation, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Throwable {
		if (environment.equals("dev") && autologin) {
			LOG.info("Autologin...only only for dev environment"); 
			userAutoLogin.login(request, response, autoScUuid);
		}		
	}

	public UUID getAutoScUuid() {
		return autoScUuid;
	}

	public void setAutoScUuid(UUID autoScUuid) {
		this.autoScUuid = autoScUuid;
	}

}
