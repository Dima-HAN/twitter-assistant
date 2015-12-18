package com.twitterassistant.dao.twitter;

import java.util.Map;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.twitterassistant.dao.BaseDao;
import com.twitterassistant.entity.model.twitter.Friend;


@Named
public class FriendDao extends BaseDao<Friend> {
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(FriendDao.class);



	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@Override
	public void applyFilters(Map<String, Object> filters, BasicDBObjectBuilder builder) {
		// TODO Auto-generated method stub
		
	}

}
