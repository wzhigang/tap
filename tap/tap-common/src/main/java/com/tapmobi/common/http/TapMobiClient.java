package com.tapmobi.common.http;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.IgnoreSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import com.tapmobi.common.http.TapHttpRequest;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class TapMobiClient {

	public static final String[] DATE_PATTERNS = new String[] {
			"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz",
			"EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z",
			"EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z",
			"EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z",
			"EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z",
			"EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z",
			"EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z",
			"E, dd-MMM-yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz" };


	public static CloseableHttpClient createHttpClient(boolean isSSL) {
		if (isSSL) {
			try {
				SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				}).build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				return HttpClients.custom().setSSLSocketFactory(sslsf).build();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
		}
		return HttpClients.createDefault();
	}
	
	public static CloseableHttpClient createHttpClient(TapHttpRequest req) {
		PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();
		Registry<CookieSpecProvider> r = RegistryBuilder.<CookieSpecProvider>create()
//				.register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider(publicSuffixMatcher))
//		        .register(CookieSpecs.STANDARD, new RFC6265CookieSpecProvider(publicSuffixMatcher))
				.register(CookieSpecs.IGNORE_COOKIES,  new IgnoreSpecProvider())
		        .build();  
		
		CloseableHttpClient httpclient = null;

		boolean auth = false;
		CredentialsProvider provider = null;
		if (req.getUserName()!=null && req.getPassword()!=null && !"".equals(req.getUserName()) && !"".equals(req.getPassword())) {
			auth = true;

			// 设置BasicAuth
            provider = new BasicCredentialsProvider();
            AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(req.getUserName(), req.getPassword());
            provider.setCredentials(scope, credentials);              
		}

		if (req.getRequestURL().startsWith("https://")) {
			try {
				SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				}).build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				if (auth) {
					httpclient = HttpClients.custom().setDefaultCookieSpecRegistry(r).setDefaultCredentialsProvider(provider).setSSLSocketFactory(sslsf).build();
					return httpclient;
					//return HttpClients.custom().setDefaultCredentialsProvider(provider).setSSLSocketFactory(sslsf).build();
				} else {
					httpclient = HttpClients.custom().setDefaultCookieSpecRegistry(r).setSSLSocketFactory(sslsf).build();
					return httpclient;
					//return HttpClients.custom().setSSLSocketFactory(sslsf).build();
				}
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
		}
		if (auth) {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			httpClientBuilder.setDefaultCredentialsProvider(provider);
			httpclient = httpClientBuilder.setDefaultCookieSpecRegistry(r).build();
			return httpclient;
			//return httpClientBuilder.build();
		}
		
		httpclient = HttpClients.custom().setDefaultCookieSpecRegistry(r).build();		
		return httpclient;
		//return HttpClients.createDefault();
	}
}
