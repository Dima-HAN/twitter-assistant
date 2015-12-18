package com.twitterassistant.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendMail {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SendMail.class);
	
	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	@Autowired
	private VelocityEmailSender<?> velocityEmailSender;	
		
	@Value("#{twitProperties['dev.email.live']}")
	private boolean live;
	
	@Value("#{twitProperties['mail.from.support']}")
	private String supportEmail;	
	
	@Value("#{twitProperties['application.name']}")
	private String appName;
	
	@Value("#{twitProperties['twitterassistant.host']}")
	private String host;
	
	private String email = "%s <%s>";
	
	private String emailName = "%s %s <%s>";
	
	

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	

}
