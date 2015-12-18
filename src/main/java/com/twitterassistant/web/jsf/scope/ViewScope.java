package com.twitterassistant.web.jsf.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * http://cagataycivici.wordpress.com/2010/02/17/port-jsf-2-0s-viewscope-to-spring-3-0/
 */
public class ViewScope implements Scope {

  // Constants ---------------------------------------------------------------------------------------------- Constants

  private static final transient Logger LOG = LoggerFactory.getLogger(ViewScope.class);

  // Instance Variables ---------------------------------------------------------------------------- Instance Variables

  // Constructors ---------------------------------------------------------------------------------------- Constructors

  // Public Methods ------------------------------------------------------------------------------------ Public Methods

  public Object get(String name, ObjectFactory objectFactory) { 
  	Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();

    if (viewMap.containsKey(name)) {
      return viewMap.get(name);
    }
    else {
      Object object = objectFactory.getObject();
      viewMap.put(name, object);
      return object;
    }
  }

  public Object remove(String name) {
    return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
  }

  public String getConversationId() {
    return null;
  }

  public void registerDestructionCallback(String name, Runnable callback) {
    //Not supported
  }

  public Object resolveContextualObject(String key) {
    return null;
  }


  // Protected Methods ------------------------------------------------------------------------------ Protected Methods

  // Private Methods ---------------------------------------------------------------------------------- Private Methods

  // Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class