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

import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventContextUtils {

	/**
	 *
	 * @param applicationContext
	 * @param annotation
	 * @return a map of services and their methods annotated with annotation
	 */
	public static Map<String, List<Method>> findServicesWithMethodAnnotation(ApplicationContext applicationContext, Class<? extends Annotation> annotation) {
		Map<String, List<Method>> beanMap = new HashMap<String, List<Method>>();
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		if (allBeanNames != null) {
			for (String beanName : allBeanNames) {
				//skip some of them
				if(beanName.contains("Bean") || beanName.contains("scopedTarget"))
					continue;
				
				Object listener = applicationContext.getBean(beanName);
				Class<?> listenerType = listener.getClass();
				if (Advised.class.isAssignableFrom(listenerType)) {
					listenerType = ((Advised) listener).getTargetSource().getTargetClass();
				}
				Method[] methods = listenerType.getMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(annotation)) {
						List<Method> methodList = beanMap.get(beanName);
						if (methodList == null) {
							methodList = new ArrayList<Method>();
							beanMap.put(beanName, methodList);
						}
						methodList.add(method);
					}
				}
			}
		}
		return beanMap;
	}

}
