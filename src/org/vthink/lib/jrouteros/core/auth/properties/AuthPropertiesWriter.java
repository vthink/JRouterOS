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
package org.vthink.lib.jrouteros.core.auth.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.vthink.lib.jrouteros.core.entity.Auth;

/**
 * This class used to writing MikroTik RouterOS
 * authentication in Properties File.
 * 
 * @author Lalu Erfandi Maula Yusnu
 *
 */
public class AuthPropertiesWriter {
	private Properties prop;
	private String path;
	
	/**
	 * This is constructor of class {@link AuthPropertiesWriter}
	 * 
	 * @param pathToPropertiesFile String
	 */
	public AuthPropertiesWriter(String pathToPropertiesFile){
		this.prop = new Properties();
		this.path = pathToPropertiesFile;
	}
	
	/**
	 * This method used for writing MikroTik RouterOS 
	 * authentication to Properties file.
	 * 
	 * @param authObject {@link Auth}
	 * @return boolean
	 */
	public boolean writeToProperties(Auth authObject){
		this.prop.setProperty("hostname", authObject.getHostname());
		this.prop.setProperty("port", String.valueOf(authObject.getPort()));
		this.prop.setProperty("username", authObject.getUsername());
		this.prop.setProperty("password", authObject.getPassword());
		try {
			this.prop.store(new FileOutputStream(new File(this.path)), "JRouterOS Properties File");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
