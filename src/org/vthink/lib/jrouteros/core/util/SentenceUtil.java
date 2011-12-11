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
package org.vthink.lib.jrouteros.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.vthink.lib.jrouteros.core.entity.Attribute;

/**
 * 
 * This class used for create list of command 
 * for sending to MikroTik RouterOS API
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class SentenceUtil{
	private List<Attribute> list;

	/**
	 * Constructor of Class SentenceUtil
	 * This Constructor will create new object
	 * of List<Attribute>.
	 */
	public SentenceUtil() {
		list = new ArrayList<Attribute>();
	}

	/**
	 * @param attributeName String
	 */
	public void select(String attributeName){
		String name="";
		if (attributeName.contains(",")){
			if (attributeName.contains(" ")){
				StringTokenizer st1 = new StringTokenizer(attributeName, " ");
				while (st1.hasMoreTokens()) {
					name = name + st1.nextElement();
				}
			} else {
				name = attributeName;
			}
		} else{
			name = attributeName;
		}

		this.list.add(new Attribute("select", "proplist", name));

	}

	/**
	 * This is method for creating queries in MikroTik RouterOS API. <br />
	 * 
	 * variable name is an attribute name. <br />
	 * variable operand is operator for logical, <br /> 
	 * accepted value are  -, =, <, and > <br />
	 * variable value is an attribute value.<br />
	 * 
	 * @param name String
	 * @param operand String
	 * @param value String
	 * @return boolean
	 */
	public boolean where(String name, String operand, String value){
		if (operand.equals("-")||operand.equals("=")||
				operand.equals("<")||operand.equals(">")){
			String build="";
			build= operand.trim()+ name.trim();
			list.add(new Attribute("where",build, value.trim()));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This is method for creating queries with not statement (?#!) 
	 * in MikroTik RouterOS API. <br />
	 * 
	 * variable name is an attribute name. <br />
	 * variable operand is operator for logical, <br /> 
	 * accepted value are  -, =, <, and > <br />
	 * variable value is an attribute value.<br />
	 * 
	 * @param name
	 * @param operand
	 * @param value
	 * @return boolean
	 */
	public boolean whereNot(String name, String operand, String value){
		if (operand.equals("-")||operand.equals("=")||
				operand.equals("<")||operand.equals(">")){
			String build= "?" + operand.trim()  + name.trim() + "=";
			list.add(new Attribute("whereNot", build, value.trim()));
			list.add(new Attribute("whereNot", "?#", "!"));
			return true;
		} else {
			return false;
		}
	}


	/**
	 *This is method for creating queries with 'or' (?#|) statement
	 *in MikroTik RouterOS API. <br />
	 * 
	 * variable name is an attribute name. <br />
	 * variable operand is operator for logical, <br /> 
	 * accepted value are  -, =, <, and > <br />
	 * variable value is an attribute value.<br />
	 * 
	 * @param name String
	 * @param operand String
	 * @param value String
	 * @return boolean
	 */
	public boolean orWhere(String name, String operand, String value){
		if (operand.equals("-")||operand.equals("=")||
				operand.equals("<")||operand.equals(">")){
			String build= "?" + operand.trim()  + name.trim() + "=";
			list.add(new Attribute("orWhere", build, value.trim()));
			list.add(new Attribute("orWhere", "?#", "|"));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This is method for creating queries 'or' (?#|) 'not' (?#!)
	 * in MikroTik RouterOS API. <br />
	 * 
	 * variable name is an attribute name. <br />
	 * variable operand is operator for logical, <br /> 
	 * accepted value are  -, =, <, and > <br />
	 * variable value is an attribute value.<br />
	 * 
	 * @param name String
	 * @param operand String
	 * @param value String
	 * @return boolean
	 */
	public boolean orWhereNot(String name, String operand, String value){
		if (operand.equals("-")||operand.equals("=")||
				operand.equals("<")||operand.equals(">")){
			String build= "?" + operand.trim()  + name.trim() + "=";
			list.add(new Attribute("orWhereNot", build, value.trim()));
			list.add(new Attribute("orWhereNot", "?#", "!"));
			list.add(new Attribute("orWhereNot", "?#", "|"));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This is method for creating queries with 'and' (?#&) statement
	 * in MikroTik RouterOS API. <br />
	 * 
	 * variable name is an attribute name. <br />
	 * variable operand is operator for logical, <br /> 
	 * accepted value are  -, =, <, and > <br />
	 * variable value is an attribute value.<br />
	 * 
	 * @param name String
	 * @param operand String
	 * @param value String
	 * @return boolean
	 */
	public boolean andWhere(String name, String operand, String value){
		if (operand.equals("-")||operand.equals("=")||
				operand.equals("<")||operand.equals(">")){
			String build= "?" + operand.trim()  + name.trim() + "=";
			list.add(new Attribute("andWhere", build, value.trim()));
			list.add(new Attribute("andWhere", "?#", "&"));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This is method for creating queries with 'and' (?#&) 'not' (?#!) statement
	 * in MikroTik RouterOS API. <br />
	 * 
	 * variable name is an attribute name. <br />
	 * variable operand is operator for logical, <br /> 
	 * accepted value are  -, =, <, and > <br />
	 * variable value is an attribute value.<br />
	 * 
	 * @param name String
	 * @param operand String
	 * @param value String
	 * @return boolean
	 */
	public boolean andWhereNot(String name, String operand, String value){
		if (operand.equals("-")||operand.equals("=")||
				operand.equals("<")||operand.equals(">")){
			String build= "?" + operand.trim()  + name.trim() + "=";
			list.add(new Attribute("andWhereNot", build, value.trim()));
			list.add(new Attribute("andWhereNot", "?#", "!"));
			list.add(new Attribute("andWhereNot", "?#", "&"));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method just for command that has a print 
	 * method in the end of sentence.<br />
	 * ex. /interface/print
	 * 
	 * @param command String
	 * @return boolean
	 */
	public boolean fromCommand(String command){
		return list.add(new Attribute("commandPrint","command", command));
	}

	public boolean addCommand(String command){
		return list.add(new Attribute("commandReguler","command", command));
	}


	/**
	 * Use this method for add, set, enable, and disable
	 * command to MikroTik RouterOS
	 * 
	 * @param name String
	 * @param value String
	 * @return boolean
	 */
	public boolean setAttribute(String name, String value){
		return list.add(new Attribute("setAttribute",name, value));
	}

	/**
	 * Get Build Command
	 * @return List of Attribute
	 */
	public List<Attribute> getBuildCommand(){
		return this.list;
	}

	/**
	 * Add Object of Attribute Class
	 * @param attr {@link Attribute}
	 */
	public void add(Attribute attr){
		this.list.add(attr);
	}

}
