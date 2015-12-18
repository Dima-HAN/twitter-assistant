package com.twitterassistant.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * JSON Utils
 */
public final class JsonUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

	private static ObjectMapper JSON_MAPPER = new ObjectMapper();

	private static com.fasterxml.jackson.databind.ObjectMapper JSON_MAPPER_LAZY = new com.fasterxml.jackson.databind.ObjectMapper();

	public static int LAZY_IGNORE = 1;

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private JsonUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	public static String toJson(Object object, int lazy) {
		LOG.trace("Converting object: {}", object);

		String string = null;

		// ignore lazy loading exceptions, https://github.com/FasterXML/jackson-module-hibernate
		mapperWithModule(false);

		try {
			string = JSON_MAPPER_LAZY.writeValueAsString(object);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return string;
	}

	public static String toJson(Object object, boolean prettyPrint) {
		LOG.trace("Converting object: {}", object);

		String string = null;
		
		JSON_MAPPER.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

		try {
			if (prettyPrint) {
				string = JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			} else {
				string = JSON_MAPPER.writeValueAsString(object);
			}			
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return string;
	}

	public static String toJson(Object object) {
		return toJson(object, false);
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		LOG.trace("Converting JSON:\n{}", json);

		T object = null;

		try {
			object = JSON_MAPPER.readValue(json, clazz);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return object;
	}
	
	public static <T> T toObject(InputStream ts, Class<T> clazz) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(ts, writer, "UTF-8");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
		return toObject(writer.toString(), clazz);
	}
	
	public static <T> HashMap<T, T> toHashMap(InputStream ts) {
		return toObject(ts, HashMap.class);
	}
	
	public static <T> HashMap<T, T> toHashMapResource(String path) {
		return toHashMap(JsonUtils.class.getResourceAsStream(path));
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods
	private static com.fasterxml.jackson.databind.ObjectMapper mapperWithModule(boolean forceLazyLoading) {
		Hibernate4Module mod = new Hibernate4Module();
		mod.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING, forceLazyLoading);
		JSON_MAPPER_LAZY.registerModule(mod);
		return JSON_MAPPER_LAZY;
	}
	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

