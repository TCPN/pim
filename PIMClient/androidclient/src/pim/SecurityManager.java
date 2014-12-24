package pim;

import java.security.MessageDigest;

public class SecurityManager {
	//MD5 for password
	//AES/SHA1 for the rest
 
	public static String md5Encoder(String str) 
	{ //For password
		String md5=null;
		
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");     
			byte[] barr=md.digest(str.getBytes());
			StringBuffer sb=new StringBuffer();
			for (int i=0; i < barr.length; i++) 
			{
				sb.append(byte2Hex(barr[i]));
			}
			String hex=sb.toString();    
			md5=hex.toUpperCase();
		}
    
		catch(Exception e) {e.printStackTrace();}
    
		return md5;
    
	}
	
	private static String byte2Hex(byte b) 
	{
		String[] h={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};  
		int i=b;    
		if (i < 0) {i += 256;}    
		return h[i/16] + h[i%16];    
	}
  
}