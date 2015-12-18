package com.twitterassistant.service.social;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialPost;
import com.twitterassistant.service.ServiceLocator;


/**
 * SocialAbstract.java
 * 
 */
public abstract class BaseSocial {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	protected static final transient Logger LOG = LoggerFactory.getLogger(BaseSocial.class);

	protected static String host = ServiceLocator.propertiesService().getProperty("twitterassistant.host");
	
	protected static String assetPath = ServiceLocator.propertiesService().getProperty("twitterassistant.assets.path");

	protected static final Token EMPTY_TOKEN = null;

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public OAuthService service() {
		return service(null, null);
	}
	
	public OAuthService service(String callback) {
		return service(callback, null);
	}
		
	public abstract OAuthService service(String callback, String scope);
	
	public abstract String getAuthorizationUrl(String callback, String scope);
	
	public abstract SocialConnection connect(String code, String callback);	
	
	public abstract HashMap<String, String> post(SocialConnection sc, HashMap<String, String> properties);		
	
	public abstract HashMap<String, String> getRequestMap(HashMap<String, String> map);
	
	public abstract void delete(SocialConnection sc, SocialPost post);	
	
	public abstract ArrayList<HashMap<String, String>> getPages(SocialConnection sc);	
	
	public abstract ArrayList<HashMap<String, String>> getUserInfo(SocialConnection sc, String userId);	
	
	
	
	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	public Token getRequestToken() {
		HttpSession session = ServiceLocator.sessionService();
		if (session != null) {
			return (Token) session.getAttribute("__REQUEST_TOKEN__");
		}
		return null;
	}
	
	public void setRequestToken(Token token) {
		if(token != null) {
			HttpSession session = ServiceLocator.sessionService();
			if (session != null) {
				session.setAttribute("__REQUEST_TOKEN__", token);
			}
		}
	}
	
	public String getEmail() {
		HttpSession session = ServiceLocator.sessionService();
		if (session != null) {
			return (String) session.getAttribute("__EMAIL__");
		}
		return null;
	}
	
	public void setEmail(String email) {
		if(email != null) {
			HttpSession session = ServiceLocator.sessionService();
			if (session != null) {
				session.setAttribute("__EMAIL__", email);
			}
		}
	}
}
