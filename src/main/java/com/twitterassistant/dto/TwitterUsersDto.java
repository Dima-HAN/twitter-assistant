package com.twitterassistant.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.scribe.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.model.twitter.Follower;
import com.twitterassistant.entity.model.twitter.Friend;
import com.twitterassistant.entity.model.twitter.TwitterUser;
import com.twitterassistant.entity.model.twitter.TwitterUserType;
import com.twitterassistant.util.JsonUtils;

/**
 * TwitterUsersDto.java
 * 
 */
public class TwitterUsersDto {
	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(TwitterUsersDto.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	private int limit; // total limit allowed
	private int remaining; // how much left, mostly it will be 0
	private Date resetDate; // date when it will be reset
	private String cursor; //current cursor, 0 not more data, -1 first set
	
	private List<TwitterUser> users; //list of users

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public TwitterUsersDto(TwitterUserType type, Response response) {
		HashMap<String, Object> data = JsonUtils.toObject(response.getBody(), HashMap.class);
		users = new ArrayList<TwitterUser>();
		
		setLimit(Integer.parseInt(response.getHeader("x-rate-limit-limit")));
		setRemaining(Integer.parseInt(response.getHeader("x-rate-limit-remaining")));
		setResetDate(new Date(Long.parseLong(response.getHeader("x-rate-limit-reset"))));
		setCursor(data.get("next_cursor").toString());
		
		if(data.get("users") != null) {
			for(HashMap<String, Object> u : (List<HashMap<String, Object>>) data.get("users")) {
				if(type == TwitterUserType.FOLLOWER) 
					users.add(new Follower(u.get("id").toString(), u.get("name").toString(), u.get("screen_name").toString(), u.get("profile_image_url").toString(), (Integer) u.get("followers_count"), (Integer) u.get("friends_count")));
				else if(type == TwitterUserType.FRIEND) 
					users.add(new Friend(u.get("id").toString(), u.get("name").toString(), u.get("screen_name").toString(), u.get("profile_image_url").toString(), (Integer) u.get("followers_count"), (Integer) u.get("friends_count")));					
			}
		}
	}
	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public Date getResetDate() {
		return resetDate;
	}

	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public List<TwitterUser> getUsers() {
		return users;
	}

	public void setUsers(List<TwitterUser> users) {
		this.users = users;
	}
}
