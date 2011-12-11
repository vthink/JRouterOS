/**
 * 
 * Code is provided AS-IS and can be freely used freely. 
 * I, as a writer of code, am not responsible 
 * for anything that may arise from use of this code.
 * 
 */
package org.vthink.lib.jrouteros.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
 * This class used for sending data to MikroTik RouterOS
 * @author janisk
 */
public class StreamSender {
	private DataOutputStream out = null;


	/**
	 * This Constructor of Class {@link StreamSender} 
	 * @param out {@link DataOutputStream}
	 */
	StreamSender(DataOutputStream out) {
		this.out = out;
	}

	/**
	 * This method will returned length of command  sentence that
	 * will used as a standard protocol in MiroTik RouterOS
	 * 
	 * @param command String
	 * @return Integer
	 */
	public Integer protocolLengthEncoder(String command){
		Integer length = null;
		if (command.length() < 0x80) {
			length = command.length();
		} else if (command.length() < 0x4000) {
			length = Integer.reverseBytes(command.length() | 0x8000);
		} else if (command.length() < 0x200000) {
			length = Integer.reverseBytes(command.length() | 0xC00000);
		} else if (command.length() < 0x10000000) {
			length = Integer.reverseBytes(command.length() | 0xE0000000);
		} else {
			length = Integer.reverseBytes(command.length());	
		}
		return length;
	}

	/**
	 * This method will returned byte array of command sentence that
	 * will used as a standard protocol in MiroTik RouterOS
	 * @param command String
	 * @return byte[]
	 */
	public byte[] protocolWordEncoder(String command){
		Integer lengthEncoder = protocolLengthEncoder(command);
		StringBuilder  sb = new StringBuilder();
		String s = Integer.toHexString(lengthEncoder);
		if (s.length() < 2) {
			return new byte[]{lengthEncoder.byteValue()};
		} else {
			for (int j = 0; j < s.length(); j += 2) {
				char dec= (char) Integer.parseInt(s.substring(j, j + 2), 16);
				if (dec!=0){
					sb.append(dec);
				} else {
					sb.append("");
				}
			}
		}
		return sb.toString().getBytes();
	}


	/**
	 * This method will returned concatenation of 
	 * {@link StreamSender#protocolLengthEncoder(String)} and
	 * {@link StreamSender#protocolWordEncoder(String)} 
	 * that will send to MikroTik RouterOS
	 * 
	 * @param command String
	 * @return byte[]
	 */
	public byte[] protocolStreamEncoder(String command){
		byte[] sentence = null;
		if (!command.contains("\n")) {

			byte[] word = protocolWordEncoder(command);
			sentence = new byte[(word.length + command.length() + 1)];

			int i = 0;
			for (i = 0; i < word.length; i++) {
				sentence[i] = word[i];
			}

			try {
				for (byte c : command.getBytes("US-ASCII")) {
					sentence[i++] = c;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			String[] cmd = command.split("\n");

			//get command length
			int[] wordLine = new int[cmd.length];
			for (int a = 0; a < cmd.length; a++) {
				wordLine[a] = protocolWordEncoder(cmd[a]).length + cmd[a].length();
			}

			int length = 1;
			for (int b : wordLine) {
				length += b;
			}
			//end get command length

			sentence = new byte[length];
			int counter = 0;
			for (int a = 0; a < cmd.length; a++) {

				byte[] word = protocolWordEncoder(cmd[a]);
				for (int j = 0; j < word.length; j++) {
					sentence[counter++] = word[j];
				}

				try {
					for (byte c : cmd[a].getBytes("US-ASCII")) {
						sentence[counter++] = c;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return sentence;
	}


	/**
	 * 
	 * This method used for sending data to MikroTik RouterOS
	 * If sending data is successful it will return true
	 * otherwise it will return false
	 * @param command String
	 * @return boolean
	 */
	public boolean sendStream(String command) {
		try {
			out.write(this.protocolStreamEncoder(command));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}