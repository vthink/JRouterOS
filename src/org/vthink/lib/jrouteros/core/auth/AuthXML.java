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

import org.vthink.lib.jrouteros.core.auth.xml.AuthXMLReader;
import org.vthink.lib.jrouteros.core.auth.xml.AuthXMLWriter;
import org.vthink.lib.jrouteros.core.entity.Auth;


/**
 * This class is used for read or write
 * authentication in xml file that will be used
 * for MikroTik RouterOS
 * on XML file type
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class AuthXML {
	private AuthXMLReader authReader;
	private AuthXMLWriter authWriter;

	/**
	 * This is Constructor of Class {@link AuthXML}
	 * @param pathToXMLFile String
	 */
	public AuthXML(String pathToXMLFile){
		authReader = new AuthXMLReader(pathToXMLFile);
		authWriter = new AuthXMLWriter(pathToXMLFile);
	}

	/**
	 * This method used for save Authentication for MikroTik RouterOS
	 * in XML file type.
	 * @param authObject {@link Auth}
	 * @return boolean
	 */
	public boolean save(Auth authObject){
		return this.authWriter.writeToXML(authObject);
	}

	/**
	 * This method will fetching data from XML file
	 * as an Auth Class Object
	 * @return {@link Auth}
	 */
	public Auth getAuth(){
		return this.authReader.getAuthXMLData();
	}

}
