package com.twitterassistant.dao.user;

import java.util.Map;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.twitterassistant.dao.BaseDao;
import com.twitterassistant.entity.model.user.UserLogin;


@Named
public class UserLoginDao extends BaseDao<UserLogin> {
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserLoginDao.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	public UserLogin getUserLoginByUser(String userId) {		
		Query query = createNativeQuery("login.getUserLoginByUser");		
		query.setParameter("userId", userId);
		return (UserLogin) query.getSingleResult();
  }

	@Override
	public void applyFilters(Map<String, Object> filters, BasicDBObjectBuilder builder) {
		// TODO Auto-generated method stub
		
	}

}
