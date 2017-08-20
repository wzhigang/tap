package com.tapmobi.model;

import java.io.Serializable;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class Offer implements Serializable {
	private static final long serialVersionUID = 3595201474639770131L;

	private Long id;
	private Integer affiliateId;
	private Integer offerId;
	private String trackUrl;
	private String platform;
	private String os;
	private String appId;
	private String currency;
	private String status;
	private String expirationDate;
	private Float defaultPayout;
	private Integer priority;
	private Float dailybudget;
	private Integer dailymaxclicks;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAffiliateId() {
		return affiliateId;
	}
	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}
	public Integer getOfferId() {
		return offerId;
	}
	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}
	public String getTrackUrl() {
		return trackUrl;
	}
	public void setTrackUrl(String trackUrl) {
		this.trackUrl = trackUrl;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Float getDefaultPayout() {
		return defaultPayout;
	}
	public void setDefaultPayout(Float defaultPayout) {
		this.defaultPayout = defaultPayout;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Float getDailybudget() {
		return dailybudget;
	}
	public void setDailybudget(Float dailybudget) {
		this.dailybudget = dailybudget;
	}
	public Integer getDailymaxclicks() {
		return dailymaxclicks;
	}
	public void setDailymaxclicks(Integer dailymaxclicks) {
		this.dailymaxclicks = dailymaxclicks;
	}

}
