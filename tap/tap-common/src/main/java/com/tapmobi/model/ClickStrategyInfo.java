package com.tapmobi.model;

import java.util.List;
import java.util.Map;

public class ClickStrategyInfo {
	private String campaignId;
	//所在国家的流量趋势，key:时段(按小时分)，value:流量占比
	private Map<String,Float> trafficTrends;
	//所在国家的分时段的下载转化趋势，key:时段(按小时分)，value:转化占比
	private Map<String,Float> cvrTrends;
	//过去一周平均cvr
	private float cvr;
	//过去一周平均activate数量
	private int activates;
	private boolean isReachable;
	private int redirectionNumber;
	private Map<Integer,String> redirectRecords;
	private List redirectResults;
	private int avgLatency;
	/**
	 * @return the compaignId
	 */
	public String getCampaignId() {
		return campaignId;
	}
	/**
	 * @param compaignId the compaignId to set
	 */
	public void setCampaignId(String compaignId) {
		this.campaignId = compaignId;
	}
	/**
	 * @return the trafficTrends
	 */
	public Map<String, Float> getTrafficTrends() {
		return trafficTrends;
	}
	/**
	 * @param trafficTrends the trafficTrends to set
	 */
	public void setTrafficTrends(Map<String, Float> trafficTrends) {
		this.trafficTrends = trafficTrends;
	}
	/**
	 * @return the cvrTrends
	 */
	public Map<String, Float> getCvrTrends() {
		return cvrTrends;
	}
	/**
	 * @param cvrTrends the cvrTrends to set
	 */
	public void setCvrTrends(Map<String, Float> cvrTrends) {
		this.cvrTrends = cvrTrends;
	}
	/**
	 * @return the cvr
	 */
	public float getCvr() {
		return cvr;
	}
	/**
	 * @param cvr the cvr to set
	 */
	public void setCvr(float cvr) {
		this.cvr = cvr;
	}
	/**
	 * @return the activates
	 */
	public int getActivates() {
		return activates;
	}
	/**
	 * @param activates the activates to set
	 */
	public void setActivates(int activates) {
		this.activates = activates;
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
	 * @return the redirectionNumber
	 */
	public int getRedirectionNumber() {
		return redirectionNumber;
	}
	/**
	 * @param redirectionNumber the redirectionNumber to set
	 */
	public void setRedirectionNumber(int redirectionNumber) {
		this.redirectionNumber = redirectionNumber;
	}
	/**
	 * @return the redirectRecords
	 */
	public Map<Integer, String> getRedirectRecords() {
		return redirectRecords;
	}
	/**
	 * @param redirectRecords the redirectRecords to set
	 */
	public void setRedirectRecords(Map<Integer, String> redirectRecords) {
		this.redirectRecords = redirectRecords;
	}
	/**
	 * @return the avgLatency
	 */
	public int getAvgLatency() {
		return avgLatency;
	}
	/**
	 * @param avgLatency the avgLatency to set
	 */
	public void setAvgLatency(int avgLatency) {
		this.avgLatency = avgLatency;
	}
	/**
	 * @return the redirectResults
	 */
	public List getRedirectResults() {
		return redirectResults;
	}
	/**
	 * @param redirectResults the redirectResults to set
	 */
	public void setRedirectResults(List redirectResults) {
		this.redirectResults = redirectResults;
	}

}
