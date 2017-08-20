package com.tapmobi.common.util;

import java.util.Arrays;
import java.util.Map;

import com.tapmobi.common.http.TapHttpRequest;
import com.tapmobi.common.http.TapHttpResponse;
import com.tapmobi.common.http.TapMobiBrowser;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class IPAreaUtil {

	/*
{
code: 0,
data: {
country: "美国",
country_id: "US",
area: "",
area_id: "",
region: "",
region_id: "",
city: "",
city_id: "",
county: "",
county_id: "",
isp: "",
isp_id: "",
ip: "34.223.227.44"
}
}
	 */

	/**
	 * 根据ip查询所属国家代码
	 * @param ip
	 * @return
	 */
	public static String getCountryByIP2(String ip) {
		String reqURL = "http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
		String decodeCharset = "UTF-8";
		String json = HttpClientUtil.sendGetRequestWithBody(reqURL, decodeCharset);
		//System.out.println(json);
		Map map = GsonUtil.toMap(json);
		if (map !=null && "0".equals(String.valueOf(map.get("code")))) {
			Map data = (Map)map.get("data");
			if (data!=null) {
				return (String)data.get("country_id");
			}
		}
		return null;
	}

	public static String getCountryByIP(String ip) {
		return getCountryByIPForIPIP(ip);
	}

	public static String getCountryByIPForIPIP(String ip) {
		String country = null;
		IP.loadDefault();
		String[] result = IP.find(ip);
		if (result!=null && result.length>0) {
			switch (result[0]) {
			case "阿联酋":
				country = "AE";
				break;
			case "加拿大":
				country = "CA";
				break;
			case "中国":
				country = "CN";
				break;
			case "印度尼西亚":
				country = "ID";
				break;
			case "印度":
				country = "IN";
				break;
			case "日本":
				country = "JP";
				break;
			case "沙特阿拉伯":
				country = "SA";
				break;
			case "美国":
				country = "US";
				break;
			}
		}
		return country;
	}
	
	public static String getCountryByIPForTaobao(String ip) {
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
		req.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		TapHttpResponse resp = TapMobiBrowser.doGet(req);
		
		// 如果检查过快则休眠100毫秒，因为IP库查询接口有qps10的限制
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) { }
		
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String json = resp.getResponseContent();
			Map map = GsonUtil.toMap(json);
			if (map !=null && "0".equals(String.valueOf(map.get("code")))) {
				Map data = (Map)map.get("data");
				if (data!=null) {
					return (String)data.get("country_id");
				}
			}
		}
		return null;
	}
	
	public static void main(String[] arg) {
        System.out.println(getCountryByIP("46.151.215.177"));
        System.out.println(getCountryByIP("103.16.199.81"));
        System.out.println(getCountryByIP("47.91.104.228"));
		System.out.println(getCountryByIP("35.162.152.22"));
		System.out.println(getCountryByIP("34.223.227.44"));
		System.out.println(getCountryByIP("8.8.8.8"));
		System.out.println(getCountryByIP("211.211.211.211"));
	}
}
