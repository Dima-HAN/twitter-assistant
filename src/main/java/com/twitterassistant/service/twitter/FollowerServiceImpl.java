package com.twitterassistant.service.twitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.twitterassistant.dao.twitter.FollowerDao;
import com.twitterassistant.entity.model.twitter.Follower;

@Named
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class FollowerServiceImpl {
	
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(FollowerServiceImpl.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Inject
	private FollowerDao followerDao;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public List<Follower> getFollowers(UUID socialConnectionId) {
		return followerDao.getFollowers(socialConnectionId);
	}	
	
	public List<Follower> list(int firstResult, int maxResults, Map<String, Object> filters) {
		return followerDao.list(firstResult, maxResults, filters);
	}

	
	
	@Transactional(readOnly = false)
	public Follower update(Follower follower) {			
		followerDao.updateObject(follower);	
		return follower;
	}	
	
	@Transactional(readOnly = false)
	public void delete(Follower follower) {		
		//kill record
		followerDao.deleteObject(follower);
	}
	

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods	

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters


}
