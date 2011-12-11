/**
 * Copyright 2011 Virtual Think Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vthink.lib.jrouteros.core.entity;

import org.vthink.lib.jrouteros.core.talker.TalkerSender;

/**
 * This class needed by SentenceUtil and ResultUtil
 * for sending command and retrieve data from or to
 * Mikrotik RouterOS API. Use this class for your preparation
 * sending command. 
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class Attribute {
	private String clause;
    private String name;
    private String value;

    public Attribute() {
    }
    
    public Attribute(String clause, String name, String value) {
    	this.clause = clause;
    	this.name 	= name;	
        this.value 	= value;
    }

    public Attribute(String name, String value) {
    	this.name 	= name;	
        this.value 	= value;
    }

    
    /**
     * Get attribute name of MikroTik RouterOS
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set attribute name  of MikroTik RouterOS
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get attribute value from MikroTik RouterOS
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Set attribute value for MikroTik RouterOS
     * @param value String
     */
    public void setValue(String value) {
        this.value = value;
    }

	/**
	 * Get clause for {@link TalkerSender} checking pass
	 * @return String
	 */
	public String getClause() {
		return clause;
	}

	/**
	 * Set clause for {@link TalkerSender} checking pass 
	 * @param clause String
	 */
	public void setClause(String clause) {
		this.clause = clause;
	}
}