package com.twitterassistant.dao.twitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.twitterassistant.dao.BaseDao;
import com.twitterassistant.dao.BaseDao.Query;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialProvider;
import com.twitterassistant.entity.model.twitter.Follower;
import com.twitterassistant.entity.model.twitter.TwitterUser;


@Named
public class FollowerDao extends BaseDao<Follower> {
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(FollowerDao.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public List<Follower> getFollowers(UUID socialConnectionId) {		
		/*Query query = createNativeQuery("twitter.getFollowers");		
		query.setParameter("socialConnectionId", socialConnectionId);
		query.setFirstResult(0);
		query.setMaxResults(10);
		return query.getResultList();*/
		return list(0, 0, null, null);
  }

	@Override
	public void applyFilters(Map<String, Object> filters, BasicDBObjectBuilder builder) {
		
		if(filters.get("socialConnectionId") != null)
			builder.add("socialConnectionId", filters.get("socialConnectionId"));
			
		
	}

}
