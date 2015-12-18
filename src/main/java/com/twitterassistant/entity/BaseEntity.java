package com.twitterassistant.entity;

import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 * BaseEntity
 */
@MappedSuperclass
public abstract class BaseEntity implements JpaEntity {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	public String id;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{id=[" + getId() + "]}";
	}

	public boolean equals(BaseEntity o) {
		return (getId().equalsIgnoreCase(o.getId()));
	}	
	
	public boolean equals(UUID uuid) {
		return (getId().equalsIgnoreCase(uuid.toString()));
	}
	
	/*public static String getIdDirect(BaseEntity entity) {
		if (entity instanceof HibernateProxy) {
			LazyInitializer lazyInitializer = ((HibernateProxy) entity).getHibernateLazyInitializer();
			if (lazyInitializer.isUninitialized()) {
				return (String) lazyInitializer.getIdentifier();
			}
		}
		return entity.getId();
	}*/

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

} // end of class
