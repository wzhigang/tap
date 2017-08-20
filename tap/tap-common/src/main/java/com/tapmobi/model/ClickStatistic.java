package com.tapmobi.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClickStatistic {
	private String campaingId;
	private volatile int hour;
	private AtomicLong hourSuccess;
	private AtomicLong hourFail;
	private AtomicInteger hourSuccessCounter;
	private AtomicInteger hourFailCounter;
	private AtomicInteger dailySuccess;
	private AtomicInteger dailFail;
	/**
	 * @return the campaingId
	 */
	public String getCampaingId() {
		return campaingId;
	}
	/**
	 * @param campaingId the campaingId to set
	 */
	public void setCampaingId(String campaingId) {
		this.campaingId = campaingId;
	}
	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}
	/**
	 * @param hour the hour to set
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}
	/**
	 * @return the hourSuccess
	 */
	public AtomicLong getHourSuccess() {
		return hourSuccess;
	}
	/**
	 * @param hourSuccess the hourSuccess to set
	 */
	public void setHourSuccess(AtomicLong hourSuccess) {
		this.hourSuccess = hourSuccess;
	}
	/**
	 * @return the hourFail
	 */
	public AtomicLong getHourFail() {
		return hourFail;
	}
	/**
	 * @param hourFail the hourFail to set
	 */
	public void setHourFail(AtomicLong hourFail) {
		this.hourFail = hourFail;
	}
	/**
	 * @return the hourSuccessCounter
	 */
	public AtomicInteger getHourSuccessCounter() {
		return hourSuccessCounter;
	}
	/**
	 * @param hourSuccessCounter the hourSuccessCounter to set
	 */
	public void setHourSuccessCounter(AtomicInteger hourSuccessCounter) {
		this.hourSuccessCounter = hourSuccessCounter;
	}
	/**
	 * @return the hourFailCounter
	 */
	public AtomicInteger getHourFailCounter() {
		return hourFailCounter;
	}
	/**
	 * @param hourFailCounter the hourFailCounter to set
	 */
	public void setHourFailCounter(AtomicInteger hourFailCounter) {
		this.hourFailCounter = hourFailCounter;
	}
	/**
	 * @return the dailySuccess
	 */
	public AtomicInteger getDailySuccess() {
		return dailySuccess;
	}
	/**
	 * @param dailySuccess the dailySuccess to set
	 */
	public void setDailySuccess(AtomicInteger dailySuccess) {
		this.dailySuccess = dailySuccess;
	}
	/**
	 * @return the dailFail
	 */
	public AtomicInteger getDailFail() {
		return dailFail;
	}
	/**
	 * @param dailFail the dailFail to set
	 */
	public void setDailFail(AtomicInteger dailFail) {
		this.dailFail = dailFail;
	}

}
