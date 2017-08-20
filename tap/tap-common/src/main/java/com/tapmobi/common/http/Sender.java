package com.tapmobi.common.http;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tapmobi.common.util.GsonUtil;
import com.tapmobi.common.http.TapHttpRequest;
import com.tapmobi.common.http.TapHttpResponse;
import com.tapmobi.common.http.TapProxyConfig;
//import com.tapmobi.click.ClickUtil;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class Sender {
	private static Logger log = LoggerFactory.getLogger(Sender.class);
	
	@Deprecated
	public static TapHttpResponse doGet(TapHttpRequest req) {
		return TapMobiBrowser.doGet(req);
	}

	/*
	 * 此方法为递归调用自己，挨个请求每个url，直到下一跳为非http请求或者应用市场的url
	 * 如果到达应用市场则设置"to_market":"true"
	 */
	public static List getNavs(TapHttpRequest req, List respList) {
		req.setRequestMethod("get");
		long startTime = System.currentTimeMillis();

		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("{"), "%7B"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("}"), "%7D"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("["), "%5B"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("]"), "%5D"));
//		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("%"), "%25"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("|"), "%7C"));
		
		/*
		 * http://advert.smarter-wireless.net/index.php?
		 * 有时返回302到如下地址
		 * http://52.77.99.53/acs.php?sid=4406&adid=7411361&os=1&osv=4.4&udid=network&gaid=&pb=1dcb206ba02eb43835b685d46c219db0%7C12-1499959080%7Cnetwork
		 * 他把参数拼错了，导致提示版本过低，手工把&osv=4替换为&osv=9
		 */
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("&osv=4"), "&osv=9"));
		
		TapHttpResponse resp = TapMobiBrowser.doGet(req);
		long timeCost = System.currentTimeMillis() - startTime;

		if (resp!=null) {
			Map resultMap = new LinkedHashMap();
			resultMap.put("uri", req.getRequestURL());
			resultMap.put("status_code", String.valueOf(resp.getStatusCode()));
			resultMap.put("time_cost", String.valueOf(timeCost));
			resultMap.put("resp_header", resp.getResponseHeader());
			if (resp.getStatusCode()>=200 && resp.getStatusCode()<300) {
				resultMap.put("resp_content", resp.getResponseContent());
			}

			// 判断是否有下一跳
			String newUrl = TapMobiParser.parseRedirect(resp);
			if (!StringUtils.isEmpty(newUrl)) {
				// 下一跳不是应用市场的地址，则继续跳转
				if (!TapMobiParser.isMarketScheme(newUrl)) {
					if (TapMobiParser.isHttpScheme(newUrl)) {
						int pos = req.getRequestURL().indexOf("/",15);
						String domain = "";
						if (pos > -1){
							domain = req.getRequestURL().substring(0,pos);
						}

						if (newUrl.startsWith("/")){
							//int pos = req.getRequestURL().indexOf("/",15);
							if (pos > -1){
								//String domain = req.getRequestURL().substring(0,pos);
								//log.info("domain={}",domain);
								newUrl = domain+newUrl;
							}
						}

						// 域名不变则设置cookie
						try {
							if (newUrl.startsWith(domain)){
								List<String> cookieList = resp.getCookies();
								String cookieStr = "";
								for (String cookie : cookieList) {
									cookieStr += cookie.split(";")[0] + "; ";
								}
								if (!StringUtils.isEmpty(cookieStr)) {
									cookieStr = cookieStr.substring(0, cookieStr.length()-2);
									req.getRequestHeader().put("Cookie", cookieStr);
								}
							} else {
								req.setRequestHeader(new HashMap<String, String>());
							}
						} catch (Exception ce) {}

						respList.add(resultMap);
						
						if (resp.getStatusCode()>=200 && resp.getStatusCode()<300) {
							req.getRequestHeader().put("Referer", req.getRequestURL());
						}

						req.setRequestURL(newUrl);
						getNavs(req, respList);
					} else {
						respList.add(resultMap);
					}
				} else {
					resultMap.put("to_market", "true");
					respList.add(resultMap);
				}
			} else {
				respList.add(resultMap);
			}
		}
		return respList;
	}
	
	
	public static String getNavsNew(TapHttpRequest req, List respList) {
		req.setRequestMethod("get");
		long startTime = System.currentTimeMillis();

		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("{"), "%7B"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("}"), "%7D"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("["), "%5B"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("]"), "%5D"));
//		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("%"), "%25"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("|"), "%7C"));
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote(" "), "%20"));
		
		/*
		 * 递归调用出现异常，导致服务终止，这里尝试使用新的方法。
		 */
		req.setRequestURL(req.getRequestURL().replaceAll(Pattern.quote("&osv=4"), "&osv=9"));
		
		TapHttpResponse resp = TapMobiBrowser.doGet(req);
		long timeCost = System.currentTimeMillis() - startTime;

		if (resp!=null) {
			Map resultMap = new LinkedHashMap();
			resultMap.put("uri", req.getRequestURL());
			resultMap.put("status_code", String.valueOf(resp.getStatusCode()));
			resultMap.put("time_cost", String.valueOf(timeCost));
			resultMap.put("resp_header", resp.getResponseHeader());
			if (resp.getStatusCode()>=200 && resp.getStatusCode()<300) {
				resultMap.put("resp_content", resp.getResponseContent());
			}

			// 判断是否有下一跳
			String newUrl = TapMobiParser.parseRedirect(resp);
			if (!StringUtils.isEmpty(newUrl)) {
				// 下一跳不是应用市场的地址，则继续跳转
				if (!TapMobiParser.isMarketScheme(newUrl)) {
					if (TapMobiParser.isHttpScheme(newUrl)) {
						int pos = req.getRequestURL().indexOf("/",15);
						String domain = "";
						if (pos > -1){
							domain = req.getRequestURL().substring(0,pos);
						}

						if (newUrl.startsWith("/")){
							//int pos = req.getRequestURL().indexOf("/",15);
							if (pos > -1){
								//String domain = req.getRequestURL().substring(0,pos);
								//log.info("domain={}",domain);
								newUrl = domain+newUrl;
							}
						}

						// 域名不变则设置cookie
						try {
							if (newUrl.startsWith(domain)){
								List<String> cookieList = resp.getCookies();
								String cookieStr = "";
								for (String cookie : cookieList) {
									cookieStr += cookie.split(";")[0] + "; ";
								}
								if (!StringUtils.isEmpty(cookieStr)) {
									cookieStr = cookieStr.substring(0, cookieStr.length()-2);
									req.getRequestHeader().put("Cookie", cookieStr);
								}
							} else {
								req.setRequestHeader(new HashMap<String, String>());
							}
						} catch (Exception ce) {}

						respList.add(resultMap);
						
						if (resp.getStatusCode()>=200 && resp.getStatusCode()<300) {
							req.getRequestHeader().put("Referer", req.getRequestURL());
						}
						return newUrl;
						//req.setRequestURL(newUrl);
						//getNavs(req, respList);
					} else {
						respList.add(resultMap);
					}
				} else {
					resultMap.put("to_market", "true");
					respList.add(resultMap);
				}
			} else {
				respList.add(resultMap);
			}
		}
		return null;
	}

	@Deprecated
	public static List getNavs(String requestURL, List respList) {
		TapHttpRequest req = new 	TapHttpRequest();
		req.setRequestMethod("get");
		req.setRequestURL(requestURL);
		long startTime = System.currentTimeMillis();
		TapHttpResponse resp = doGet(req);
		long timeCost = System.currentTimeMillis() - startTime;

		if (resp!=null) {
			Map resultMap = new LinkedHashMap();
			resultMap.put("uri", req.getRequestURL());
			resultMap.put("status_code", resp.getStatusCode());
			resultMap.put("time_cost", String.valueOf(timeCost));
			resultMap.put("resp_header", resp.getResponseHeader());
			respList.add(resultMap);

			if(resp.getStatusCode()==301 || resp.getStatusCode()==302) {
				getNavs(resp.getResponseHeader().get("Location"), respList);
			}
		}
		return respList;
	}

	/**
	 * 检测一个代理服务器的响应时间
	 * @param host_port 127.0.0.1:8123:user:pass
	 * @return 检测耗时秒数
	 */
	public static Long checkProxyTime(String host, int port, String user, String pass) {
		try {
			TapProxyConfig proxyConfig = new TapProxyConfig("http", host, port, user, pass);

			TapHttpRequest req = new TapHttpRequest();
			req.setRequestURL("http://oneid.mmstat.com/taobao/getDeviceInfo");
			req.setProxyConfig(proxyConfig);
			req.setUserName(user);
			req.setPassword(pass);

			long startTime = System.currentTimeMillis();
			TapHttpResponse resp = TapMobiBrowser.doGet(req);
			long timeCost = System.currentTimeMillis()-startTime;
			long timeMS = timeCost/1000 + 1;						
			log.debug(GsonUtil.getJsonWithFormat(resp));

			// 响应时间小于10秒且返回指定内容才算成功
			
			/*
request:http://haitao.toc0.com/j
response:200
-1
			 */
			// 
//			if (timeCost<10000 && resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
//				String respContent =  resp.getResponseContent();
//				respContent = respContent.replaceAll(System.getProperty("line.separator"), "");
//				if ("﻿-1".equals(respContent)) {
//					return timeMS;
//				} else {
//					log.info(host+":"+port+" check is return:"+respContent);
//				}			
//			}

			/*
request:https://oneid.mmstat.com/taobao/getDeviceInfo
response:200
{
status: 400,
message: "BAD REQUEST - missing required argument 'tokenid'",
isError: true,
data: null
}
			 */
//			if (timeCost<10000 && resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
//				String respContent =  resp.getResponseContent();
//				Map map = GsonUtil.toMap(respContent);
//				if (map !=null && "400".equals(String.valueOf(map.get("status")))) {
//					return timeMS;	
//				} else {
//					log.info(host+":"+port+" check is return:"+respContent);
//				}				
//			}

			/*
request:http://oneid.mmstat.com/taobao/getDeviceInfo
response:301
https://oneid.mmstat.com/taobao/getDeviceInfo
			 */
			if (timeCost<20000 && resp!=null && resp.getStatusCode()>=300 && resp.getStatusCode()<400 && resp.getResponseHeader()!=null) {
				Map<String, String> map = resp.getResponseHeader();
				if (map !=null && "https://oneid.mmstat.com/taobao/getDeviceInfo".equals(map.get("Location"))) {
					return timeMS;	
				} else {
					log.info(host+":"+port+" check is return:"+GsonUtil.getJsonWithFormat(resp));
				}				
			}
		
		} catch (Exception e) {
			log.warn("parse host:port Exception", e);
		}
		return null;
	}

//	public static String testHttpAuth() {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setRequestURL("http://221.122.127.226:19800/");
//		req.setUserName("root");
//		req.setPassword("yiqifa-dubbo");
//		TapHttpResponse res = doGet(req);
//		return res.getResponseHeader().toString();
//	}
//
//	public static String testHttpNoAuth() {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setRequestURL("http://221.122.127.38/nstatus");
//		req.setUserName("root");
//		req.setPassword("yiqifa-dubbo");
//		TapHttpResponse res = doGet(req);
//		return res.getResponseContent();
//	}
//
//	public static String testDoGet() {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setRequestURL("https://briskads.go2affise.com/click?pid=41&offer_id=145&sub1=58AB52BA-8CFB-44D4-B10F-AB5191C7D0CC&sub2=f255b0c6-4307-4b02-b53a-36ab6ec8b18c");
//		
////		ProxyConfig proxyConfig = new ProxyConfig("http", "127.0.0.1", 8123);
////		ProxyConfig proxyConfig = new ProxyConfig("http", "175.155.225.90", 8118);
////		ProxyConfig proxyConfig = new ProxyConfig("http", "104.129.204.130", 8800);
////		req.setProxyConfig(proxyConfig);
//		req.setProxyConfig(null);
//		TapHttpResponse res = doGet(req);
//		return GsonUtil.getJsonWithFormat(res);
//	}
//
//	public static String testNavs(String url) {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setUserAgent(ClickUtil.getUserAgentByOS("ios"));		
//		req.setRequestURL(url);
//		List respList = new ArrayList();
//		getNavs(req, respList);
//		
//		return GsonUtil.getJsonResult(respList);
//	}
//	
//	public static String testStingrad() {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setRequestURL("http://api.stingrad.com/affiliate/offer/findAll/?token=MyTtdbp8Du0ovTfnVQeaTaWXX0CRTAh2&offer_id=30669,31321,31286,31288");
//
////		ProxyConfig proxyConfig = new ProxyConfig("http", "127.0.0.1", 8123);
////		ProxyConfig proxyConfig = new ProxyConfig("http", "175.155.225.90", 8118);
////		ProxyConfig proxyConfig = new ProxyConfig("http", "104.129.204.130", 8800);
////		req.setProxyConfig(proxyConfig);
//		req.setProxyConfig(null);
//		TapHttpResponse res = doGet(req);
//		Map offer = GsonUtil.toMap(res.getResponseContent());
//		return GsonUtil.getJsonWithFormat(offer);
//	}
//
//	public static String testBrisk() {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setUserAgent(ClickUtil.getUserAgentByOS("ios"));		
////		req.setRequestURL("https://briskads.go2affise.com/click?pid=41&offer_id=145&sub1=64936374-B4D0-4097-A2FA-0FE160DBF8DE&sub2=f255b0c6-4307-4b02-b53a-36ab6ec8b18c");
//		req.setRequestURL("https://briskads.go2affise.com/click?pid=41&offer_id=139&sub1=75604C8D-FA6E-4053-BA76-73126EA9F268&sub2=f355b0c7-4307-4b05-b53a-36ab6ec8b18c");
//		List respList = new ArrayList();
//		getNavs(req, respList);
//		
//		return GsonUtil.getJsonResult(respList);
//	}
//
//	public static String testBlam() {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setUserAgent(ClickUtil.getUserAgentByOS("ios"));		
//		req.setRequestURL("http://track.blam.mobi/?aff_id=696441&offer_id=35797&idfa=43A35536-DA93-40BC-A288-DAD5BD3A04C8&gaid=&pb=DC-f746b24a-5f6c-4c3f-9fa8-f11acba012ce&subid=6331893");
//		List respList = new ArrayList();
//		getNavs(req, respList);
//		
//		return GsonUtil.getJsonWithFormat(respList);
//	}
//
//	public static String wzgTest() {
//		TapHttpRequest req = new TapHttpRequest();
//		req.setUserAgent("Mozilla/5.0(iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/603.2.4 (KHTML, like Gecko) Version/10.0 Mobile/14F89 Safari/602.1");		
////		req.setRequestURL("https://atracking-auto.appflood.com/transaction/post_click?offer_id\u003d15706051\u0026aff_id\u003d10811\u0026aff_sub\u003ddfe0a7d9-4fc7-4928-a6bd-b1a3c5a70b34\u0026aff_sub3\u003d484D6ADE-152C-4740-9DCA-49F246DAF767\u0026aff_sub6\u003d65490\u0026subid1\u003ddfe0a7d9-4fc7-4928-a6bd-b1a3c5a70b34");
////		req.setRequestURL("https://click.khingtracking.com/?sc=33230&t=1&l=264383&p=101346&partner_click_id=964f3380-a739-4d9d-a322-72189a9d2d0f&sub_id1=7D065A03-0460-4A7B-BDD4-E1BAA56BD4A3&url=29hPf8");
//
//		// 成长宝宝贝
//		req.setRequestURL("https://click.khingtracking.com/?sc=33230&t=1&l=264383&p=101346&partner_click_id=964f3380-a739-4d9d-a322-72189a9d2d0f&sub_id1=7D065A03-0460-4A7B-BDD4-E1BAA56BD4A3&url=29hPf8");
//				
////		req.setRequestURL("https://p.gouwuke.com/n?k=2mLErntOWZLErI6H2mLErntFWEKmrnKq6NzS6ZLO6nUH2mqcrI6H3E2e1JDs15MHRc645wb!rj--&t=http://www.jd.com/");
//		
////		req.setRequestURL("https://www.eqifa.com/earner/user/getUserAndManager.do");
////		req.getRequestHeader().put("Cookie", "Hm_lvt_f09985dbe1fc925f9c416567c4aa9baa=1499244618; Hm_lvt_7b29d1b550eef9d074536cb2d722c5bf=1499305890; Hm_lvt_8d19433c91b6c77ae1824b75eabbdda1=1499243741,1499826970,1499929948; Hm_lpvt_8d19433c91b6c77ae1824b75eabbdda1=1499937060; eqifaUser=Y2hvbmdub2hAZ21haWwuY29tLy8vLzI1MDc1Ni8vZWFybmVyLy95YW5nYm9AZW1hci5jb20uY24vLy8vLy8wMTAtNTg3OTM5ODAtODM5OC8vMTEyNTkwNTIzNS8vLy95YW5nYm9AZW1hci5jb20uY24vLy8vLy8wMTAtNTg3OTM5ODAtODM5OC8vMTEyNTkwNTIzNS8vLy/T0NCnLy83Ny8vMS8vZGJmYzg2NWQy; JSESSIONID=1FB2CFF185714039BD1AD4C6A4A44567; Hm_lvt_93ab254e2850d6854661ed38fb82e9cb=1499244813,1499826976,1499937066; Hm_lpvt_93ab254e2850d6854661ed38fb82e9cb=1499937075");
//
//		List respList = new ArrayList();
//		getNavs(req, respList);
//		
//		return GsonUtil.getJsonWithFormat(respList);
//	}
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		System.out.println(wzgTest());
////		String url1 = "http://click.startappservice.com/tracking/adClick?d=IAAAAAAgAAA6X1tPSERNW11cW11NQkxYQ1dVTSUWCxEzHQYRGxsHGAwXFD0FUhoOBFAVHAYBHB4ARU8aBA4cHUlRUU1WTUFVX01ATEFaW1tWRVdLQldQQklQQUBdXVtHSUJXWF5TWVRIRExSQk1XU0FXU1JDSUJDXlhcWUZIQEklMyQ%2BQBVdCAUQGBFEW1ZXXVZNEhdRRkECFkhXXlpETxFBXg1NWlFENzUxLUQESBYDDAITSUJNUFldXwQaQ0BUERJZQkBcWFUTTEcWRD0nKC0mJzE5JldTR01UQFBEWkJMVCYgODE4PzhCQUVcIjkmJjwmPCggSlxcVicsPz8%2BMyQoU0RTUzM%2FNDw3CBtNX11EQ1pRUUFRTTErKjE9MDRXX1pTKTA1OFdQQ0VcFhEJHQZRTkBJCgADRQAbEg1PHxsDGRwATVRFREBaX1hNXFREIxUTFxoVERxFCRYbHREaCgpNXlZEIBwOAgQIHhdGX1NKRAQGHApNXVdERxcAQExSSRFIDVsUGF1AWQpXRgdPRE1MRBBYFEJSCFtCSRRBSl1TWktKUENUT1lVXkBGX1ZPKzk3ITA6Lj0tUEVXTwcIHhcJC0BBSU0yO0xdU1hIXURCRkBcSVNRW15CWkZNVF5NU1NENz0yMzYtNTRGVlZPDQIBDExWXFhJXURCSkxcQF5VTVtLREBaWUxfW1NEFRUNARFC&amp;ru=https%3A%2F%2Fc.o5o4o6.com%2F%3Fa%3D185%26c%3D20513%26af_pid%3D311187%26E%3DJIcHRuimJII%253d%26s1%3D211783096%26s2%3Dstartapp_click_id_placeholder%26udid%3Dstartapp_advertising_id&advId=0f8852ac-34d0-49c3-8462-2ea0e69ec683&clickId=DC-ff2a72a2-3bad-43aa-aae2-1d1e0d33d6e0&;prod=12345&amp;pub=yeahmobi&segId=211783096";
////		String url2 = "http://earner.yiqifa.com";
////		System.out.println(getNavs(url2, new ArrayList()));
////		System.out.println(testHttpAuth());
////		System.out.println(testHttpNoAuth());
//
//		//System.out.println(testNavs("http://track.blam.mobi/?aff_id=696441&offer_id=35797&idfa=43A35536-DA93-40BC-A288-DAD5BD3A04C8&gaid=&pb=DC-f746b24a-5f6c-4c3f-9fa8-f11acba012ce&subid=6331893"));
//		//System.out.println(testNavs("http://track.turbob.mobi/?aff_id=326098&offer_id=88553&aff_sub=020d239eba3fccadbb7dbfc6f01eaffa&aff_sub2=LIUfKq36IOjhcc4H"));
//		//System.out.println(testNavs("http://tracking.supeera.com/aff_c?offer_id=12932&aff_id=3440&aff_sub=c5ae9ca90526fafe85253a39c6119db9&aff_sub2=eZb4t7FkJUEMgaiE"));
//		//System.out.println(testNavs(""));
//		//System.out.println(testNavs("http://track.blam.mobi/?aff_id=696441&offer_id=35797&idfa={2143FBF-64E8-47D4-95F2-08BC3660EEE2&gaid=&pb=f665b0c7-5507-4b05-b53a-36ab6ec8b22c&subid=2345"));
//
//		// brisk
//		//System.out.println(testNavs("https://briskads.go2affise.com/click?pid=41&offer_id=139&sub1=f675b0c7-4307-4b05-b53a-36ab6ec8b18c&sub2=75604C8D-FA6E-4053-BA76-73126EA9F268"));
//		//System.out.println(testNavs("https://briskads.go2affise.com/click?pid=41&offer_id=116&sub1=f665b0c7-5507-4b05-b53a-36ab6ec8b18c&sub2=75604C8D-FA6E-4053-BA76-73126EA9F268"));
//		//System.out.println(testNavs("https://briskads.go2affise.com/click?pid=41&offer_id=140&sub1=f675b0c7-4307-4b05-b53a-36ab6ec8b18c&sub2=75604C8D-FA6E-4053-BA76-73126EA9F268"));
//		//yeahmobi
//		//System.out.println(testNavs("http://clicks.mobrand.net/target/t.mobrand.net/tracking/aff/MZXuS9QST8C4cVYqjy_Yfg/PcldWSwQT0yXaTaVbx_k6w/LCooGzZBJWlmCnMGUGQ?aff_sub=e379ec7d-4c88-49a5-a6aa-2d93ba8048c5__pspm&source=1373693588_1003&idfa=&advid="));
//
//		
//		// khingdom
//		//System.out.println(testNavs("https://click.khingtracking.com/?sc=32425&t=1&l=263446&p=101346&partner_click_id=71316b3b-73ced-4543-954a-0c46b613b598&sub_id1=68267&ios_ifa=device_id&g_aid=device_id&url=cEf1D&device_id=15DA0B96-A2DC-440D-BDF5-AEBD7466FFE4"));
//		//System.out.println(testNavs("https://click.khingtracking.com/?sc=32425&t=1&l=263446&p=101346&partner_click_id=22316b3b-73ad-4543-954a-0c46b613b577&sub_id1=68267&ios_ifa=8CF292F0-59AB-4106-85AE-6D07E48BE98E&url=cEf1D"));
//		// appflood
//		// "uri": "http://atracking-auto.appflood.com/transaction/post_click?offer_id=14112961&aff_id=10688&aff_sub=DC-996e224c-e073-4ff8-bc6c-ca8fad92b7f2&aff_sub3=d2c3d165-1323-4d05-83d8-06e3a15abfca&aff_sub6=2287015",
//		//System.out.println(testNavs("https://atracking-auto.appflood.com/transaction/post_click?offer_id=12452384&aff_id=10811&source=12345&aff_sub=DC-996e224c-e073-4ff8-bc6c-ca8fad92b7f2&aff_sub3=d2c3d165-1323-4d05-83d8-06e3a15abfca&aff_sub6=2287015"));
//		//https://atracking-auto.appflood.com/transaction/post_click?offer_id=1934533&aff_id=10811&source=12345&aff_sub=DC-996e224c-e073-4ff8-bc6c-ca8fad92b7f2
//		//System.out.println(testNavs("https://atracking-auto.appflood.com/transaction/post_click?offer_id=1934533&aff_id=10811&aff_sub=f675b0c7-4307-4b05-b53a-36ab6ec8b18c&aff_sub3=DC-996e224c-e073-4ff8-bc6c-ca8fad92b7f2&aff_sub6=12345"));
//		
//
//		// blam
//		//System.out.println(testNavs("http://track.blam.mobi/?aff_id=696441&offer_id=35797&idfa=2143FBF-64E8-47D4-95F2-08BC3660EEE2&gaid=&pb=f665b0c7-5507-4b05-b53a-36ab6ec8b22c&subid=2345"));
//		//System.out.println(testNavs("http://track.blam.mobi/?aff_id=696441&offer_id=35797&aff_sub=2143FBF-64E8-47D4-95F2-08BC3660EEE2&aff_sub2=f665b0c7-5507-4b05-b53a-36ab6ec8b22c"));
//
//		//System.out.println(parseRefreshUrl("<meta http-equiv=\\\"refresh\\\" content=\\\"0;url=https://trk.tracksys55.com/click?f=a&pub_id=267&ctv_id=123101&line_item_id=52417&pub_sub=p30d2600de7f087a277f90e4a60e2efe74e&sub_pub_id=101346_68267\\\">"));
////		System.out.println(checkProxyTime("127.0.0.1", 8123));
////		System.out.println(testStingrad());
//	}

}
