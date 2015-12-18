package com.twitterassistant.service.social;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.twitterassistant.dao.social.SocialConnectionDao;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialProvider;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.service.SearchResult;

@Named
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class SocialConnectionServiceImpl {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SocialConnectionServiceImpl.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Inject
	SocialConnectionDao socialConnectionDao;	
	

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public SocialConnection getConnectionByUserId(SocialProvider provider, String userId) {
		try {
			return socialConnectionDao.getConnectionByUserId(provider, userId); 
		} catch (NoResultException e) {
			return null;
		}		
	}	
	
	public SocialConnection getConnectionById(String id) {
		try {
			return socialConnectionDao.getById(id); 
		} catch (NoResultException e) {
			return null;
		}		
	}	
	
	public SocialConnection getConnectionByUuid(UUID uuid) {
		try {
			return socialConnectionDao.getByUuid(uuid); 
		} catch (NoResultException e) {
			return null;
		}		
	}	
	
	//public SearchResult<SocialConnection> list(int firstResult, int maxResults, Map<String, Object> filters, Order sortOrder) {		
		//return socialConnectionDao.list(firstResult, maxResults, filters, sortOrder);
	//}

	@Transactional(readOnly = false)
	public SocialConnection update(SocialConnection socialConnection) {			
		socialConnectionDao.updateObject(socialConnection);	
		return socialConnection;
	}	
	
	@Transactional(readOnly = false)
	public void delete(SocialConnection socialConnection) {		
		//kill connection
		socialConnectionDao.deleteObject(socialConnection);
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
