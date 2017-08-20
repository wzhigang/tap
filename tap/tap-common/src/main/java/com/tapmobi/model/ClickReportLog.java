package com.tapmobi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class ClickReportLog implements Serializable {
	private static final long serialVersionUID = -3279062786061620700L;

	private String tableName;
	
	private long id;
	private String clickId;
	private int affiliateId;
	private int offerId;
	private String appId;
	private String deviceId;
	private String networkId;
	private String currency;
	private String clickResult;
	private volatile int timeCost;
	private Date createTime;
	private volatile int hourMaxClick;
	private AtomicInteger hourSuccess;
	private AtomicInteger hourFail;
	private volatile int jumpCount;
	private String campaignId;
	private String trackUrl;
	private boolean isReachable;
	private String placementId;
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the affiliateId
	 */
	public int getAffiliateId() {
		return affiliateId;
	}
	/**
	 * @param affiliateId the affiliateId to set
	 */
	public void setAffiliateId(int affiliateId) {
		this.affiliateId = affiliateId;
	}
	/**
	 * @return the offerId
	 */
	public int getOfferId() {
		return offerId;
	}
	/**
	 * @param offerId the offerId to set
	 */
	public void setOfferId(int offerId) {
		this.offerId = offerId;
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
	 * @return the networkId
	 */
	public String getNetworkId() {
		return networkId;
	}
	/**
	 * @param networkId the networkId to set
	 */
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the clickResult
	 */
	public String getClickResult() {
		return clickResult;
	}
	/**
	 * @param clickResult the clickResult to set
	 */
	public void setClickResult(String clickResult) {
		this.clickResult = clickResult;
	}
	/**
	 * @return the timeCost
	 */
	public int getTimeCost() {
		return timeCost;
	}
	/**
	 * @param timeCost the timeCost to set
	 */
	public void setTimeCost(int timeCost) {
		this.timeCost = timeCost;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the hourMaxClick
	 */
	public int getHourMaxClick() {
		return hourMaxClick;
	}
	/**
	 * @param hourMaxClick the hourMaxClick to set
	 */
	public void setHourMaxClick(int hourMaxClick) {
		this.hourMaxClick = hourMaxClick;
	}
	/**
	 * @return the hourSuccess
	 */
	public AtomicInteger getHourSuccess() {
		return hourSuccess;
	}
	/**
	 * @param hourSuccess the hourSuccess to set
	 */
	public void setHourSuccess(AtomicInteger hourSuccess) {
		this.hourSuccess = hourSuccess;
	}
	/**
	 * @return the hourFail
	 */
	public AtomicInteger getHourFail() {
		return hourFail;
	}
	/**
	 * @param hourFail the hourFail to set
	 */
	public void setHourFail(AtomicInteger hourFail) {
		this.hourFail = hourFail;
	}
	/**
	 * @return the jumpCount
	 */
	public int getJumpCount() {
		return jumpCount;
	}
	/**
	 * @param jumpCount the jumpCount to set
	 */
	public void setJumpCount(int jumpCount) {
		this.jumpCount = jumpCount;
	}
	/**
	 * @return the campaignId
	 */
	public String getCampaignId() {
		return campaignId;
	}
	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	/**
	 * @return the trackUrl
	 */
	public String getTrackUrl() {
		return trackUrl;
	}
	/**
	 * @param trackUrl the trackUrl to set
	 */
	public void setTrackUrl(String trackUrl) {
		this.trackUrl = trackUrl;
	}
	/**
	 * @return the isReachable
	 */
	public boolean isReachable() {
		return isReachable;
	}
	/**
	 * @param isReachable the isReachable to set
	 */
	public void setReachable(boolean isReachable) {
		this.isReachable = isReachable;
	}
	/**
	 * @return the placementId
	 */
	public String getPlacementId() {
		return placementId;
	}
	/**
	 * @param placementId the placementId to set
	 */
	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}


}
