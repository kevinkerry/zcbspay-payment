package com.zcbspay.platform.payment.utils.des;

public class PBOCDES {

	public static void main(String[] args) {
		//测试
		
		//包含汉字的字符串
		String str= "1820130227084536000300000003003794                                                                       01|00000202|ksds|lk|20130227|20130309|499130293990007|优惠券";
		
		//纯字母数字
		//String str2 = "1234567ABFXDF222222";
		String result = Get_PBOC_DES(str.getBytes(),"0123456789ABCDEF", "0000000000000000");
		System.out.println("PBOCDES加密后："+result);
	}
	
	/*
	 *  PBOCDES加密
	 *  @param shuju:加密的数据的byte[]
	 *  @param key:密钥 16位十六进制
	 *  @param IV:初始向量，默认为0000000000000000
	 */
	public static String Get_PBOC_DES(byte[] shuju, String key, String IV)
	{
		String returntype = "";
		try
		{
	         //----------------------------------------
	         byte[] keyss = new byte[8];
	         byte[] IVS = new byte[8];
	         keyss = ConvertUtil.hexStringToByte(key);
	         IVS = ConvertUtil.hexStringToByte(IV);
	         //----------------------------------------
	         byte[] keys = keyss;
	         //数据内容字节数组
	         String slshuju = ConvertUtil.bytesToHexString(shuju);
	         int TLen = 0;
	         int DBz = 0;
	         if (slshuju.length() % 16 != 0 || slshuju.length() % 16 == 0)
	         {
	             TLen = (((int)(slshuju.length() / 16)) + 1) * 16;
	             DBz = (slshuju.length() / 16) + 1;
	             slshuju = slshuju + "8";
	             TLen = TLen - slshuju.length();
	             for (int i = 0; i < TLen; i++)
	             {
	                 slshuju = slshuju + "0";
	             }
	         }
	         byte[] Zshuju = new byte[slshuju.length() / 2];
	         Zshuju = ConvertUtil.hexStringToByte(slshuju);
	         
	         byte[] D1 = new byte[8];
	         byte[] D2 = new byte[8];
	         byte[] I2 = new byte[8];
	         byte[] I3 = new byte[8];
	         byte[] bytTemp = new byte[8];
	         byte[] bytTempX = new byte[8];
	         //初始向量
	         byte[] I0 = IVS;
	         if (DBz >= 1)
	         {
	             for (int i = 0; i < 8; i++)
	             {
	                 D1[i] = Zshuju[i];
	             }
	             for (int i = 0; i < 8; i++)
	             {
	                 bytTemp[i] = (byte)(I0[i] ^ D1[i]);
	             }
	             I2 = bytTemp;
	             bytTempX = DesECBencrypt.encryptDES(I2, keys);
	         }
	         if (DBz >= 2)
	         {
	             for (int j = 2; j <= DBz; j++)
	             {
	                 for (int i = (j - 1) * 8; i < j * 8; i++)
	                 {
	                     D2[i - (j - 1) * 8] = Zshuju[i];
	                 }
	                 for (int i = 0; i < 8; i++){
	                     bytTemp[i] =  (byte)(bytTempX[i] ^ D2[i]);
	                 }
	                 I3 = bytTemp;
	                 bytTempX = DesECBencrypt.encryptDES(I3, keys);
	             }
	         }
	         returntype = ConvertUtil.bytesToHexString(bytTempX);
	         
		}catch(Exception e)
		{
			returntype = "";
		}
		return returntype;
	}
	
}