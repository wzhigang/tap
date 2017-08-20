package com.tapmobi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Zhigang(wzhigang@gmail.com)
 * @version 1.0.0
 *
 */
public class ClickReportSum implements Serializable {
	private static final long serialVersionUID = 3474580945113716493L;

	private Long id;
	private Integer affiliateId;
	private Integer offerId;
	private String appId;
	private String currency;
	private Date stateDate;
	private Integer clickAmount;
	private Integer successAmount;

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
	public Date getStateDate() {
		return stateDate;
	}
	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}
	public Integer getClickAmount() {
		return clickAmount;
	}
	public void setClickAmount(Integer clickAmount) {
		this.clickAmount = clickAmount;
	}
	public Integer getSuccessAmount() {
		return successAmount;
	}
	public void setSuccessAmount(Integer successAmount) {
		this.successAmount = successAmount;
	}
}
