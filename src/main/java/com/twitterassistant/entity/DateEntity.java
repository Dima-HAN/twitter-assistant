package com.twitterassistant.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * DateEntity
 */
@MappedSuperclass
public abstract class DateEntity extends BaseEntity {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	@Column(nullable = true)
	private Date modified;
	
	@Column(nullable = true, updatable = false)
	private Date created;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 36, updatable = false)
	private String uuid;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	//Returns:-1, 0 or 1 as this UUID is less than, equal to, or greater than val
	public int compareUuid(UUID uuid) {
		return (UUID.fromString(getUuid()).compareTo(uuid));
	}
	
	@PrePersist
	protected void onCreate() {
		created = new Date();
		uuid = UUID.randomUUID().toString();
	}
	
	@PreUpdate
	protected void onUpdate() {
		modified = new Date();
	}
	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

} // end of class
