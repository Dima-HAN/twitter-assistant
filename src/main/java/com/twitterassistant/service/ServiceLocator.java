package com.twitterassistant.service;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import com.twitterassistant.security.UserDetailsServiceImpl;
import com.twitterassistant.service.mail.SendMail;
import com.twitterassistant.service.social.SocialConnectionServiceImpl;
import com.twitterassistant.service.user.UserServiceImpl;

/**
 * Service Locator
 */
@Named
public class ServiceLocator implements ApplicationContextAware {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(ServiceLocator.class);

	private static ApplicationContext applicationContext = null;

	private static Properties props = null;

	private static final String RESPONSE_NAME_AT_ATTRIBUTES = ServletRequestAttributes.class.getName() + ".ATTRIBUTE_NAME";

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	public static Properties propertiesService() {
		if (props == null) {
			Resource resource = new ClassPathResource("com/twitterassistant/twit.properties");
			try {
				props = PropertiesLoaderUtils.loadProperties(resource);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return props;
	}

	public static UserServiceImpl userService() {
		return applicationContext.getBean(UserServiceImpl.class);
	}
	
	public static SocialConnectionServiceImpl socialConnectionService() {
		return applicationContext.getBean(SocialConnectionServiceImpl.class);
	}

	public static UserDetailsServiceImpl userDetailsService() {
		return applicationContext.getBean(UserDetailsServiceImpl.class);
	}

	public static MessageSource messageSourceService() {
		return applicationContext.getBean(MessageSource.class);
	}

	public static SendMail mailService() {
		return applicationContext.getBean(SendMail.class);
	}

	public static HttpServletRequest requestService() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest();
	}

	public static HttpSession sessionService() {
		return requestService().getSession(true);
	}

	public static GenericManageableCaptchaService captchaService() {
		return applicationContext.getBean(GenericManageableCaptchaService.class);
	}

	public static Runnable taskService(String beanId) {
		return (Runnable) applicationContext.getBean(beanId);
	}

	public static Properties config() {
		Properties config = new Properties();

		try {
			config = applicationContext.getBean(PropertiesFactoryBean.class).getObject();
		} catch (IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
		}

		return config;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

} // end of class
