/**
 * 
 * Code is provided AS-IS and can be freely used freely. 
 * I, as a writer of code, am not responsible 
 * for anything that may arise from use of this code.
 * 
 */
package org.vthink.lib.jrouteros.core.talker;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vthink.lib.jrouteros.core.Connector;
import org.vthink.lib.jrouteros.core.entity.Attribute;
import org.vthink.lib.jrouteros.core.util.ResultUtil;

/**
 * Change Log :
 *  	1.0 janisk
 *  		First Java  Class released from author
 *  
 * 		1.1 Lalu Erfandi Maula Yusnu
 * 			Creating break down for some of method to adapt with JRouterOS.
 * 			Added method : 	isTrap(), isDone(), isData(), getResult(), 
 * 							isDebug(), setDebug(), runDebugger() and parseRawToList()
 * 
 * Contributor:
 * 		Lalu Erfandi Maula Yusnu <nunenuh (at) gmail.com>
 * 
 * 
 */

/**
 * This class is used for retrieving data from MikroTik RouterOS.
 * 
 * @author janisk
 */
public class TalkerReceiver extends Thread {
	private Connector con ;
	private ResultUtil result;
	private boolean trap = false;
	private boolean done = false;
	private boolean re	 = false;
	private boolean debug = false;

	/**
	 * @param con {@link Connector}
	 */
	public TalkerReceiver(Connector con) {
		this.con = con;
		result = new ResultUtil();
		this.setName("Talker Reciever");
		this.setDaemon(true);
	}

	/**
	 * Do data receive from MikroTik RouterOS
	 */
	public void doReceiving(){
		this.run();
	}

	/**
	 * Get response status from Mikrotik RouterOS <br />
	 * if there is !trap the response status is true <br />
	 * if there is !re the response status is false 
	 * @return boolean
	 */
	public boolean isTrap(){
		return this.trap;
	}


	/**
	 * Get response status from Mikrotik RouterOS <br />
	 * if there is !done the response status is true <br />
	 * otherwise it will return false
	 * @return boolean
	 */
	public boolean isDone(){
		return this.done;
	}

	/**
	 * Get response status from Mikrotik RouterOS <br />
	 * if there is !re the response status is true <br />
	 * otherwise it will return false
	 * @return boolean
	 */
	public boolean isData(){
		return this.re;
	}

	/**
	 * parse raw string that returned from Mikrotik RouterOS
	 * to QueryObjectRecord List
	 * @param rawString String
	 */
	private void parseRawToList(String rawString) {
		if (!rawString.trim().isEmpty()){
			List<Attribute> list = new ArrayList<Attribute>();
			StringTokenizer st1 = new StringTokenizer(rawString, "\n");
			while (st1.hasMoreTokens()){
				Attribute qob = new Attribute();
				String line = st1.nextToken();
				if (!line.equals("!re")&&!line.equals("!trap")){
					String[] tmp =line.split("=");
					qob.setName(tmp[1]);
					if (tmp.length==3){
						qob.setValue(tmp[2]);
					} else {
						qob.setValue(null);
					}
					list.add(qob);
				}
			}
			if (!list.isEmpty()) result.add(list);
		}
	}


	/**
	 * Get returned data from Mikrotik RouterOS
	 * @return List
	 */
	public ResultUtil getResult() {
		//wait until this class finished get data from Mikrotik RouterOS
		if (this.isAlive()){
			try {
				sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(TalkerReceiver.class.getName()).log(Level.SEVERE, null, ex);
			}
		} 
		return this.result;
	}

	
	/**
	 * Run Debugger output with some formated Information
	 * @param debugString String
	 */
	private void runDebugger(String debugString){
		if (isDebug()){
			System.out.println(debugString);
		}
	}
	
	/**
	 * Check if debugging mode is on
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Set debugging to see what data is receive from MikroTik RouterOS
	 * @param on boolean
	 */
	public void setDebug(boolean on) {
		this.debug = on;
	}


	@Override
	public void run() {
		String s = "";
		while (true) {
			s = this.con.recieve();
			if (s != null) {
				if (s.contains("!re")){
					this.parseRawToList(s);
					this.runDebugger(s);
					this.re = true;
				}

				if (s.contains("!trap")){
					this.trap = true;
					this.runDebugger(s);
					break;
				}

				if (s.contains("!done")) {
					this.done = true;
					this.runDebugger(s);
					break;
				}
			}
		}
	}
}