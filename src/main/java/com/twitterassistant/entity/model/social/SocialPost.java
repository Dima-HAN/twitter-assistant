package com.twitterassistant.entity.model.social;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.DateEntity;
import com.twitterassistant.entity.model.user.User;
import com.twitterassistant.util.JsonUtils;

/**
 * SocialPost entity
 * 
 */
//@formatter:off
@Entity
@NamedQueries({ 
	//@NamedQuery(name = "social.post.getPostByUuid", query = "select sp from SocialPost sp join fetch sp.user u where sp.uuid = :uuid"),
	//@NamedQuery(name = "social.post.getPostsByStreamUuid", query = "select sp from SocialPost sp where sp.streamUuid = :uuid")
})
//@formatter:on
@Table(name = "social_post")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class SocialPost extends DateEntity {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SocialPost.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	private Date posted;	

	private Date startDate;	

	private SocialProvider provider;

	private String providerPostId;
	
	private int status;
	
	private String message;
	
	@JsonIgnore
	private String request;
	
	@JsonIgnore
	private String response;

	//@ManyToOne(fetch = FetchType.LAZY)
	//@Field(analyze = Analyze.NO)
	//@JsonIgnore
	//private User user;

	@Transient
	@JsonIgnore
	private long recordId;
	
	//@Transient
	//private HashMap<String, Object> property;

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public SocialPost() {
	}
	
	public SocialPost(long id, SocialProvider provider) {
		setProvider(provider);
		setRecordId(id);	//tmp save id of the property or post here
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public int compareTo(SocialPost o) {
		return 0;
	}
	
	//public HashMap<String, Object> getPropertiesObject() {
		//return getRequest() == null ? new HashMap<String, Object>() : JsonUtils.toObject(getRequest(), HashMap.class);
	//}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	/*public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}*/

	@Enumerated(EnumType.STRING)
	public SocialProvider getProvider() {
		return provider;
	}

	public void setProvider(SocialProvider provider) {
		this.provider = provider;
	}

	public String getProviderPostId() {
		return providerPostId;
	}

	public void setProviderPostId(String providerPostId) {
		this.providerPostId = providerPostId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		if(getPosted() == null && status == 200)
			setPosted(new Date());
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Date getPosted() {
		return posted;
	}

	public void setPosted(Date posted) {
		this.posted = posted;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	/*public HashMap<String, Object> getProperty() {
		//if(property == null)
			//setProperty(getPropertiesObject());
		return property;
	}

	public void setProperty(HashMap<String, Object> property) {
		this.property = property;
	}*/

}
