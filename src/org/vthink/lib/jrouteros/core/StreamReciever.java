/**
 * 
 * Code is provided AS-IS and can be freely used freely. 
 * I, as a writer of code, am not responsible 
 * for anything that may arise from use of this code.
 * 
 */
	
package org.vthink.lib.jrouteros.core;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * Change Log :
 *  	1.0 janisk
 *  		First Java  Class released from author
 *  
 * 		1.1 Lalu Erfandi Maula Yusnu
 * 			Creating break down for some of method to adapt with JRouterOS.
 * 
 * Contributor:
 * 		Lalu Erfandi Maula Yusnu <nunenuh (at) gmail.com>
 * 
 * 
 */

/**
 * This class used for retrieving data from MikroTik RouterOS
 *
 * @author janisk
 */
public class StreamReciever implements Runnable {

	private DataInputStream in = null;
	@SuppressWarnings("rawtypes")
	LinkedBlockingQueue queue = null;


	/**
	 * This Constructor of StreamReciever Class
	 * 
	 * @param in {@link DataInputStream}
	 * @param queue {@link LinkedBlockingQueue}
	 */
	@SuppressWarnings("rawtypes")
	public StreamReciever(DataInputStream in, LinkedBlockingQueue queue) {
		this.in = in;
		this.queue = queue;
	}

	/**
	 * This method used for decode standard MikroTik RouterOS data
	 * @param a Integer
	 * @return Integer
	 */
	public int protocolLengthDecoder(int a){
		int sk=0;
		if (a < 0x80) {
			sk = a;
		} else if (a < 0xC0) {
			a = a << 8;
			a += readStream();
			sk = a ^ 0x8000;
		} else if (a < 0xE0) {
			for (int i = 0; i < 2; i++) {
				a = a << 8;
				a += readStream();
			}
			sk = a ^ 0xC00000;
		} else if (a < 0xF0) {
			for (int i = 0; i < 3; i++) {
				a = a << 8;
				a += readStream();
			}
			sk = a ^ 0xE0000000;
		} else if (a < 0xF8) {
			a = 0;
			for (int i = 0; i < 5; i++) {
				a = a << 8;
				a += readStream();
			}
		}
		return sk;
	}


	/**
	 * Read length of data from MikroTik RouterOS
	 * 
	 * @return Integer
	 */
	public int readStream(){
		int out = 0;
		try {
			out = in.read();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return out;
	}

	/**
	 * Reads up to len bytes of data from the MikroTik RouterOS
	 * @param word byte[]
	 * @param lengthDecoder Integer
	 * @return Integer
	 */
	private int readStream(byte[] word, int lengthDecoder){
		int length;
		try {
			length = in.read(word, 0, lengthDecoder);
		} catch (IOException ex) {
			length = 0;
			ex.printStackTrace();
		}
		return length;
	}

	/**
	 * Build Stream into {@link LinkedBlockingQueue} for
	 * synchronizing String from MikroTik RouterOS
	 * with {@link Connector} class
	 * @param raw
	 */
	@SuppressWarnings("unchecked")
	public void buildStreamReader(String raw){
		try {
			queue.put(raw);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String s 	= "";
		int length 	= 0;
		while (true) {
			length = readStream();
			if (length != 0 && length > 0) {
				int lengthDecoder 	= protocolLengthDecoder(length);
				byte[] word 		= new byte[lengthDecoder];
				length = readStream(word, lengthDecoder);
				s += "\n" + new String(word);
			} else {
				this.buildStreamReader(s);
				s = "";
			}
		}
	}
}
