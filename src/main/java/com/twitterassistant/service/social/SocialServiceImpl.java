package com.twitterassistant.service.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.twitterassistant.annotation.event.Event;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialPost;
import com.twitterassistant.entity.model.social.SocialProvider;
import com.twitterassistant.entity.model.user.Permission;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.entity.model.user.UserStatus;
import com.twitterassistant.security.UserAutoLogin;
import com.twitterassistant.service.event.EventServiceImpl;
import com.twitterassistant.service.user.UserServiceImpl;
import com.twitterassistant.util.StringUtils;

@Named
public class SocialServiceImpl implements ApplicationContextAware {
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SocialServiceImpl.class);

	private static ApplicationContext applicationContext = null;

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Inject
	private SocialConnectionServiceImpl socialConnectionServiceImpl;
	
	@Inject
	private EventServiceImpl eventServiceImpl;
	
	@Inject
	UserServiceImpl userServiceImpl; 
	
	@Inject
	UserAutoLogin userAutoLogin; 

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	public String getAuthorizationUrl(SocialProvider provider, String callback, String scope) {
		return getBean(provider.getName()).getAuthorizationUrl(callback, scope);
	}

	public SocialConnection connect(SocialProvider provider, String code, String callback) {
		return getBean(provider.getName()).connect(code, callback);
	}

	public HashMap<String, String> post(SocialConnection sc, HashMap<String, String> properties) {
		return getBean(sc.getProvider().getName()).post(sc, properties);
	}

	public ArrayList<HashMap<String, String>> getPages(SocialConnection sc) {
		return getBean(sc.getProvider().getName()).getPages(sc);
	}
	
	public ArrayList<HashMap<String, String>> getUserInfo(SocialConnection sc, String userId) {
		return getBean(sc.getProvider().getName()).getUserInfo(sc, userId);
	}

	// parses map and creates a proper request depending on a provider
	public HashMap<String, String> getRequestMap(SocialConnection sc, HashMap<String, String> map) {
		return getBean(sc.getProvider().getName()).getRequestMap(map);
	}

	public void setEmail(SocialProvider provider, String email) {
		getBean(provider.getName()).setEmail(email);
	}

	public void delete(SocialConnection sc, SocialPost post) {
		// call webservice
		if (post.getProviderPostId() != null)
			getBean(sc.getProvider().getName()).delete(sc, post);
		// delete from db
		// ServiceLocator.socialPostService().delete(post);
	}

	public void disconnect(SocialProvider provider, User user) {
		//SocialConnection social = socialConnectionServiceImpl.getConnection(provider, user.getId());
		//if (social != null)
		//	socialConnectionServiceImpl.delete(social);
	}

	public String signup(HttpServletRequest request, HttpServletResponse response, SocialConnection sc) { //, UserAutoLogin userAutoLogin
		
		//create a new user
		User user = new User();
		user.setFirstName(sc.getFirstName());
		user.setLastName(sc.getLastName());
		user.setUserStatus(UserStatus.ENABLED);
		user.addPermission(Permission.ROLE_FREE);
		
		//create user twitter default social connection
		Set<SocialConnection> conns = new HashSet<SocialConnection>();
		conns.add(sc);

		user.setSocialConnections(conns);
		userServiceImpl.update(user);

		// autologin
		userAutoLogin.login(request, response, sc); 

		return null;
	}
	
	//add a new twitter account to already existed user
	public String addAccount(SocialConnection sc, User user) { 		
		
		sc.setUser(user);
		socialConnectionServiceImpl.update(sc);
		
		//fire event account was changed, must rewrite session
		eventServiceImpl.publish(Event.ACCOUNT_CHANGE_EVENT, sc);

		return null;
	}


	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods
	private BaseSocial getBean(String beanName) {
		try {
			String className = StringUtils.camelize(beanName, false);
			return (BaseSocial) applicationContext.getBean(className);
		} catch (NoSuchBeanDefinitionException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}	

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

}
