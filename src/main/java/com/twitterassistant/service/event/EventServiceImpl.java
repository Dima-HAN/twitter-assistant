/*
 * Copyright (C) 2011 47 Degrees, LLC
 * http://47deg.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *    
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.twitterassistant.service.event;

import com.twitterassistant.annotation.event.EventListener;
import com.twitterassistant.annotation.event.EventPublisher;
import com.twitterassistant.service.event.EventService;
import com.twitterassistant.service.event.EventContextUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;

@Service
@Aspect
public class EventServiceImpl implements EventService, ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

	private static final transient Logger	LOG	= LoggerFactory.getLogger(EventServiceImpl.class);

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


	private Map<String, List<EventSubscriber>> eventListeners = new TreeMap<String, List<EventSubscriber>>();

	/**
	 * Handle an application event.
	 *
	 * @param contextRefreshedEvent the event to respond to
	 */
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		eventListeners.clear();
		Map<String, List<Method>> methodMap = EventContextUtils.findServicesWithMethodAnnotation(applicationContext, EventListener.class);
		for (Map.Entry<String, List<Method>> entry : methodMap.entrySet()) {
			for (Method method : entry.getValue()) {
				String[] events = method.getAnnotation(EventListener.class).value();
				for (String event : events) {
					subscribe(event.toString(), entry.getKey());
				}
			}
		}
	}


	/**
	 * Subscribes this objects as listener for a given event
	 *
	 * @param event
	 * @param listenerNames
	 */
	public synchronized void subscribe(String event, String... listenerNames) {
		LOG.debug(String.format("Subscribing %s to %s", Arrays.toString(listenerNames), event));
		List<EventSubscriber> subscribers = new ArrayList<EventSubscriber>();
		for (String listenerName : listenerNames) {
			Object bean = applicationContext.getBean(listenerName);
			Class<?> listenerType = bean.getClass();
			if (Advised.class.isAssignableFrom(listenerType)) {
				listenerType = ((Advised) bean).getTargetSource().getTargetClass();
			}
			Method[] methods = listenerType.getMethods();
			List<Method> subscribedMethods = new ArrayList<Method>();
			for (Method method : methods) {
				if (method.isAnnotationPresent(EventListener.class)) {
					if (Arrays.asList(method.getAnnotation(EventListener.class).value()).contains(event)) {
						subscribedMethods.add(method);
					}
				}
			}
			EventSubscriber eventSubscriber = new EventSubscriber(listenerName, subscribedMethods);
			subscribers.add(eventSubscriber);
		}
		getListenersForEvent(event, true).addAll(subscribers);
	}

	/**
	 * UnSubscribes this objects as listener for a given event
	 *
	 * @param event
	 * @param listenerNames
	 */
	public void unSubscribe(String event, String... listenerNames) {
		LOG.debug(String.format("UnSubscribing %s to %s", Arrays.toString(listenerNames), event));
		getListenersForEvent(event, false).removeAll(Arrays.asList(listenerNames));
	}

	private List<EventSubscriber> getListenersForEvent(String value, boolean create) {
		List<EventSubscriber> listeners = eventListeners.get(value);
		if (listeners == null && create) {
			listeners = new ArrayList<EventSubscriber>();
			eventListeners.put(value, listeners);
		}
		return listeners;
	}

	/**
	 * Broadcast an
	 *
	 * @param event
	 * @param args
	 */
	public void publish(String event, Object... args) {
		LOG.debug(String.format("Publishing %s", event));
		List<EventSubscriber> listenersForEvent = getListenersForEvent(event, false);
		if (listenersForEvent != null) {
			for (EventSubscriber listener : listenersForEvent) {
				Object bean = applicationContext.getBean(listener.getBeanName());
				for (Method method : listener.getMethods()) {
					try {
						method.invoke(bean, args);
					} catch (Throwable e) {
						LOG.error("Cannot publish event: " + event, e);
						//throw new RuntimeException(e);
					}
				}
			}
		}
	}
	

	/**
	 * Intercepts methods that declare @EventPublisher and broadcasts the return value to all listeners
	 *
	 * @param pjp proceeding join point
	 * @return the intercepted method returned object
	 * @throws Throwable in case something goes wrong in the actual method call
	 */
	@Around(value = "@annotation(com.twitterassistant.annotation.event.EventPublisher) && @annotation(eventPublisher)")
	public Object handleBroadcast(ProceedingJoinPoint pjp, EventPublisher eventPublisher) throws Throwable {
		Object retVal = pjp.proceed();
		String[] events = eventPublisher.value();
		for (String event : events) {
			LOG.debug(String.format("Intercepted %s", Arrays.toString(events)));
			publish(event, retVal);
		}
		return retVal;
	}
}
