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
package org.vthink.lib.jrouteros.core.talker;

import org.vthink.lib.jrouteros.core.Connector;
import org.vthink.lib.jrouteros.core.util.ResultUtil;
import org.vthink.lib.jrouteros.core.util.SentenceUtil;

/**
 * This class used for connecting, sending command,
 * and retrieve data from Mikrotik RouterOS
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class Talker extends Connector{
	private boolean debug=false;
	private TalkerReceiver talkerReceiver;
	private TalkerSender talkerSender;

	/**
	 * Constructor for Talker Class 
	 * The parameter is required for connecting to Mikrotik RouterOS
	 * 
	 * @param address String
	 * @param port int
	 * @param username String
	 * @param password String
	 */
	public Talker(String address, int port, String username, String password) {
		super(address, port, username, password.toCharArray());
		this.talkerSender		= new TalkerSender(this);
		this.talkerReceiver		= new TalkerReceiver(this); 
	}

	/**
	 * Check if authentication successful or failed
	 * @return boolean
	 */
	public boolean isLogin(){
		return super.isLogin();
	}

	/**
	 * Sending command to Mikrotik RouterOS
	 * @param sentence SentenceUtil
	 */
	public void send(SentenceUtil sentence){
		this.talkerSender.send(sentence);

		if (isDebug()){
			System.out.println();
			System.out.println(
					"=====================" +
							" Recieving Debug " +
					"===================");
		}

		this.talkerReceiver.doReceiving();

	}

	/**
	 * 
	 * Get Result of CommandUtil from MikroTik RouterOS
	 * @return List of CommandUtil
	 */
	public ResultUtil getResult(){
		return talkerReceiver.getResult();
	}


	/**
	 * Get response status from MikroTik RouterOS <br />
	 * if there is !trap the response status is true <br />
	 * if there is !re the response status is false 
	 * @return boolean
	 */
	public boolean isTrap(){
		return this.talkerReceiver.isTrap();
	}


	/**
	 * Get response status from Mikrotik RouterOS <br />
	 * if there is !done the response status is true <br />
	 * otherwise it will return false
	 * @return boolean
	 */
	public boolean isDone(){
		return this.talkerReceiver.isDone();
	}

	/**
	 * Get response status from Mikrotik RouterOS <br />
	 * if there is !re the response status is true <br />
	 * otherwise it will return false
	 * @return boolean
	 */
	public boolean isData(){
		return this.talkerReceiver.isData();
	}

	/**
	 * Check if debugging mode is on
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Set debugging to see what data is send to MikroTik RouterOS
	 * and what data is receive from MikroTik RouterOS
	 * @param on boolean
	 */
	public void setDebug(boolean on) {
		this.debug = on;
		this.talkerSender.setDebug(on);
		this.talkerReceiver.setDebug(on);
	}
}
