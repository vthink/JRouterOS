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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This used for converting or encrypt
 * that suitable for MikroTik RouterOS
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class ConverterUtil {

	/**
	 * This nethod is used for encrypt String to MD5
	 * @param text String
	 * @return String
	 */
	public static String MD5(String text){
		String output 		= null;
		try {
			MessageDigest dg = MessageDigest.getInstance("MD5");
			dg.reset();
			dg.update(stringToBytes(text));
			byte[] hasher 	= new byte[32];
			hasher 			= dg.digest();
			return bytesToHexString(hasher);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("There is No MessageDigest Instance of MD5");
			System.exit(1);
		} 
		return output;
	}

	/**
	 * This method used for converting byte array to hex string
	 * 
	 * @param hasher byte[]
	 * @return String
	 */
	public static String bytesToHexString(byte[] hasher){
		String HEXES 		=  "0123456789abcdef";
		StringBuilder hex 	=  new StringBuilder( 2 * hasher.length );
		for ( final byte b : hasher ) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4))
			.append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	/**
	 * This method used for converting byte array to string
	 * @param hasher byte[]
	 * @return String
	 */
	public static String bytesToString(byte[] hasher){
		String hex = bytesToHexString(hasher);
		return hexToString(hex);
	}

	/**
	 * This method used for converting string to bytes
	 * @param str String
	 * @return byte[]
	 */
	public static byte[] stringToBytes(String str){
		int len = str.length();
		byte[] out = new byte[len];
		for (int i=0; i<len; i++) out[i] = (byte) ( str.charAt(i) & 0xFF);
		return out;
	}


	/**
	 * This method used for converting hex to string
	 * @param hexString String
	 * @return String
	 */
	public static String hexToString(String hexString){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<hexString.length()-1; i+=2 ){
			int decimal = Integer.parseInt(hexString.substring(i, (i + 2)), 16);
			sb.append((char)decimal);
		}
		return sb.toString();
	}

	/**
	 * This method used for converting integer to byte array
	 * @param value Integer
	 * @return byte[]
	 */
	public static byte[] intToByteArray(int value) {
		return new byte[] {
				(byte)(value >>> 24),
				(byte)(value >>> 16),
				(byte)(value >>> 8),
				(byte)value};
	}

	/**
	 * This method used for converting byte array into integer
	 * @param bArray byte[]
	 * @return integer
	 */
	public static int byteArrayToInt(byte [] bArray) {
		return (bArray[0] << 24)
				+ ((bArray[1] & 0xFF) << 16)
				+ ((bArray[2] & 0xFF) << 8);
	}


}
