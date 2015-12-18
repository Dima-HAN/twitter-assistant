package com.twitterassistant.entity;

import javax.persistence.MappedSuperclass;

/**
 * DefaultEntity
 */
@MappedSuperclass
public abstract class DefaultEntity implements JpaEntity { 

  // Constants ---------------------------------------------------------------------------------------------- Constants

  // Instance Variables ---------------------------------------------------------------------------- Instance Variables

  // Constructors ---------------------------------------------------------------------------------------- Constructors

  // Public Methods ------------------------------------------------------------------------------------ Public Methods

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{id=[" + getId() + "]}";
  }
  
  // Protected Methods ------------------------------------------------------------------------------ Protected Methods

  // Private Methods ---------------------------------------------------------------------------------- Private Methods

  // Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class
