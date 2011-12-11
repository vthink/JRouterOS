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

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.vthink.lib.jrouteros.core.entity.Auth;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This class used to writing MikroTik RouterOS
 * authentication in xml file.
 * 
 * @author Lalu Erfandi Maula Yusnu 
 */
public class AuthXMLWriter {
	private File file;

	public AuthXMLWriter(String pathToXMLFile){
		file = new File(pathToXMLFile);
	}


	/**
	 * Write Data from Auth object to XML File
	 * @param authObject AuthPOJO
	 * @return boolean
	 */
	public boolean writeToXML(Auth authObject){
		try{
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement("mikrotik");
			doc.appendChild(root);

			Element tagHost = doc.createElement("hostname");
			tagHost.setTextContent(authObject.getHostname());
			root.appendChild(tagHost);

			Element tagPort = doc.createElement("port");
			tagPort.setTextContent(String.valueOf(authObject.getPort()));
			root.appendChild(tagPort);

			Element tagUsername = doc.createElement("username");
			tagUsername.setTextContent(authObject.getUsername());
			root.appendChild(tagUsername);

			Element tagPassword = doc.createElement("password");
			tagPassword.setTextContent(authObject.getPassword());
			root.appendChild(tagPassword);


			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(this.file); 
			transformer.transform(source, result);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
