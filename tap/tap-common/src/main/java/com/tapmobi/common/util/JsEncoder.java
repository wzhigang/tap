package com.tapmobi.common.util;

public class JsEncoder {
	private static String[] specs = new String[]{"\\\\","\\/script","\\\"","\\\'","\\&"};
	public static String encoderJsStr(String jsStr){
		String result = jsStr;
		for(String spec : specs){
			System.out.println(spec);
			result = result.replaceAll(spec, "\\"+spec);
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(encoderJsStr("<script type=\"text/javascript\"> alert('cs' & 'bs') </script>"));
	}
}
