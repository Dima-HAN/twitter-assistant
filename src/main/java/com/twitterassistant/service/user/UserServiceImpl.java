package com.twitterassistant.service.user;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.twitterassistant.dao.user.UserDao;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.service.SearchResult;

@Named
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserServiceImpl {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Inject
	UserDao userDao;	
	

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	//returns simply user object
	public User getById(String id) {
		return userDao.getById(id);
	}
	
	//public SearchResult<User> list(int firstResult, int maxResults, Map<String, Object> filters, Order sortOrder) {		
		//return userDao.list(firstResult, maxResults, filters, sortOrder);
	//}	

	@Transactional(readOnly = false)
	public User update(User user) {			
		userDao.updateObject(user);	
		return user;
	}	
	
	@Transactional(readOnly = false)
	public void delete(User user) {		
		//kill user
		userDao.deleteObject(user);
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
