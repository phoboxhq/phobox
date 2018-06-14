package de.milchreis.phobox.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Helper {
	
	/**
	 * Returns a MD5 hash for a given input string. (hex encoded)
	 * 
	 * @param input		expects an input string 
	 * @return			hex encoded md5 string of the input
	 */
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
