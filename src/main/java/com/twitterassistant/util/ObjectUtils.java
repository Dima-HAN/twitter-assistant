package com.twitterassistant.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.model.SelectItem;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitterassistant.entity.BaseEntity;

public class ObjectUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(ObjectUtils.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public static boolean equals(BaseEntity obj, BaseEntity obj2) {
		return org.apache.commons.lang.ObjectUtils.equals(obj.getId(), obj2.getId());
	}
	
	public static <T> List<?> uniqueList(List<T> list) {
		List<T> newlist = new ArrayList<T>();
		for(T l : list)
			if (newlist.contains(l) == false) 
				newlist.add(l);		
		return newlist;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<?> shrinkList(List<?> list) {
		for (Iterator<T> i = (Iterator<T>) list.iterator(); i.hasNext();) {
			if (i.next() == null) {
				i.remove();
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<?> shrinkSet(Set<?> set) {
		for (Iterator<T> i = (Iterator<T>) set.iterator(); i.hasNext();) {
			if (i.next() == null) {
				i.remove();
			}
		}
		return set;
	}

	public static <T> List<T> SetToList(Set<T> set) {
		List<T> list = new ArrayList<T>();
		if (set != null)
			for (T p : set) {
				list.add(p);
			}
		return list;
	}

	public static <T> List<T> EnumToList(T[] enums) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < enums.length; i++) {
			list.add(enums[i]);
		}
		return list;
	}

	public static SelectItem[] EnumToSelectItem(Object[] enums) {
		SelectItem[] selects = new SelectItem[enums.length];
		for (int i = 0; i < enums.length; i++) {
			selects[i] = new SelectItem(enums[i], ((Enum<?>) enums[i]).name());
		}
		return selects;
	}
	
	public static <T> Set<T> ListToSet(List<T> list) {
		return (list == null) ? new HashSet<T>() : new HashSet<T>(list);
	}
	
	public static <T> LinkedHashSet<T> ListToLinkedSet(List<T> list) {
		return (list == null) ? new LinkedHashSet<T>() : new LinkedHashSet<T>(list);
	}
	
	public static <T> TreeSet<T> ListToTreeSet(List<T> list) {
		return (list == null) ? new TreeSet<T>() : new TreeSet<T>(list);
	}

	public static <K, V> HashMap<K, V> ListToMap(List<HashMap<String, String>> list) {
		HashMap<K, V> map = new HashMap<K, V>();
		return map;
	}

	public static List<String> StringToList(String str) {
		return str == null || str.equals("[]") ? new ArrayList<String>() : Arrays.asList(str.replaceAll("[\\[\\]]*", "").split("\\s*,\\s*"));
	}

	public static List<Long> StringToLongList(String str) {
		if (str.equals("[]"))
			return new ArrayList<Long>();
		else {
			List<String> draft = StringToList(str);
			List<Long> list = new ArrayList<Long>();
			for (String id : draft)
				try {
					list.add(Long.parseLong(id));
				} catch (Exception e) {
					// just skip it
				}
			return list;
		}
	}

	public static HashMap<String, String> QueryToMap(String query) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			List<NameValuePair> list = URLEncodedUtils.parse(new URI("?" + query), "UTF-8");
			if (list.size() > 0) {
				for (NameValuePair param : list) {
					map.put(param.getName(), param.getValue());
				}
			}
		} catch (URISyntaxException e) {
			// do nothing
		}
		return map;
	}

	public static <E> List<Path<Object>> CsvToExprs(String csv, Root<E> root) {
		List<Path<Object>> list = new ArrayList<Path<Object>>();
		String[] vals = csv.split(",");
		for (int i = 0; i < vals.length; i++) {
			String val = vals[i].trim();
			if (val.length() > 0) {
				list.add(root.get(val));
			}
		}
		return list;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
}
