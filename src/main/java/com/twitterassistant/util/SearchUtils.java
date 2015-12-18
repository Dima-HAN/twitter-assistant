package com.twitterassistant.util;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.BaseEntity;


/**
 * SearchUtils
 */
public final class SearchUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(SearchUtils.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private SearchUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	@SuppressWarnings("unchecked")
	public static <T> T search(List<T> list, T t) {
		for(T e : list) {
			if(((Comparable<T>) e).compareTo(t) == 1)
				 return e;
		}
    return null;
	}
	
	public static String search(List<String> list, String q) {
		if(list != null)
			for(String s : list)
				if(s.equals(q))
					return q;
    return null;
	}
	
	public static HashMap<String, Object> search(List<HashMap<String, Object>> list, String key, String val) {
		for(HashMap<String, Object> map : list) {
			if(map.containsKey(key) && map.get(key).equals(val))
				 return map;
		}
    return null;
	}
	
	/*public static <E> E search(List<E> list, Long id) {
		for(E e : list) {
			if(((BaseEntity) e).getId().longValue() == id)
				 return e;
		}
    return null;
	}*/
	
	public static boolean inArray(String[] arr, String q) {
		if(arr != null && q != null)
			for(String a : arr) 
				if(a.equals(q))
					return true;
    return false;
	}
	
	public static boolean inList(List<Long> list, Long id) {
		if(list != null)
			for(Long e : list) {
				if(e.longValue() == id.longValue())
					 return true;
			}
    return false;
	}
	
	public static boolean inList(LinkedHashSet<Long> list, Long id) {
		if(list != null)
			for(Long e : list) {
				if(e.longValue() == id.longValue())
					 return true;
			}
    return false;
	}
	
	/*public static boolean inSet(Set<Object> list, Long id) {
		if(list != null)
			for(Object e : list) {
				if(Long.parseLong(((BaseEntity) e).getId().toString()) == id)
					 return true;
			}
    return false;
	}*/
	

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

