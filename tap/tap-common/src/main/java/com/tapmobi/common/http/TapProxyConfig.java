package com.tapmobi.common.http;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class TapProxyConfig {
    private String proxyType;
	private String proxyHost;
    private int proxyPort;

    private int speed;

    private String proxyUserName;
    private String proxyPassword;

    /*
     * 1 = new
     * 2 = vaild
     * 3 = slow
     * 4 = delete
     */
    private int proxyStauts;

    private String proxyCountry;
    private String proxyProvince;
    private String proxyCity;

    
    public TapProxyConfig(String proxyType, String proxyHost, int proxyPort) {
		this.proxyType = proxyType;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

    public TapProxyConfig(String proxyType, String proxyHost, int proxyPort, String proxyUserName, String proxyPassword) {
		this.proxyType = proxyType;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyUserName = proxyUserName;
		this.proxyPassword = proxyPassword;
	}

    public static TapProxyConfig resolveString(String proxy) {
    	String[] array = proxy.split(":");
		if (array!=null && array.length>1) {
			String host = array[0];
			int port = Integer.parseInt(array[1]);
			String user = "";
			String pass = "";
			if (array.length>3) {
				user = array[2];
				pass = array[3];
			}
			return new TapProxyConfig("http", host, port, user, pass);
		}
		return null;
    }

    public enum ProxyType {HTTP , SOCKES}


	public String getProxyHost() {
		return proxyHost;
	}


	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}


	public int getProxyPort() {
		return proxyPort;
	}


	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}


	public String getProxyUserName() {
		return proxyUserName;
	}


	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}


	public String getProxyPassword() {
		return proxyPassword;
	}


	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}


	public String getProxyType() {
		return proxyType;
	}


	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}


	public String getProxyCountry() {
		return proxyCountry;
	}


	public void setProxyCountry(String proxyCountry) {
		this.proxyCountry = proxyCountry;
	}


	public String getProxyProvince() {
		return proxyProvince;
	}


	public void setProxyProvince(String proxyProvince) {
		this.proxyProvince = proxyProvince;
	}


	public String getProxyCity() {
		return proxyCity;
	}


	public void setProxyCity(String proxyCity) {
		this.proxyCity = proxyCity;
	}


	public int getProxyStauts() {
		return proxyStauts;
	}


	public void setProxyStauts(int proxyStauts) {
		this.proxyStauts = proxyStauts;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


}
