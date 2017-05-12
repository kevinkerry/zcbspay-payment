package com.zcbspay.platform.payment.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.springframework.util.Base64Utils;

public class TripleDES {

	private static final String Algorithm = "TripleDES";
	SecretKey secretKey;
	Cipher ecipher;
	Cipher dcipher;

	public TripleDES(String base64key) {
		try {
			DESedeKeySpec keyspec = new DESedeKeySpec(
					Base64Utils.decode(base64key.getBytes()));
			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance(TripleDES.Algorithm);
			secretKey = keyfactory.generateSecret(keyspec);

		} catch (InvalidKeyException e2) {
			// LOG.error("InvalidKeyException:",e2);
			e2.printStackTrace();
		} catch (NoSuchAlgorithmException e2) {
			// LOG.error("NoSuchAlgorithmException:",e2);
			e2.printStackTrace();
		} catch (InvalidKeySpecException e2) {
			// LOG.error("InvalidKeySpecException:",e2);
			e2.printStackTrace();
		}
		try {
			// Create and initialize the encryption engine +"/ECB/NoPadding"
			// +"/ECB/PKCS5Padding"
			ecipher = Cipher.getInstance(secretKey.getAlgorithm());

			ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
			// Create and initialize the encryption engine
			dcipher = Cipher.getInstance(secretKey.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, secretKey);
		} catch (InvalidKeyException e) {
			// LOG.error("InvalidKeyException:",e);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// LOG.error("NoSuchAlgorithmException:",e);
		} catch (NoSuchPaddingException e) {
			// LOG.error("NoSuchPaddingException:",e);
		}

	}

	/**
	 * Takes a single String as an argument and returns an Encrypted version of
	 * that String.
	 * 
	 * @param str
	 *            String to be encrypted
	 * @return <code>String</code> Encrypted version of the provided String
	 */
	public String encrypt(String str) {
		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);
			print(enc);
			// Encode bytes to base64 to get a string
			return new sun.misc.BASE64Encoder().encode(enc);
			// return Base64.encode(enc);

		} catch (BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * Takes a encrypted String as an argument, decrypts and returns the
	 * decrypted String.
	 * 
	 * @param str
	 *            Encrypted String to be decrypted
	 * @return <code>String</code> Decrypted version of the provided String
	 */
	public String decrypt(String str) {

		try {

			// Decode base64 to get bytes
			byte[] dec = Base64Utils.decode(str.getBytes());

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);
			// print(utf8);
			// Decode using utf-8
			return new String(utf8, "UTF8");

		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void print() {
		System.out.println("Algorithm: " + secretKey.getAlgorithm());
		System.out.println("Format: " + secretKey.getFormat());

	}

	public void print(byte[] b) {
		System.out.print("{");
		for (int i = 0; i < b.length; i++)
			System.out.print(b[i] + ",");
		System.out.println("}");
	}
}
