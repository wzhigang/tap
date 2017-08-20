package com.tapmobi.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.tapmobi.common.http.TapHttpRequest;
import com.tapmobi.common.http.TapHttpResponse;
import com.tapmobi.common.http.TapProxyConfig;
import com.tapmobi.common.http.TapMobiBrowser;

public class ProxyUtil {
	private static Logger log = LoggerFactory.getLogger(ProxyUtil.class);


	// "http://api.xicidaili.com/free2016.txt"
	// http://www.tapmobi.io/proxy/proxy.txt
	public static List<String> getProxyByText(String url) {
		log.info("getProxyByText url:"+url);
		List<String> list = new ArrayList<String>();

		try {
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL(url);
		TapHttpResponse resp = TapMobiBrowser.doGet(req);
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String linesep = System.getProperty("line.separator");
			linesep = "\n";
			String[] hosts = resp.getResponseContent().split(linesep);
			if (hosts!=null && hosts.length>0) {
				for (int i=0; i<hosts.length; i++) {
					list.add(hosts[i].trim());
				}
				log.info("getProxyByText done. new proxy size:"+list.size());
			}
		}
		} catch (Exception e) {
			log.warn("getProxyByText Exception:", e);
		}

		return list;
	}


	/*
    <tr class="odd">
      <td class="country"><img src="http://fs.xicidaili.com/images/flag/cn.png" alt="Cn" /></td>
      <td>183.159.74.154</td>
      <td>8118</td>
      <td>
        <a href="/2017-07-30/zhejiang">浙江杭州</a>
      </td>
      <td class="country">高匿</td>
      <td>HTTP</td>
      <td class="country">
        <div title="0.531秒" class="bar">
          <div class="bar_inner fast" style="width:85%">
            
          </div>
        </div>
      </td>
      <td class="country">
        <div title="0.106秒" class="bar">
          <div class="bar_inner fast" style="width:97%">
            
          </div>
        </div>
      </td>
      
      <td>5小时</td>
      <td>17-07-30 16:55</td>
    </tr>
	 */
	/*
	 * http://api.xicidaili.com/free2016.txt
	 * http://www.xicidaili.com/nn/
	 */
	public static List<String> getProxyByXicidaili(String url) {
		log.info("getProxyByXicidaili url:"+url);
		List<String> list = new ArrayList<String>();

		try {
//		String url = "http://www.xicidaili.com/nn/";
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL(url);
		TapHttpResponse resp = TapMobiBrowser.doGetByHttpUtil(req);
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String html = resp.getResponseContent();
			Document doc = Jsoup.parse(html);
            Elements elements_tr = doc.select("table").first().children().select("tbody").first().children();
            log.info("get page list size =====" + elements_tr.size());
            for (Element e_tr : elements_tr) {
            	Elements elements_td = e_tr.select("td");
            	String host = "";
            	String port = "";
            	String type = "";

            	int i=0;
            	for (Element e_td : elements_td) {
            		switch (i) {
            			case 1:
            			host = e_td.text();
            			break;
            			case 2:
            			port = e_td.text();
            			break;
            			case 5:
            			type = e_td.text();
            			break;
            		}
            		i++;
            	}
            	if (!StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(type) && type.toLowerCase().startsWith("http")) {
            		String proxy = host+":"+port;
            		//System.out.println(proxy);
            		list.add(proxy);
            	}
            }
		}
		log.info("getProxyByXiciDaili done. new proxy size:"+list.size());
		} catch (Exception e) {
			log.error("getProxyByXiciDaili Exception:", e);
		}

		return list;
	}
	
	/*
	 * http://www.kuaidaili.com/free/inha/2/
	 */
	public static List<String> getProxyByKuaidaili(String url) {
		log.info("url:"+url);
		List<String> list = new ArrayList<String>();

		try {
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL(url);
		req.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		
//		TapHttpResponse resp = TapMobiBrowser.doGetByHttpUtil(req);
		TapHttpResponse resp = TapMobiBrowser.doGetByHttpClient(req);
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String html = resp.getResponseContent();
			Document doc = Jsoup.parse(html);
            Elements elements_tr = doc.select("table.table-bordered").first().children().select("tbody").first().children();
            log.info("get page list size =====" + elements_tr.size());
            
            for (Element e_tr : elements_tr) {
            	Elements elements_td = e_tr.select("td");
            	String host = "";
            	String port = ":";
            	String type = "http";

            	int i=0;
            	for (Element e_td : elements_td) {
            		switch (i) {
            			case 0:
            			host = e_td.text();
            			break;
            			case 1:
            			port = e_td.text();
            			break;
            			case 3:
            			type = e_td.text();
            			break;
            		}
            		i++;
            	}	

            	if (!StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(type) && type.toLowerCase().startsWith("http")) {
            		String proxy = host+":"+port;
            		//System.out.println(proxy);
            		list.add(proxy);
            	}
            }
		}
		log.info("done. new proxy size:"+list.size());
		} catch (Exception e) {
			log.error("Exception:", e);
		}

		return list;
	}
	
	/*
	 * http://www.xroxy.com/proxylist.php?port=&type=&ssl=&country=ID&latency=&reliability=&sort=reliability&desc=true&pnum=0
	 */
	public static List<String> getProxyByX(String url) {
		log.info("getProxyByX url:"+url);
		List<String> list = new ArrayList<String>();

		try {
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL(url);
		req.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");

//		ProxyConfig proxyConfig = new ProxyConfig("http", "127.0.0.1", 8123);
//		req.setProxyConfig(proxyConfig);
//		TapHttpResponse resp = TapMobiBrowser.doGetByHttpUtil(req);
		TapHttpResponse resp = TapMobiBrowser.doGetByHttpClient(req);
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String html = resp.getResponseContent();
			Document doc = Jsoup.parse(html);
            //Elements elements_tr = doc.select("table.plbc_bloc_proxy_table").first().children().select("tbody").first().children();
            Elements elements_tr = doc.select("[class^=row]");
            log.info("get page list size =====" + elements_tr.size());
            
            for (Element e_tr : elements_tr) {
            	Elements elements_td = e_tr.select("td");
            	String host = "";
            	String port = "";
            	String type = "http";

            	int i=0;
            	for (Element e_td : elements_td) {
            		switch (i) {
            			case 1:
            			host = e_td.text();
            			break;
            			case 2:
            			port = e_td.text();
            			break;
//            			case 5:
//            			type = e_td.text();
//            			break;
            		}
            		i++;
            	}	

            	if (!StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(type) && type.toLowerCase().startsWith("http")) {
            		String proxy = host+":"+port;
//            		System.out.println(proxy);
            		list.add(proxy);
            	}
            }
		}
		log.info("getProxyByX done. new proxy size:"+list.size());
		} catch (Exception e) {
			log.error("getProxyByX Exception:", e);
		}

		return list;
	}

	/*
	 * https://freevpn.ninja/free-proxy/country/sa
	 */
	public static List<String> getProxyByFreevpn(String url) {
		log.info("getProxyByFreevpn url:"+url);
		List<String> list = new ArrayList<String>();

		try {
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL(url);
		req.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");

		TapHttpResponse resp = TapMobiBrowser.doGetByHttpUtil(req);
//		TapHttpResponse resp = TapMobiBrowser.doGetByHttpClient(req);
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String html = resp.getResponseContent();
			Document doc = Jsoup.parse(html);
            //Elements elements_tr = doc.select("table.plbc_bloc_proxy_table").first().children().select("tbody").first().children();
            Elements elements_tr = doc.select("article");
            log.info("get page list size =====" + elements_tr.size());
            
            for (Element e_tr : elements_tr) {
            	Elements elements_td = e_tr.select("div");
            	String host = "";
            	String port = ":";
            	String type = "http";

            	int i=0;
            	for (Element e_td : elements_td) {
            		switch (i) {
            			case 0:
            			host = e_td.text();
            			break;
            			case 1:
            			type = e_td.text();
            			break;
            		}
            		i++;
            	}	

            	if (!StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(type) && type.toLowerCase().startsWith("http")) {
            		String proxy = host+":"+port;
//            		System.out.println(proxy);
            		list.add(proxy);
            	}
            }
		}
		log.info("getProxyByFreevpn done. new proxy size:"+list.size());
		} catch (Exception e) {
			log.error("getProxyByFreevpn Exception:", e);
		}

		return list;
	}

	/*
	 * https://www.proxydocker.com/en/proxylist/country/Saudi%20Arabia?page=1
	 */
	public static List<String> getProxyByProxydocker(String url) {
		log.info("getProxyByProxydocker url:"+url);
		List<String> list = new ArrayList<String>();

		try {
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL(url);
		req.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");

		String p = ConfigUtil.getString("tapmobi.click.getproxy.proxy");
		if (!StringUtils.isEmpty(p)) {
			TapProxyConfig proxyConfig = TapProxyConfig.resolveString(p);
			if (proxyConfig!=null) {
				req.setProxyConfig(proxyConfig);
			}
		}
		
//		TapHttpResponse resp = TapMobiBrowser.doGetByHttpUtil(req);
		TapHttpResponse resp = TapMobiBrowser.doGetByHttpClient(req);
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String html = resp.getResponseContent();
			Document doc = Jsoup.parse(html);
            Elements elements_tr = doc.select("table.table-hover").first().children().select("tbody").first().children();
            log.info("get page list size =====" + elements_tr.size());
            
            for (Element e_tr : elements_tr) {
            	Elements elements_td = e_tr.select("td");
            	String host = "";
            	String port = ":";
            	String type = "http";

            	int i=0;
            	for (Element e_td : elements_td) {
            		switch (i) {
            			case 0:
            			host = e_td.text();
            			break;
            			case 1:
            			type = e_td.text();
            			break;
            		}
            		i++;
            	}	

            	if (!StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(type) && type.toLowerCase().startsWith("http")) {
            		String proxy = host+":"+port;
//            		System.out.println(proxy);
            		list.add(proxy);
            	}
            }
		}
		log.info("getProxyByProxydocker done. new proxy size:"+list.size());
		} catch (Exception e) {
			log.error("getProxyByProxydocker Exception:", e);
		}

		return list;
	}
	
	/*
	 * https://www.proxynova.com/proxy-server-list/country-id/
	 */
	public static List<String> getProxyByNova(String url) {
		log.info("getProxyByNova url:"+url);
		List<String> list = new ArrayList<String>();

		try {
//		String url = "https://www.proxynova.com/proxy-server-list/country-id/";
//		String url = "http://www.qq.com/";
		TapHttpRequest req = new TapHttpRequest();
		req.setRequestURL(url);
		req.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");

//		ProxyConfig proxyConfig = new ProxyConfig("http", "127.0.0.1", 8123);
//		req.setProxyConfig(proxyConfig);
//		TapHttpResponse resp = TapMobiBrowser.doGetByHttpUtil(req);
		TapHttpResponse resp = TapMobiBrowser.doGetByHttpClient(req);
		if (resp!=null && resp.getStatusCode()==200 && resp.getResponseContent()!=null) {
			String html = resp.getResponseContent();
			Document doc = Jsoup.parse(html);
            Elements elements_tr = doc.select("table#tbl_proxy_list").first().children().select("tbody").first().children();
            log.info("get page list size =====" + elements_tr.size());

            Context cx = Context.enter();
            Scriptable scope = cx.initStandardObjects();
            
            for (Element e_tr : elements_tr) {
            	Elements elements_td = e_tr.select("td");
            	String host = "";
            	String port = "";
            	String type = "http";
            	String javascript = "";

            	int i=0;
            	for (Element e_td : elements_td) {
            		switch (i) {
            			case 0:
            			javascript = e_td.html();
            			break;
            			case 1:
            			port = e_td.text();
            			break;
//            			case 5:
//            			type = e_td.text();
//            			break;
            		}
            		i++;
            	}
            	
                // <td align="left"><script>document.write('23139.2'.substr(2) + '55.57.32');</script></td>
            	if (!StringUtils.isEmpty(javascript)) {
            		try {
            		javascript = javascript.replaceAll(Pattern.quote("<script>document.write("), "").replaceAll(Pattern.quote(");</script>"), "");            		
            		Object result = cx.evaluateString(scope, javascript, null, 1, null);
            		host = Context.toString(result);
//            		System.out.println("javascript:"+javascript);
//            		System.out.println("host:"+host);
            		} catch (Exception jse) {}
            	}
    			

            	if (!StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(type) && type.toLowerCase().startsWith("http")) {
            		String proxy = host+":"+port;
//            		System.out.println(proxy);
            		list.add(proxy);
            	}
            }
		}
		log.info("getProxyByNova done. new proxy size:"+list.size());
		} catch (Exception e) {
			log.error("getProxyByNova Exception:", e);
		}

		return list;
	}

	public static void main(String[] args) {
//		getProxyByNova("https://www.proxynova.com/proxy-server-list/country-id/");
//		getProxyByX("http://www.xroxy.com/proxylist.php?port=&type=&ssl=&country=ID&latency=&reliability=&sort=reliability&desc=true&pnum=0");
//		getProxyByFreevpn("https://freevpn.ninja/free-proxy/country/sa");
//		getProxyByProxydocker("https://www.proxydocker.com/en/proxylist/country/Saudi%20Arabia?page=1");
		getProxyByKuaidaili("http://www.kuaidaili.com/free/inha/2/");
	}

}
