package com.twitterassistant.dao.social;

import java.util.Map;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.twitterassistant.dao.BaseDao;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialProvider;


@Named
public class SocialConnectionDao extends BaseDao<SocialConnection> {
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SocialConnectionDao.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public SocialConnection getConnectionByUserId(SocialProvider provider, String userId) {		
		Query query = createNativeQuery("social.getConnectionUserById");		
		query.setParameter("providerUserId", userId);
		query.setParameter("provider", provider);			
		return query.getSingleResult();
  }

	@Override
	public void applyFilters(Map<String, Object> filters, BasicDBObjectBuilder builder) {
		// TODO Auto-generated method stub
		
	}
	
}
