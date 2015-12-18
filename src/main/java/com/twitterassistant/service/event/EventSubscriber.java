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

import java.lang.reflect.Method;
import java.util.List;


public class EventSubscriber {

	private String beanName;

	private List<Method> methods;

	public EventSubscriber(String beanName, List<Method> methods) {
		this.beanName = beanName;
		this.methods = methods;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
}
