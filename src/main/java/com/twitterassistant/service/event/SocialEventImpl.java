package com.twitterassistant.service.event;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.annotation.event.Event;
import com.twitterassistant.annotation.event.EventListener;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.service.ServiceLocator;

@Named
public class SocialEventImpl {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SocialEventImpl.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables


	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	@EventListener(Event.ACCOUNT_CHANGE_EVENT)
	public void accountChange(SocialConnection sc) {

		HttpServletRequest request = ServiceLocator.requestService();
		HttpSession session = request.getSession();
		
		session.setAttribute("sc-uuid", sc.getUuid());
		request.setAttribute("sc", sc);
	}


	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
