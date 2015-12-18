package com.twitterassistant.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.twitterassistant.dao.BaseDao.Query;
import com.twitterassistant.entity.JpaEntity;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.service.SearchResult;

/**
 * Base DAO
 */
public abstract class BaseDao<T extends JpaEntity> {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private transient static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@PersistenceContext(unitName = "twitterassistant-mongo")
	protected EntityManager entityManager;

	protected FullTextEntityManager textEntityManager;

	private Class<T> entityClass;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	public BaseDao() {
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@Inject
	public void getTextEntityManager() {
		textEntityManager = Search.getFullTextEntityManager(entityManager);
	}

	public <E> void updateObject(E entity) {
		// lets nullify all the empty strings
		for (Field f : entity.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(String.class)) {
					String value = (String) f.get(entity);
					if (value != null && value.trim().equals("")) {
						f.set(entity, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Session session = entityManager.unwrap(Session.class);
		Transaction tx = session.getTransaction();
		tx.begin();
		if (((JpaEntity) entity).getId() == null)
			session.persist(entity);
		else
			session.merge(entity);
		tx.commit();
	}

	public void deleteObject(Object o) {
		Session session = entityManager.unwrap(Session.class);
		Transaction tx = session.getTransaction();
		tx.begin();
		session.delete(entityManager.merge(o));
		tx.commit();
	}

	public T getById(String id) {
		return entityManager.find(entityClass, id);
	}

	public T getByUuid(UUID uuid) {
		Query query = new Query();
		query.setQueryString("{$query: { uuid : ':uuid'} }");
		query.setParameter("uuid", uuid);
		return (T) query.getSingleResult();
	}

	public List<T> list(Map<String, Object> filters) {
		return list(0, 1, filters, null);
	}

	public List<T> list(Map<String, Object> filters, Order sortOrder) {
		return list(0, 1, filters, sortOrder);
	}

	public List<T> list(int firstResult, int maxResults, Map<String, Object> filters) {
		return list(firstResult, maxResults, filters, null);
	}

	public List<T> list(int firstResult, int maxResults, Map<String, Object> filters, Order sortOrder) {
		if (filters == null) {
			filters = Collections.emptyMap();
		}
		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
		applyFilters(filters, builder);

		// run query
		Query query = new Query();
		query.setQueryString(builder.get().toString());
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	public SearchResult<T> listLucene(int firstResult, int maxResults, Map<String, Object> filters, Order sortOrder) {
		if (filters == null) {
			filters = Collections.emptyMap();
		}

		QueryBuilder builder = textEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entityClass).get();
		// Query luceneQuery = builder.keyword().onField("firstName").matching("Nikita and Danila").createQuery();
		org.apache.lucene.search.Query luceneQuery = builder.all().createQuery();
		LOG.debug("Lucene Query: " + luceneQuery.toString());
		FullTextQuery query = textEntityManager.createFullTextQuery(luceneQuery, entityClass);
		query.setSort(new Sort(new SortField("lastName", SortField.STRING, true)));
		query.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.FIND_BY_ID);
		return new SearchResult<T>(query, firstResult, maxResults);
	}

	public abstract void applyFilters(Map<String, Object> filters, BasicDBObjectBuilder builder);

	// public abstract void applyFilters(Map<String, String> filters, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root, CriteriaQuery<T> criteriaQuery);

	// public abstract void queryHook(Map<String, String> filters, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root, CriteriaQuery<T> criteriaQuery);

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods
	protected Query createNativeQuery(String queryName) {
		return new Query(queryName);
	}

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	// Classes -------------------------------------------------------------------------------------------------- Classes
	public class Query {

		private String queryString;
		private int maxResults = 0;
		private int startPosition = -1;

		public Query() {

		}

		public Query(String queryName) {
			this.queryString = entityManager.unwrap(Session.class).getNamedQuery(queryName).getQueryString();
		}

		public void setQueryString(String queryString) {
			this.queryString = queryString;
		}

		public void setParameter(String key, Object value) {
			this.queryString = queryString.replace("':" + key + "'", "'" + value.toString() + "'");
		}

		public void setMaxResults(int maxResults) {
			this.maxResults = maxResults;
		}

		public void setFirstResult(int startPosition) {
			this.startPosition = startPosition;
		}

		public T getSingleResult() {
			javax.persistence.Query query = entityManager.createNativeQuery(queryString, entityClass);
			query.setMaxResults(1);
			return (T) query.getSingleResult();
		}

		public List<T> getResultList() {
			javax.persistence.Query query = entityManager.createNativeQuery(queryString, entityClass);
			if (startPosition != -1)
				query.setFirstResult(startPosition);
			if (maxResults > 0)
				query.setMaxResults(maxResults);
			return (List<T>) query.getResultList();
		}

	}

} // end of class
