package com.tapmobi.common.util;

import com.tapmobi.common.util.UUIDGenerator;
import com.tapmobi.model.CandidateOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tapmobi.model.ClickReportLog;

public class ClickInfoCompositor {
	Logger logger = LoggerFactory.getLogger(ClickInfoCompositor.class);
	public ClickInfoCompositor() {
	}
	
	public String generateClickId(){
		return UUIDGenerator.getUUID();
	}
	/**
	 * the implementation will vary from different affiliate 
	 * @param offer
	 * @param deviceId
	 * @return
	 */
	public String composit(ClickReportLog clickReport,String deviceId){
		StringBuffer clickInfo= new StringBuffer();
		clickInfo.append(clickReport.getTrackUrl()).append("?");
		clickInfo.append("deviceId=").append(deviceId);
		clickInfo.append("&subid1=");
		String clickId = generateClickId();
		clickInfo.append(clickId);
		clickInfo.append("&subid2=");
		clickInfo.append(clickReport.getId());
		return clickInfo.toString();
	}
	
}
