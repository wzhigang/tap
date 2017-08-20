package com.tapmobi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class Activate implements Serializable {
	private static final long serialVersionUID = 3487892973743307966L;
	private Integer id;
	private String aId;
	private String clickId;
	private String deviceId;
	private String ip;
	private String activateDate;
	private String sysTime;
	private String appId;
	private String country;

	/**
	 * @return the aId
	 */
	public String getaId() {
		return aId;
	}

	/**
	 * @param aId the aId to set
	 */
	public void setaId(String aId) {
		this.aId = aId;
	}

	/**
	 * @return the clickId
	 */
	public String getClickId() {
		return clickId;
	}

	/**
	 * @param clickId the clickId to set
	 */
	public void setClickId(String clickId) {
		this.clickId = clickId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the activateDate
	 */
	public String getActivateDate() {
		return activateDate;
	}

	/**
	 * @param activateDate the activateDate to set
	 */
	public void setActivateDate(String activateDate) {
		this.activateDate = activateDate;
	}

	public Activate() {
	}

	/**
	 * @return the sysTime
	 */
	public String getSysTime() {
		return sysTime;
	}

	/**
	 * @param sysTime the sysTime to set
	 */
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}
