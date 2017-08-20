package com.tapmobi.model;

import com.tapmobi.common.util.DateTimeUtil;

public class Message {
	protected long timestamp=System.currentTimeMillis();
	protected String datetimeStr = DateTimeUtil.getUserDate("yyyyMMddhhmmss");
	public static final String delimiter = "@@";
	public static final String logDelimiter = "\u0001";
	public static final String innerDelimiter="*";
	public static final String NotApplicable = "NA";
	
	protected int id;
	//msg format：channel$$osName$$osVersion$$&platform$$&country$$&city$$&timestamp
	protected String msg;
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}


	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the datetimeStr
	 */
	public String getDatetimeStr() {
		return datetimeStr;
	}

	/**
	 * @param datetimeStr the datetimeStr to set
	 */
	public void setDatetimeStr(String datetimeStr) {
		this.datetimeStr = datetimeStr;
	}

	public Message() {
	}

}
