package com.logansrings.booklibrary.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

public class Encrypting {

	private MessageDigest messageDigest;
	private static Encrypting instance;

	public static synchronized Encrypting getInstance() {
		if (instance == null) {
			instance = new Encrypting();
		}
		return instance;
	}

	protected Encrypting() {}

	/**
	 * Encrypts aString
	 * @param aString
	 * @return an encrypted String or null if unable to encrypt aString
	 */
	public synchronized String encrypt(String aString) {
		String encryptedString = null;
		try {
			MessageDigest messageDigest = getMessageDigest();
			messageDigest.reset();
			messageDigest.update(aString.getBytes());
			byte[] messageDigestBytes = messageDigest.digest();

			encryptedString = (getEncoder()).encode(messageDigestBytes); 
			return encryptedString;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private BASE64Encoder getEncoder() {
		return new BASE64Encoder();
	}

	protected MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
		if (messageDigest == null) {
			setMessageDigest(MessageDigest.getInstance("SHA-1"));
		}
		return messageDigest;
	}

	protected void setMessageDigest(MessageDigest messageDigest) {
		this.messageDigest = messageDigest;
	}

	public static void main(String arg[]) {
		Encrypting encrypting = Encrypting.getInstance();
		String encrypt1 = encrypting.encrypt("admin");
		String encrypt2 = encrypting.encrypt("admin");
		boolean isEqual = encrypt1.equals(encrypt2);

		encrypt1 = encrypting.encrypt("admin");
		encrypt2 = encrypting.encrypt("x");

		isEqual = encrypt1.equals(encrypt2);

		encrypt1 = encrypting.encrypt("admin");
		encrypt2 = encrypting.encrypt("password");

		isEqual = encrypt1.equals(encrypt2);

		encrypt1 = encrypting.encrypt("admin");
		encrypt2 = encrypting.encrypt("abcdefghijklmnopqrstuvwxyz");

		isEqual = encrypt1.equals(encrypt2);
	}

}