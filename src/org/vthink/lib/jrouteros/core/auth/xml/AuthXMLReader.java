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
package org.vthink.lib.jrouteros.core.auth.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.vthink.lib.jrouteros.core.entity.Auth;
import org.w3c.dom.Document;
import java.io.File;

/**
 * This class is used to reading xml file
 * for MikroTik RouterOS.
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class AuthXMLReader {
	private File file;
	private Auth authObject;

	public AuthXMLReader(String pathToXMLFile){
		file = new File(pathToXMLFile);
		authObject = new Auth();
	}

	/**
	 * Get Data from XML File and convert it to 
	 * AuthPOJO Object
	 * @return Auth
	 */
	public Auth getAuthXMLData(){
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(this.file);
			doc.getDocumentElement().normalize();

			this.authObject.setHostname(doc.getElementsByTagName("hostname").item(0).getLastChild().getNodeValue());
			this.authObject.setPort(Integer.valueOf(doc.getElementsByTagName("port").item(0).getLastChild().getNodeValue()));
			this.authObject.setUsername(doc.getElementsByTagName("username").item(0).getLastChild().getNodeValue());
			this.authObject.setPassword(doc.getElementsByTagName("password").item(0).getLastChild().getNodeValue());
			return this.authObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



}
