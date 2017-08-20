package com.tapmobi.common.http;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import com.tapmobi.common.http.TapHttpResponse;

public class TapMobiParser {

	public static Map<String, String> parseRequestHeader(HttpServletRequest request) {
		Map<String, String> headerMap = new LinkedHashMap<String, String>();
		headerMap.put("RemoteAddr", request.getRemoteAddr());
		headerMap.put("RequestURL", request.getRequestURL().toString());

		Enumeration<String> headernames = request.getHeaderNames();
		while(headernames.hasMoreElements()) {
			String name = headernames.nextElement();
			String value = request.getHeader(name);
			headerMap.put(name, value);
		}
		return headerMap;
	}

	/*
	 * <meta http-equiv='refresh' content='0;http://purifysecure.com/?a=1567&oc=35656&c=53935&s1=41&s2=594a3aec419a5c110a619ee8'>
	 * <meta http-equiv=\"refresh\" content=\"0;url=https://trk.tracksys55.com/click?f=a&pub_id=267&ctv_id=123101&line_item_id=52417&pub_sub=p30d2600de7f087a277f90e4a60e2efe74e&sub_pub_id=101346_68267\">
	 * <meta http-equiv=\"refresh\" content=\"1;url=https://xk4-a.tlnk.io/serve?action=click&amp;publisher_id=142536&amp;site_id=86678&amp;ref_id=cd3d6ad320a6cf0b7677efed3c481ed0b4e48f9d59566960&amp;sub_publisher=267-101346_68267&amp;sub_campaign=24573&amp;ios_ifa=&amp;mac_address=&amp;odin=&amp;my_campaign=adxperience_iOS_CA&amp;cost_model=cpi&amp;cost=3\"/>
	 */
	public static String parseRefreshUrl(String htmlContent) {
		String url = null;
		if (!StringUtils.isEmpty(htmlContent)) {
			// 转成小写
			String html = htmlContent.toLowerCase();
			// 替换\"为"
			html = html.replaceAll("\\\\\"", "\"");
			// 替换双引号为单引号
			html = html.replaceAll("\"", "'");
			// 替换双转义符号
			html = html.replaceAll("&amp;", "&");
			
			int startpos = html.indexOf("http-equiv='refresh'");
			if (startpos!=-1) {
				startpos = html.indexOf("content='", startpos);
				if (startpos!=-1) {
					int endpos = html.indexOf("'", startpos+9);
					if (endpos!=-1) {
						String substr = htmlContent.substring(startpos+9, endpos);
						String[] array = substr.split(";");
						if (array!=null) {
							for (int i=0; i<array.length; i++) {
								String str = array[i].trim();
								if (str.startsWith("url") || str.startsWith("URL")) {
									int index = str.indexOf("=");
									if (str.length()>index+1)
										str = str.substring(index+1);
								}
								if (!StringUtils.isEmpty(str))
									str = str.trim();
								if (isHttpScheme(str)) {
									url = str;
									break;
								}
							}
						}
					}
				}
			}
		}
		return url;
	}

	/*
	 * <!doctype html>\n<html lang=\"en\">\n<head>\n    <title>Redirecting...</title>\n</head>\n<body>\n<script>document.location.href = 'http://track.turbob.mobi/?aff_id=326098&offer_id=88553&aff_sub=3f7f800cd19895eb1a229382b11ac005&aff_sub2=LIUfKq36IOjhcc4H';</script>\n</body>\n</html>
	 * <html> <head> <script type="text/javascript"> window.location = 'https://click.startappservice.com/tracking/adClick?d=IAAAAAAgAAA6X1tPSERNUFZaWlBOQkZRQ1dYR01QXE1BV0JFWkFfW1ZaQkRcQ0VQSUZSXlRDWkFFVF1fWlFJS0BSRVdQQk0hLzMtSkRHSl5dViE4IysAQ0BYQEFSCwdET0kRWl0LCAQaQUQHF0dTFRYHWgIRHVNFXVIqKjUmEkVVS0RQRxUBWFVLHENGDAwPCFdJFRFSQBEHEkQEDQdfKzk3ITA6Lj0tUEZWT0ZPRVNXVl5EWkNFVFxNWFdEQEBCRkJcID0oIiATDVNAUVJeRVVaRkRcQ1dXRU1QTVJDSE0SCAMdDkZOQ0kCHRlPAwNLBxMaFh4RRw4cHwwaHxFPPBEWA1NSWV5CV0lMUF9NXFRESlpRUUFSTTlUWkBDSUhJHR0bDkY=&advId=idfa&segId=211408437&clickId=222-19104766-c4c3080f-1deb-46d8-96b3-6aaedc0ff35d&prod=55-222-876849567138582528_1373693588-440100B44FF391AC670EB590EA7F8A77'; </script> </head> <body></body> </html>
	 * <html><body><script>window.location="http://click.howdoesin.net/aff_c?offer_id=13830550&affiliate_id=3367&aid=25F1CF92-B46F-49AD-8523-E8B83F133A28&aff_sub2=c06b66b0709701a8f5be63d3efda703904&aff_sub5=10811_a198e9400924de9d"</script></body></html>
	 */
	public static String parseJSUrl(String htmlContent) {
		String url = null;
		if (!StringUtils.isEmpty(htmlContent)) {
			// 转成小写
			String html = htmlContent.toLowerCase();
			// 替换\"为"
			html = html.replaceAll("\\\\\"", "\"");
			// 替换双引号为单引号
			html = html.replaceAll("\"", "'");
			// 替换双转义符号
			html = html.replaceAll("&amp;", "&");

			int startpos = html.indexOf("<script");
			if (startpos!=-1) {
				startpos = html.indexOf("document.location.href", startpos);
				if (startpos==-1) {
					startpos = html.indexOf("window.location", startpos);	
				}
				if (startpos!=-1) {
					startpos = html.indexOf("'http", startpos);
					if (startpos!=-1) {
						int endpos = html.indexOf("'", startpos+1);
						if (endpos!=-1) {
							String substr = htmlContent.substring(startpos+1, endpos);

							if (!StringUtils.isEmpty(substr))
								substr = substr.trim();
							if (isHttpScheme(substr)) {
								url = substr;
							}
						}
					}
				}
			}
		}
		return url;
	}
	
	public static String parseOnloadUrl (String htmlContent) {
		String url = null;
		if (!StringUtils.isEmpty(htmlContent)) {
			// 转成小写
			String html = htmlContent.toLowerCase();
			// 替换\"为"
			html = html.replaceAll("\\\\\"", "\"");
			// 替换双引号为单引号
			html = html.replaceAll("\"", "'");
			// 替换双转义符号
			html = html.replaceAll("&amp;", "&");

			int startpos = html.indexOf("onload");
			if (startpos!=-1) {
				startpos = html.indexOf("document.location.href", startpos);
				if (startpos==-1) {
					startpos = html.indexOf("window.location", startpos);	
				}
				if (startpos!=-1) {
					startpos = html.indexOf("'http", startpos);
					if (startpos!=-1) {
						int endpos = html.indexOf("'", startpos+1);
						if (endpos!=-1) {
							String substr = htmlContent.substring(startpos+1, endpos);

							if (!StringUtils.isEmpty(substr))
								substr = substr.trim();
							if (isHttpScheme(substr)) {
								url = substr;
							}
						}
					}
				}
			}
		}
		return url;
	}

	public static boolean isHttpScheme(String url) {
		if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("//")|| url.startsWith("/")) {
			return true;
		}
		return false;
	}

	public static boolean isMarketScheme(String url) {
		if (url.startsWith("https://itunes.apple.com/") || url.startsWith("http://itunes.apple.com/")
				|| url.startsWith("itms-apps://itunes.apple.com") || url.startsWith("itms-appss://itunes.apple.com")
				|| url.startsWith("itmss://itunes.apple.com/") || url.startsWith("itmss://itunes.apple.com/")
				|| url.startsWith("https://play.google.com/") || url.startsWith("market://") ) {
			return true;
		}
		return false;
	}
	
	/*
	 * 判断是否302或者200的refresh或者js跳转
	 */
	public static String parseRedirect(TapHttpResponse resp) {
		String newUrl = null;
		if(resp.getStatusCode()>=300 && resp.getStatusCode()<400) {
			newUrl = resp.getResponseHeader().get("Location");
			if (newUrl==null) {
				newUrl = resp.getResponseHeader().get("location");
			}
			if (newUrl==null) {
				newUrl = resp.getResponseHeader().get("LOCATION");
			}
		} else if (resp.getStatusCode()>=200 && resp.getStatusCode()<300) {
			newUrl = parseRefreshUrl(resp.getResponseContent());
			if (StringUtils.isEmpty(newUrl)) {
				newUrl = parseJSUrl(resp.getResponseContent());
			}
			
			if (StringUtils.isEmpty(newUrl)){
				newUrl = parseOnloadUrl(resp.getResponseContent());
			}
			
			// Refresh: 0; url=http://t.mobrand.net/tracking/aff/MZXuS9QST8C4cVYqjy_Yfg/PcldWSwQT0yXaTaVbx_k6w/LCooGzZBJWlmCnMGUGQ?aff_sub=e379ec7d-4c88-49a5-a6aa-2d93ba8048c5__pspm&source=1373693588_1003&idfa=&advid=
			if (StringUtils.isEmpty(newUrl)) {
				String header = resp.getResponseHeader().get("Refresh");
				if (header!=null) {
					int start = header.indexOf("http");
					if (start!=-1) {
						newUrl = header.substring(start);
					}
				}
			}
		}
		return newUrl;
	}
	
	public static void main(String[] args) {
		//int index = url.indexOf("=");
		//System.out.println(url.substring(index+1));
		String url = "<!DOCTYPE html><html><head><meta http-equiv=\"refresh\" content=\"0; url = http://click.startappservice.com/tracking/adClick?d=IAAAAAAgAAA6X1tPSEVEW11XXlNLQUVQQ1dYR01QWk1ASEhaW11AUlJaQkRcQ0RVRURUVlZKWkFFVF1eX1JMR0RYSldQQk1UXlZEQElCUVhNWlZEQERRRk1WSEFTTVJGREREWVddWlxJQVdTRUlRXkVUXEBAQU1CSlxfVlZaQEZcQUBCREZYPi4%2FNTMVHUxaU1hJXURCR0RcQFNTW15HWkFFWFIICgkKFldWQkkCHx1LGgwZFgARDQYPRRESAxBCRUNcQF5dVkBFSE1ER15NXlZEMkVYUUVRSE0RHBYXWg%3D%3D&clickId=TRK_ZHBrdC5haWRlcG9rb3QubW9jXzQ1MTFfOTE5MV8xOC45OTEuNjEuMzAxXzA0ODQy&prod=1351&pub=1919_&segId=204754098&advId=4ae016e7-f7b5-4de5-8615-8741428494dd\"></head><body onload=\"window.location = 'http://click.startappservice.com/tracking/adClick?d=IAAAAAAgAAA6X1tPSEVEW11XXlNLQUVQQ1dYR01QWk1ASEhaW11AUlJaQkRcQ0RVRURUVlZKWkFFVF1eX1JMR0RYSldQQk1UXlZEQElCUVhNWlZEQERRRk1WSEFTTVJGREREWVddWlxJQVdTRUlRXkVUXEBAQU1CSlxfVlZaQEZcQUBCREZYPi4%2FNTMVHUxaU1hJXURCR0RcQFNTW15HWkFFWFIICgkKFldWQkkCHx1LGgwZFgARDQYPRRESAxBCRUNcQF5dVkBFSE1ER15NXlZEMkVYUUVRSE0RHBYXWg%3D%3D&clickId=TRK_ZHBrdC5haWRlcG9rb3QubW9jXzQ1MTFfOTE5MV8xOC45OTEuNjEuMzAxXzA0ODQy&prod=1351&pub=1919_&segId=204754098&advId=4ae016e7-f7b5-4de5-8615-8741428494dd'\"></body></html>\"";
		System.out.println(parseRefreshUrl(url));
		System.out.println(parseJSUrl(url));
		System.out.println(parseOnloadUrl(url));



	}

}
