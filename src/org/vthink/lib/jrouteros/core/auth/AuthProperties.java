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
package org.vthink.lib.jrouteros.core.auth;

import org.vthink.lib.jrouteros.core.auth.properties.AuthPropertiesReader;
import org.vthink.lib.jrouteros.core.auth.properties.AuthPropertiesWriter;
import org.vthink.lib.jrouteros.core.entity.Auth;



/**
 * This class is used for read or write
 * authentication in properties file that will be used
 * for MikroTik RouterOS
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class AuthProperties {
	private AuthPropertiesReader reader;
	private AuthPropertiesWriter writer;
	
	/**
	 * This is Constructor of Class {@link AuthProperties}
	 * @param pathToPropertiesFile String
	 */
	public AuthProperties(String pathToPropertiesFile){
		this.reader  = new AuthPropertiesReader(pathToPropertiesFile);
		this.writer = new AuthPropertiesWriter(pathToPropertiesFile);
	}
	
	/**
	 * This method used for save Authentication for MikroTik RouterOS
	 * in Properties file type.
	 * @param authObject {@link Auth}
	 * @return boolean
	 */
	public boolean save(Auth authObject){
		return this.writer.writeToProperties(authObject);
	}
	
	/**
	 * This method will fetching data from properties file
	 * as an Auth Class Object
	 * @return {@link Auth}
	 */
	public Auth getAuth(){
		return this.reader.getPropertiesData();
	}
}
