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


public interface EventService {

	/**
	 * Broadcast an event and its context
	 * @param event the event
	 * @param args the arguments
	 */
	void publish(String event, Object... args);

	/**
	 * Subscribes this objects as listener for a given event
	 * @param event
	 * @param listeners the service names
	 */
	void subscribe(String event, String... listeners);

	/**
	 * UnSubscribes this objects as listener for a given event
	 * @param event
	 * @param listeners the service names
	 */
	void unSubscribe(String event, String... listeners);

}