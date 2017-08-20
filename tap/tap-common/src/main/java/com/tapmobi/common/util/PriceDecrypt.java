package com.tapmobi.common.util;

import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PriceDecrypt {

	
	private static Log log = LogFactory.getLog(PriceDecrypt.class);

	

	public static void main(String[] args) {
		String dspKey = "b0e478141a539b1d4b7ae660acd29fdc";
		String encodedPrice= "NDZCMTg4QTU2RjREQTY3NkRCNzlCNkMwOTE3NUY0MzQ0ZTMzY2E1MQ%3D%3D";
		
		log.info("decrypt Price: " + decrypt(encodedPrice,dspKey));
	}

	public static String getStr(String enc) {
		try {
			byte[] src = Base64Utils.decode(URLDecoder.decode(enc,"UTF-8"));
			String aa = new String(src);
			return aa;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ���ܼ۸�
	 * 
	 * @param enc
	 * @return
	 */
	public static String decrypt(String enc, final String dspKey) {
		
		if (enc.contains(" ")) {
			enc = enc.replace(" ", "+");
		}

		String src = getStr(enc);
		String prid = src.substring(0, 32);
		String encrypt = src.substring(32, src.length() - 4);

		int priceEnc = Integer.parseInt(encrypt, 16) ^ Integer.parseInt(MD5Encode.md5(prid + dspKey, "").substring(0, 4), 16);
		long result = 0;
		result = priceEnc;

		if (result >= 500000 || result < 0) {
			result = 0;
		}

		return String.valueOf(result);

	}

}
