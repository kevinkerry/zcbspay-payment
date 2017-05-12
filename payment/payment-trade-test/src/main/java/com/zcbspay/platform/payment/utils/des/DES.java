package com.zcbspay.platform.payment.utils.des;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES{
	
	public static void main(String[] args) throws Exception {
	   //DES加密 16进制字符串
	  ENCRYPTMethod("5C656FF73140F705","1111111111111111");
	  
	  //DES解密---使用ECB模式
	  test2("A104E4BA4E121B4E","1111111111111111","DES/ECB/NoPadding");
	 }
	 
	 
	 /**
	  * DES加密
	  * @param HexString  字符串（16位16进制字符串）
	  * @param keyStr     密钥16个1
	  * @throws Exception
	  */
	 public static void ENCRYPTMethod(String HexString,String keyStr) throws Exception{ 
	        try {
	           byte[] theKey = null;
	           byte[] theMsg = null;
	           theMsg = hexToBytes(HexString);
	           theKey = hexToBytes(keyStr);
	           KeySpec ks = new DESKeySpec(theKey);
	           SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
	           SecretKey ky = kf.generateSecret(ks);
	           Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
	           cf.init(Cipher.ENCRYPT_MODE,ky);
	           byte[] theCph = cf.doFinal(theMsg);
	           System.out.println("*************DES加密****************");
	           System.out.println("密钥    : "+bytesToHex(theKey));
	           System.out.println("字符串: "+bytesToHex(theMsg));
	           System.out.println("加密后: "+bytesToHex(theCph));
	        } catch (Exception e) {
	           e.printStackTrace();
	           return;
	        }
	     }
	 
		/**
		 * DES解密
		 * 
		 * @param hexStr  16位十六进制字符串
		 * @param keyStr  密钥16个1
		 * @param modeStr 解密模式:ECB
		 * @throws Exception
		 */
		 public static void test2(String hexStr,String keyStr,String modeStr) throws Exception{
			 	
		        String algorithm = modeStr;
		        try {
		           byte[] theKey = null;
		           byte[] theMsg = null;
		           theMsg = hexToBytes(hexStr);
		           theKey = hexToBytes(keyStr);
		           KeySpec ks = new DESKeySpec(theKey);
		           SecretKeyFactory kf
		              = SecretKeyFactory.getInstance("DES");
		           SecretKey ky = kf.generateSecret(ks);
		           Cipher cf = Cipher.getInstance(algorithm);
		           cf.init(Cipher.DECRYPT_MODE,ky);
		           byte[] theCph = cf.doFinal(theMsg);
		           System.out.println("*************DES解密****************");
		           System.out.println("密钥    : "+bytesToHex(theKey));
		           System.out.println("字符串: "+bytesToHex(theMsg));
		           System.out.println("解密后: "+bytesToHex(theCph));
		           
		        } catch (Exception e) {
		           e.printStackTrace();
		           return;
		        }
		 
		 }
	 
	 public static byte[] hexToBytes(String str) {
	       if (str==null) {
	          return null;
	       } else if (str.length() < 2) {
	          return null;
	       } else {
	          int len = str.length() / 2;
	          byte[] buffer = new byte[len];
	          for (int i=0; i<len; i++) {
	              buffer[i] = (byte) Integer.parseInt(
	                 str.substring(i*2,i*2+2),16);
	          }
	          return buffer;
	       }

	    }
	    public static String bytesToHex(byte[] data) {
	      if (data==null) {
	         return null;
	      } else {
	         int len = data.length;
	          String str = "";
	       for (int i=0; i<len; i++) {
	        if ((data[i]&0xFF)<16) str = str + "0"
	                  + java.lang.Integer.toHexString(data[i]&0xFF);
	        else str = str
	                  + java.lang.Integer.toHexString(data[i]&0xFF);
	    }
	       return str.toUpperCase();
	     }
	     }  
}
