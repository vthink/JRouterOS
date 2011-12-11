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

import org.vthink.lib.jrouteros.core.entity.Auth;
import org.vthink.lib.jrouteros.core.talker.Talker;

/**
 * This class is used for accessing class Talker,
 * with this class you can specify way of authentication
 * to MikroTik RouterOS API.
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class AuthService {
	private Talker talker;
	private Auth authObject;
	private AuthXML authXML;
	private AuthProperties authProp;

	private String authXMLPath;
	private String authPropertiesPath;
	private String authType;


	public AuthService(){
	}

	/**
	 * There are 4 (four) type that are valid,
	 * direct, xml, and sqllite.<br/>
	 * 
	 * If you write <b><i>direct<i/></b> then you just access one time
	 * QueryService for accessing MikroTik API.<br/>
	 * 
	 * If you write <b><i>xml</i></b>, you have to specify 
	 * path of xml file that you've created before with
	 * method {@link AuthService#setXMLPathFile(String)}.<br/>
	 * 
	 * If you write <b><i>properties</i></b>, you have to specify
	 * path of properties file that you've created before
	 * with method {@link AuthService#setPropertiesPathFile(String)}
	 * @param type String
	 */
	public void setAuthType(String type){
		this.authType = type;
	}

	/**
	 * Set XMl File Configuration for Authentication to MikroTik RouterOS
	 * @param pathToXMLFile String
	 */
	public void setXMLPathFile(String pathToXMLFile){
		this.authXMLPath = pathToXMLFile;
	}
	
	/**
	 * @param pathToPropertiesFile String
	 */
	public void setPropertiesPathFile(String pathToPropertiesFile){
		this.authPropertiesPath = pathToPropertiesFile;
	}

	/**
	 * Set Direct Configuration for Authentication to MikroTik RouterOS
	 * @param authObject Auth
	 */
	public void setDirect(Auth authObject){
		this.authObject = authObject;
	}

	/**
	 * Get Auth Object based on type
	 * @return Auth
	 */
	public Auth getAuth(){
		if (this.authType.equals("direct")){
			return this.authObject;
		} else if (this.authType.equals("xml")){
			authXML = new AuthXML(this.authXMLPath);
			return authXML.getAuth();
		} else if (this.authType.equals("properties")){
			//not finished yet
			authProp = new AuthProperties(this.authPropertiesPath);
			return authProp.getAuth();
		} else {
			return null;
		}
	}

	/**
	 * Get Talker for executing command to MikroTik RouterOS
	 * @return Talker 
	 */
	public Talker getRouterOSTalker(){
		this.authObject = this.getAuth();
		talker = new Talker(
				this.authObject.getHostname(), this.authObject.getPort(),
				this.authObject.getUsername(), this.authObject.getPassword()
				);
		return talker;
	}


}
