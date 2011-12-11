/**
 * 
 * Code is provided AS-IS and can be freely used freely. 
 * I, as a writer of code, am not responsible 
 * for anything that may arise from use of this code.
 * 
 */

package org.vthink.lib.jrouteros.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vthink.lib.jrouteros.core.talker.Talker;
import org.vthink.lib.jrouteros.core.util.ConverterUtil;

/**
 * Change Log :
 *  	1.0 janisk
 *  		First Java  Class released from author
 *  
 * 		1.1 Lalu Erfandi Maula Yusnu
 * 			Creating break down for some of method to adapt with JRouterOS.
 * 			Added method : isLogin(), isReachable(), and challanger()
 * 
 * Contributor:
 * 		Lalu Erfandi Maula Yusnu <nunenuh (at) gmail.com>
 * 
 * 
 */

/**
 * 
 * This contains connection. Everything should be here,
 * should operate with this class only
 * 
 * @author janisk
 */
public class Connector extends Thread {
	
	private Socket socket;
	private DataOutputStream outSock;
	private DataInputStream inSock;
	private StreamReciever streamReader;
	private StreamSender streamSender;
	private Thread listenerReciever;

	private String address;
	private int port;
	private String username;
	private char[] password;

	private boolean connected = false;
	private boolean login  	  = false;

	@SuppressWarnings("rawtypes")
	LinkedBlockingQueue linkedRecieve = new LinkedBlockingQueue(40);

	/**
	 * This is constructor of class Connector, 
	 * this class is used for connecting to MikroTik RouterOS.
	 * To use this class you must provide parameter.
	 * 
	 * @param address String
	 * @param port int
	 * @param username String
	 * @param password char[]
	 */
	public Connector(String address, int port, String username, char[] password) {
		this.address 	= address;
		this.port 		= port;
		this.username 	= username;
		this.password 	= password;
		this.setName("settings");
		this.doConnect();
	}

	/**
	 * Check if connection to MikroTik RouterOS has been established.
	 * 
	 * @return boolean
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Check if username and password has been accepted by MikroTik RouterOS.
	 * if failed it will return false otherwise it will return true
	 * 
	 * @return boolean
	 */
	public boolean isLogin(){
		return this.login;
	}

	/**
	 * This method will create listener for 
	 * receiving data from MikroTik RouterOS.
	 */
	private void listenRecieve() {
		if (this.isConnected()) {
			if (streamReader == null) {
				streamReader = new StreamReciever(inSock, linkedRecieve);
			}
			listenerReciever = new Thread(streamReader);
			listenerReciever.setDaemon(true);
			listenerReciever.setName("Receiver Listener");
			listenerReciever.start();
		}
	}



	/**
	 * Use this method for sending command to MikroTik RouterOS
	 * 
	 * @param sentence String
	 * @return boolean
	 */
	public boolean send(String sentence) {
		return streamSender.sendStream(sentence);

	}

	/**
	 * Use this method for receiving data from MikroTik RouterOS
	 * 
	 * @return String
	 */
	public String recieve() {
		String s = null;
		try {
			s = (String) linkedRecieve.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Do a connect and login to MikroTik RouterOS
	 */
	private void doConnect(){
		if (!this.isConnected()) {
			this.start();
			try {
				this.join();
				if (this.isReachable()) {
					this.login = this.connect();
				} 
			} catch (InterruptedException ex) {
				Logger.getLogger(Talker.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}


	/**
	 * If authentication to MikroTik RouterOS
	 * granted it will return true 
	 * otherwise it will return false
	 * 
	 * @return boolean
	 */
	private boolean connect() {
		this.send("/login");
		String res = this.recieve();

		if (!res.contains("!trap") && res.length() > 4) {
			String[] word = res.trim().split("\n");
			if (word.length > 1) {
				word = word[1].split("=ret=");
				String challange = word[word.length - 1];
				String challanger = this.challanger(this.username, this.password, challange);
				this.send(challanger);
				String resChallange = this.recieve();
				if (resChallange.contains("!done") && !resChallange.contains("!trap")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method is used for send response command in
	 * {@link Connector#connect()}
	 * 
	 * @param username String
	 * @param password char[]
	 * @param challange String
	 * @return String
	 */
	private String challanger(String username, char[] password, String challange){
		String prepare 				= ConverterUtil.hexToString("00") + new String(password) + ConverterUtil.hexToString(challange);
		String encryptPrepare 		= ConverterUtil.MD5(prepare);
		String responseChallange	= "/login\n=name=" + username + "\n=response=00" + encryptPrepare;
		return responseChallange;
	}

	/**
	 * Initialize Connection Property for accessing MikroTik RouterOS
	 */
	private void initStream(){
		try {
			socket 		 = new Socket(address, port);
			inSock 		 = new DataInputStream(socket.getInputStream());
			outSock 	 = new DataOutputStream(socket.getOutputStream());
			streamReader = new StreamReciever(inSock, linkedRecieve);
			streamSender = new StreamSender(outSock);
			this.listenRecieve();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * If address that has been specify in Constructor
	 * is reachable it will return true otherwise it will
	 * return false
	 * 
	 * @return boolean
	 */
	public boolean isReachable(){
		try {
			InetAddress ia = InetAddress.getByName(address);
			if (ia.isReachable(2000)) {
				return true;
			} else {
				return false;
			}
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		int attempt=1;
		while (!this.isConnected()&&attempt<=4){
			if (this.isReachable()) {
				this.connected 	= true;
				this.initStream();
			} else {
				this.connected 	= false;
			}
			attempt++;
		}
	}

}