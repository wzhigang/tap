package com.tapmobi.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.tapmobi.common.http.TapHttpRequest;
import com.tapmobi.common.http.TapHttpResponse;
import com.tapmobi.common.http.TapProxyConfig;

public class TapMobiBrowser {
	private static Logger log = LoggerFactory.getLogger(TapMobiBrowser.class);

	public static TapHttpResponse doGet(TapHttpRequest req) {
		return doGetByHttpClient(req);
	}

	public static TapHttpResponse doGetByHttpClient(TapHttpRequest req) {
		TapHttpResponse resp = new TapHttpResponse();
		String requestURL = req.getRequestURL();
		TapProxyConfig proxyConfig = req.getProxyConfig();
		
		CloseableHttpClient httpclient = null;
		HttpGet httpGet = null;

		try {
//		CloseableHttpClient httpclient = TapMobiClient.createHttpClient(requestURL.startsWith("https://"));
		httpclient = TapMobiClient.createHttpClient(req);

		httpGet = new HttpGet(requestURL);

		// set RedirectsEnabled is false
		Builder builder = RequestConfig.custom().setRedirectsEnabled(false);
		// set http proxy
		if (proxyConfig!=null) {
			HttpHost proxy = new HttpHost(proxyConfig.getProxyHost(), proxyConfig.getProxyPort(), proxyConfig.getProxyType());
			builder.setProxy(proxy);
			httpGet.setHeader("X-Real-IP", proxyConfig.getProxyHost());
			httpGet.setHeader("X-Forwarded-For", proxyConfig.getProxyHost());
		}
		// set timeout
		RequestConfig requestConfig = builder.setSocketTimeout(40000).setConnectTimeout(40000).build();

		httpGet.setConfig(requestConfig);

		httpGet.setHeader("User-Agent", req.getUserAgent());
		
		if (req.getRequestHeader().get("Cookie")!=null) {
			httpGet.setHeader("Cookie", req.getRequestHeader().get("Cookie"));
		}
//		if (req.getRequestHeader().get("Referer")!=null) {
//			httpGet.setHeader("Referer", req.getRequestHeader().get("Referer"));
//		}

		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
		//httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		httpGet.setHeader("Cache-Control", "no-cache");
		httpGet.setHeader("Pragma", "no-cache");
		httpGet.setHeader("Connection", "keep-alive");
		
		log.debug("httpGet:" + httpGet);

		HttpResponse httpResponse = null;

			httpResponse = httpclient.execute(httpGet);
			resp.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			Header[] headers = httpResponse.getAllHeaders();
			for (int i=0; i<headers.length; i++) {
//				System.out.println(headers[i].getName());
//				System.out.println(headers[i].getValue());
				resp.getResponseHeader().put(headers[i].getName(), headers[i].getValue());
				if ("Set-Cookie".equalsIgnoreCase(headers[i].getName())) {
					resp.getCookies().add(headers[i].getValue());
				}
			}
	        
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				resp.setResponseContent(EntityUtils.toString(entity));

		        // 为了解决中文乱码，改为下面
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
//		        String line;
//		        StringBuffer strBuf = new StringBuffer();
//		        while((line = bufferedReader.readLine()) != null) {
//		        	strBuf.append(line).append(System.getProperty("line.separator"));
//		        }
//		        resp.setResponseContent(strBuf.toString());
			} else {
				log.warn("url response entity is null");
			}
		} catch (ConnectException ce) {
			String info = "Connect Exception";
			info += ", url:" + requestURL;
			if (proxyConfig!=null) {
				info += ", proxy "+proxyConfig.getProxyHost()+":"+proxyConfig.getProxyPort();
			}
			info += ", error:"+ce.getMessage();
			log.warn(info);
		} catch (ConnectTimeoutException te) {
			String info = "Connect Timeout Exception";
			info += ", url:" + requestURL;
			if (proxyConfig!=null) {
				info += ", proxy "+proxyConfig.getProxyHost()+":"+proxyConfig.getProxyPort();
			}
			info += ", error:"+te.getMessage();
			log.warn(info);
		} catch (Exception e) {
			String info = "request url is error";
			info += ", url:" + requestURL;
			if (proxyConfig!=null) {
				info += ", proxy "+proxyConfig.getProxyHost()+":"+proxyConfig.getProxyPort();
			}
			info += ", error:"+e.getMessage();
			log.warn(info);
		} finally {
			if (httpclient!=null) {
				try {
					httpclient.close();
				} catch (IOException ioe) {
					log.warn("httpclient.close Excepiton" , ioe);
				}
			}
		}
		return resp;
	}

	public static TapHttpResponse doGetByHttpUtil(TapHttpRequest req) {
		TapHttpResponse resp = new TapHttpResponse();
		String requestURL = req.getRequestURL();
		
		WebClient webClient=new  WebClient(BrowserVersion.CHROME);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(2 * 1000);
        webClient.getCookieManager().setCookiesEnabled(true);

        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(60* 1000);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);       
        webClient.getOptions().setScreenHeight(736);
        webClient.getOptions().setScreenWidth(414);		
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        
        TapProxyConfig proxyConfig = req.getProxyConfig();
        if (proxyConfig!=null) {
            com.gargoylesoftware.htmlunit.ProxyConfig htmlutilProxyConfig = webClient.getOptions().getProxyConfig();   
            htmlutilProxyConfig.setProxyHost(proxyConfig.getProxyHost());    
            htmlutilProxyConfig.setProxyPort(proxyConfig.getProxyPort());  
        }
        
		try {
			URL link=new URL(requestURL); 
			WebRequest webRequest = new WebRequest(link);
			webRequest.setAdditionalHeader("User-Agent", req.getUserAgent());
			
//			HtmlPage page = webClient.getPage(webRequest);
			//HtmlPage page = webClient.getPage(requestURL);
			Page page = webClient.getPage(requestURL);
//			Thread.sleep(1000*60);
			
//			String html = page.getWebResponse().getContentAsString();
//			WebResponse r = page.getWebResponse();
//			String curUrl = r.getWebRequest().getUrl().toString();
//			html = r.getContentAsString();
//			log.info(curUrl);
//			log.info("--------------------------------------------------");
//			log.info(html);
//			log.info("--------------------------------------------------");

			List<NameValuePair>  headers = page.getWebResponse().getResponseHeaders();
			for (NameValuePair header : headers) {
				resp.getResponseHeader().put(header.getName(), header.getValue());
				
			}
			
			resp.setResponseContent(page.getWebResponse().getContentAsString());
		} catch (FailingHttpStatusCodeException e) {
			log.warn("TapMobiBrowser FailingHttpStatusCodeException" , e);
		} catch (MalformedURLException e) {
			log.warn("TapMobiBrowser MalformedURLException" , e);
		} catch (IOException e) {
			log.warn("TapMobiBrowser IOException" , e);
		} catch (Exception e) {
			log.warn("TapMobiBrowser Excepiton" , e);
		} finally {
            webClient.close();
        }
		return resp;
	}
}
