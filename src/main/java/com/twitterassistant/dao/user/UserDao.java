package com.twitterassistant.dao.user;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.twitterassistant.dao.BaseDao;
import com.twitterassistant.entity.model.user.Permission;
import com.twitterassistant.entity.model.user.User;


@Named
public class UserDao extends BaseDao<User> {
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(UserDao.class);



	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@Override
	public void applyFilters(Map<String, Object> filters, BasicDBObjectBuilder builder) {
		// TODO Auto-generated method stub
		
	}

}
