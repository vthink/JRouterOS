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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.vthink.lib.jrouteros.core.entity.Auth;

/**
 * This class is used to reading properties file
 * for MikroTik RouterOS.
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class AuthPropertiesReader {
	private Properties prop;
	private Auth authObject;

	/**
	 * This is constructor of class {@link AuthPropertiesReader}
	 * 
	 * @param pathToPropertiesFile String
	 */
	public AuthPropertiesReader(String pathToPropertiesFile){
		this.prop = new Properties();
		try {
			this.prop.load(new FileInputStream(new File(pathToPropertiesFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used for fetching data from Properties file and
	 * convert it to Auth Class Object.
	 * @return {@link Auth}
	 */
	public Auth getPropertiesData(){
		this.authObject = new Auth(this.prop.getProperty("hostname"), 
				Integer.valueOf(this.prop.getProperty("port")), 
				this.prop.getProperty("username"), 
				this.prop.getProperty("password"));
		return this.authObject;
	}

}
