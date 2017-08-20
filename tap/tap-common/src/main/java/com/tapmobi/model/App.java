package com.tapmobi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class App implements Serializable {
	private static final long serialVersionUID = -6776576013240871214L;
	private Long id; 
	private Integer rank;
	private Integer yesterdayRank;
	private Integer lastWeekRank;
	private String country;
	private String previewUrl;
	private Long appId;
	private String platform;
	private String name;
	private String description;
	private String category;
	private String packageName;
	private Date sysTime;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getYesterdayRank() {
		return yesterdayRank;
	}
	public void setYesterdayRank(Integer yesterdayRank) {
		this.yesterdayRank = yesterdayRank;
	}
	public Integer getLastWeekRank() {
		return lastWeekRank;
	}
	public void setLastWeekRank(Integer lastWeekRank) {
		this.lastWeekRank = lastWeekRank;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPreviewUrl() {
		return previewUrl;
	}
	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Date getSysTime() {
		return sysTime;
	}
	public void setSysTime(Date sysTime) {
		this.sysTime = sysTime;
	}
}
