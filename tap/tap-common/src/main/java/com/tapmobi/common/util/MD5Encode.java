package com.tapmobi.common.util;

import java.security.MessageDigest;

public class MD5Encode{ 
	
	public final static String encode(String src, String key){
		String md5Str = "";
		char hexDigits[] = { 
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 
				'e', 'f'}; 
		try { 
			byte[] strTemp = (key + src).getBytes(); 
			MessageDigest mdTemp = MessageDigest.getInstance("MD5"); 
			mdTemp.update(strTemp); 
			byte[] md = mdTemp.digest(); 
			int j = md.length; 
			char str[] = new char[j * 2]; 
			int k = 0; 
			for (int i = 0; i < j; i++) { 
				byte byte0 = md[i]; 
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
				str[k++] = hexDigits[byte0 & 0xf]; 
			}
			
			StringBuffer sb = new StringBuffer("1");
            for(int i = 0; i < str.length; i ++){
            	if(str[i] >= '0' && str[i] <= '9'){//ֻȡ���ܺ�����ֲ���
            		sb.append(str[i]);
            	}
            }
            if(sb.length() > 16){
            	md5Str = sb.substring(0,16);//ȡ�˸��ַ�
            } else {
            	md5Str = sb.toString();
            }
            return md5Str;
		} 
		catch (Exception e){ 
			return null; 
		} 
	} 
	
	public final static String md5(String s, String key) {
    	char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        				'a', 'b', 'c', 'd', 'e', 'f'};
    	try {
	    	byte[] strTemp = (key + s).getBytes();
	        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	        mdTemp.update(strTemp);
	        byte[] md = mdTemp.digest();
	        int j = md.length;
	        char str[] = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	        	byte byte0 = md[i];
	        	str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	        	str[k++] = hexDigits[byte0 & 0xf];
	      	}
      		return new String(str);
    	} catch (Exception e) {
      		return null;
    	}
  	}
	
	
	public static void main(String[] args){
		int x = 30;
		x = x*x;
		
		int y = 30;
		y = y<<4;
		
		
		
		System.out.println(x);
		
		
		System.out.println(y);
	}
	
	
	public final static String md5(String s, String key, int length) {
    	char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        				'a', 'b', 'c', 'd', 'e', 'f'};
    	try {
	    	byte[] strTemp = (key + s).getBytes();
	        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	        mdTemp.update(strTemp);
	        byte[] md = mdTemp.digest();
	        int j = md.length;
	        char str[] = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	        	byte byte0 = md[i];
	        	str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	        	str[k++] = hexDigits[byte0 & 0xf];
	      	}
	        if(length > 0) {
	        	if(length > str.length) {
	        		return new String(str);
	        	} else {
	        		StringBuffer temp = new StringBuffer();
	        		for (int i = 0; i < length; i ++) {
	        			temp.append(str[i]);
	        		}
	        		return temp.toString();
	        	}
	        }
      		return new String(str);
    	} catch (Exception e) {
      		return null;
    	}    	
  	}

}