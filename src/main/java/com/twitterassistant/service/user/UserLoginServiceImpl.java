package com.twitterassistant.service.user;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.twitterassistant.dao.user.UserLoginDao;
import com.twitterassistant.entity.model.user.UserLogin;

@Named
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserLoginServiceImpl {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserLoginServiceImpl.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Inject
	UserLoginDao userLoginDao;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// returns user login info by user id
	public UserLogin getUserLoginByUser(String userId) {
		try {
			return userLoginDao.getUserLoginByUser(userId);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public UserLogin update(UserLogin userLogin) {
		userLoginDao.updateObject(userLogin);
		return userLogin;
	}

	@Transactional(readOnly = false)
	public void delete(UserLogin userLogin) {
		userLoginDao.deleteObject(userLogin);
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

}
