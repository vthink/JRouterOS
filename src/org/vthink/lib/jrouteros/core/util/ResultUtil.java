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
import java.util.Iterator;
import java.util.List;
import org.vthink.lib.jrouteros.core.entity.Attribute;

/**
 * ResultUtil is a class for retrieving data
 * from MikroTik RouterOS. ResultUtil 
 * representing data with cursor {@link ResultUtil#next()} 
 * and give access to its data with {@link ResultUtil#getResult(String)} or
 * {@link ResultUtil#getResult(int)}
 * 
 * @author Lalu Erfandi Maula Yusnu
 *
 */
public class ResultUtil {
	private List<List<Attribute>> list;
	private List<Attribute> listAttr;
	private Iterator<List<Attribute>> itList;

	public ResultUtil(){
		list = new ArrayList<List<Attribute>>();
	}

	/**
	 * Retrieves the value of attribute name 
	 * in the current row of this ResultUtil object 
	 * 
	 * @param name Attribute Name in String
	 * @return Attribute value of current name in String
	 */
	public String getResult(String name){
		String value = null;
		Iterator<Attribute> it = listAttr.iterator();
		while (it.hasNext()){
			Attribute attr = it.next();
			if (attr.getName().equals(name)){
				value = attr.getValue();
				break;
			}
		}
		return value;
	}


	/**
	 * Retrieves the value of attribute name 
	 * in the current row of this ResultUtil object
	 * 
	 * @param index integer value
	 * @return Attribute value of current index
	 */
	public String getResult(int index){
		return listAttr.get(index).getValue();
	}

	/**
	 * Returns true if the iteration has more elements.
	 * (In other words, returns true if next
	 * would return an element rather than throwing an exception.)
	 * 
	 * @return true if has more element, otherwise false
	 */
	public boolean hasNext(){
		return this.itList.hasNext();
	}

	/**
	 * Moves the cursor forward one row from its current position. 
	 * A ResultUtil cursor is initially positioned before the first row;
	 * the first call to the method next makes the first row the current row;
	 * the second call makes the second row the current row, and so on.
	 * 
	 * @return true if the next row is available, otherwise false
	 */
	public boolean next(){
		if (this.hasNext()){
			this.listAttr = this.itList.next();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Initialize List and Iterator when data has been changed
	 */
	private void fireOnChange(){
		this.listAttr = list.get(0);
		this.itList = list.iterator();
	}


	/**
	 * Append specific element to the end of the list.
	 * 
	 * @param list List of Attribute
	 * @return boolean
	 */
	public boolean add(List<Attribute> list){
		boolean status = this.list.add(list);
		fireOnChange();
		return status;
	}

	/**
	 * Return the number of element in this class.
	 * 
	 * @return Integer
	 */
	public int size(){
		return this.list.size();
	}
}
