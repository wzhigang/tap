package com.tapmobi.model;

public class CandidateOffer implements Comparable<CandidateOffer>{
		private int uid;
		private String offerId;
		private String name;
		private int affiliateId;
		private String affiliateName;
		private String createDate;
		private int requireApproval;
		private String termsAndConditions;
		private String previewUrl;
		private String currency;
		private float defaultPayout;
		private String status;
		private String expirationDate;
		private String payoutType;
		private float percentPayout;
		private int conversionCap;
		private int monthlyConversionCap;
		private float payOutCap;
		private float monthlyPayOutCap;
		private String defaultGoalName;
		private int useTargetRules;
		private int isExpired;
		private String apprvoalStatus;
		private String osName;
		private String osVersion;
		private String platform;
		private String country;
		private int rank;
		private int yesterdayRank;
		private int lastWeekRank;
		private String rankCountry;
		private String rankPlatform;
		private String clickId;
		private String trackUrl;
		private String appId;
		private String appName;
		private String appStoreUrl;
		private String appIcon;
		private String minOS;
		private int priority;
		private float dailyBudget;
		private int dailyMaxClicks;
		private int rtbRunning;
		private byte isReachalbe;
		private int latency;
		private String campaignId;
		private String placements;

		public CandidateOffer() {
		}
		public CandidateOffer(String clickId) {
			this.clickId = clickId;
		}
		/**
		 * @return the uid
		 */
		public int getUid() {
			return uid;
		}
		/**
		 * @param uid the uid to set
		 */
		public void setUid(int uid) {
			this.uid = uid;
		}
		/**
		 * @return the id
		 */
		public String getId() {
			return offerId;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.offerId = id;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
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
		 * @return the affiliateName
		 */
		public String getAffiliateName() {
			return affiliateName;
		}
		/**
		 * @param affiliateName the affiliateName to set
		 */
		public void setAffiliateName(String affiliateName) {
			this.affiliateName = affiliateName;
		}
		/**
		 * @return the createDate
		 */
		public String getCreateDate() {
			return createDate;
		}
		/**
		 * @param createDate the createDate to set
		 */
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}
		/**
		 * @return the requireApproval
		 */
		public int getRequireApproval() {
			return requireApproval;
		}
		/**
		 * @param requireApproval the requireApproval to set
		 */
		public void setRequireApproval(int requireApproval) {
			this.requireApproval = requireApproval;
		}
		/**
		 * @return the termsAndConditions
		 */
		public String getTermsAndConditions() {
			return termsAndConditions;
		}
		/**
		 * @param termsAndConditions the termsAndConditions to set
		 */
		public void setTermsAndConditions(String termsAndConditions) {
			this.termsAndConditions = termsAndConditions;
		}
		/**
		 * @return the previewUrl
		 */
		public String getPreviewUrl() {
			return previewUrl;
		}
		/**
		 * @param previewUrl the previewUrl to set
		 */
		public void setPreviewUrl(String previewUrl) {
			this.previewUrl = previewUrl;
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
		 * @return the defaultPayout
		 */
		public float getDefaultPayout() {
			return defaultPayout;
		}
		/**
		 * @param defaultPayout the defaultPayout to set
		 */
		public void setDefaultPayout(float defaultPayout) {
			this.defaultPayout = defaultPayout;
		}
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		/**
		 * @return the expirationDate
		 */
		public String getExpirationDate() {
			return expirationDate;
		}
		/**
		 * @param expirationDate the expirationDate to set
		 */
		public void setExpirationDate(String expirationDate) {
			this.expirationDate = expirationDate;
		}
		/**
		 * @return the payoutType
		 */
		public String getPayoutType() {
			return payoutType;
		}
		/**
		 * @param payoutType the payoutType to set
		 */
		public void setPayoutType(String payoutType) {
			this.payoutType = payoutType;
		}
		/**
		 * @return the percentPayout
		 */
		public float getPercentPayout() {
			return percentPayout;
		}
		/**
		 * @param percentPayout the percentPayout to set
		 */
		public void setPercentPayout(float percentPayout) {
			this.percentPayout = percentPayout;
		}
		/**
		 * @return the conversionCap
		 */
		public int getConversionCap() {
			return conversionCap;
		}
		/**
		 * @param conversionCap the conversionCap to set
		 */
		public void setConversionCap(int conversionCap) {
			this.conversionCap = conversionCap;
		}
		/**
		 * @return the monthlyConversionCap
		 */
		public int getMonthlyConversionCap() {
			return monthlyConversionCap;
		}
		/**
		 * @param monthlyConversionCap the monthlyConversionCap to set
		 */
		public void setMonthlyConversionCap(int monthlyConversionCap) {
			this.monthlyConversionCap = monthlyConversionCap;
		}
		/**
		 * @return the payOutCap
		 */
		public float getPayOutCap() {
			return payOutCap;
		}
		/**
		 * @param payOutCap the payOutCap to set
		 */
		public void setPayOutCap(float payOutCap) {
			this.payOutCap = payOutCap;
		}
		/**
		 * @return the monthlyPayOutCap
		 */
		public float getMonthlyPayOutCap() {
			return monthlyPayOutCap;
		}
		/**
		 * @param monthlyPayOutCap the monthlyPayOutCap to set
		 */
		public void setMonthlyPayOutCap(float monthlyPayOutCap) {
			this.monthlyPayOutCap = monthlyPayOutCap;
		}
		/**
		 * @return the defaultGoalName
		 */
		public String getDefaultGoalName() {
			return defaultGoalName;
		}
		/**
		 * @param defaultGoalName the defaultGoalName to set
		 */
		public void setDefaultGoalName(String defaultGoalName) {
			this.defaultGoalName = defaultGoalName;
		}
		/**
		 * @return the useTargetRules
		 */
		public int getUseTargetRules() {
			return useTargetRules;
		}
		/**
		 * @param useTargetRules the useTargetRules to set
		 */
		public void setUseTargetRules(int useTargetRules) {
			this.useTargetRules = useTargetRules;
		}
		/**
		 * @return the isExpired
		 */
		public int getIsExpired() {
			return isExpired;
		}
		/**
		 * @param isExpired the isExpired to set
		 */
		public void setIsExpired(int isExpired) {
			this.isExpired = isExpired;
		}
		/**
		 * @return the apprvoalStatus
		 */
		public String getApprvoalStatus() {
			return apprvoalStatus;
		}
		/**
		 * @param apprvoalStatus the apprvoalStatus to set
		 */
		public void setApprvoalStatus(String apprvoalStatus) {
			this.apprvoalStatus = apprvoalStatus;
		}
		
		/**
		 * @return the rank
		 */
		public int getRank() {
			return rank;
		}
		/**
		 * @param rank the rank to set
		 */
		public void setRank(int rank) {
			this.rank = rank;
		}
		/**
		 * @return the yesterdayRank
		 */
		public int getYesterdayRank() {
			return yesterdayRank;
		}
		/**
		 * @param yesterdayRank the yesterdayRank to set
		 */
		public void setYesterdayRank(int yesterdayRank) {
			this.yesterdayRank = yesterdayRank;
		}
		/**
		 * @return the lastWeekRank
		 */
		public int getLastWeekRank() {
			return lastWeekRank;
		}
		/**
		 * @param lastWeekRank the lastWeekRank to set
		 */
		public void setLastWeekRank(int lastWeekRank) {
			this.lastWeekRank = lastWeekRank;
		}
		/**
		 * @return the rankCountry
		 */
		public String getRankCountry() {
			return rankCountry;
		}
		/**
		 * @param rankCountry the rankCountry to set
		 */
		public void setRankCountry(String rankCountry) {
			this.rankCountry = rankCountry;
		}
		/**
		 * @return the rankPlatform
		 */
		public String getRankPlatform() {
			return rankPlatform;
		}
		/**
		 * @param rankPlatform the rankPlatform to set
		 */
		public void setRankPlatform(String rankPlatform) {
			this.rankPlatform = rankPlatform;
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
		 * @return the osName
		 */
		public String getOsName() {
			return osName;
		}
		/**
		 * @param osName the osName to set
		 */
		public void setOsName(String osName) {
			this.osName = osName;
		}
		/**
		 * @return the osVersion
		 */
		public String getOsVersion() {
			return osVersion;
		}
		/**
		 * @param osVersion the osVersion to set
		 */
		public void setOsVersion(String osVersion) {
			this.osVersion = osVersion;
		}
		/**
		 * @return the platform
		 */
		public String getPlatform() {
			return platform;
		}
		/**
		 * @param platform the platform to set
		 */
		public void setPlatform(String platform) {
			this.platform = platform;
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
		 * @return the appName
		 */
		public String getAppName() {
			return appName;
		}
		/**
		 * @param appName the appName to set
		 */
		public void setAppName(String appName) {
			this.appName = appName;
		}
		/**
		 * @return the dailyBudget
		 */
		public float getDailyBudget() {
			return dailyBudget;
		}
		/**
		 * @param dailyBudget the dailyBudget to set
		 */
		public void setDailyBudget(float dailyBudget) {
			this.dailyBudget = dailyBudget;
		}
		/**
		 * @return the dailyMaxClicks
		 */
		public int getDailyMaxClicks() {
			return dailyMaxClicks;
		}
		/**
		 * @param dailyMaxClicks the dailyMaxClicks to set
		 */
		public void setDailyMaxClicks(int dailyMaxClicks) {
			this.dailyMaxClicks = dailyMaxClicks;
		}
		/**
		 * @return the appStoreUrl
		 */
		public String getAppStoreUrl() {
			return appStoreUrl;
		}
		/**
		 * @param appStoreUrl the appStoreUrl to set
		 */
		public void setAppStoreUrl(String appStoreUrl) {
			this.appStoreUrl = appStoreUrl;
		}
		/**
		 * @return the appIcon
		 */
		public String getAppIcon() {
			return appIcon;
		}
		/**
		 * @param appIcon the appIcon to set
		 */
		public void setAppIcon(String appIcon) {
			this.appIcon = appIcon;
		}
		/**
		 * @return the minOS
		 */
		public String getMinOS() {
			return minOS;
		}
		/**
		 * @param minOS the minOS to set
		 */
		public void setMinOS(String minOS) {
			this.minOS = minOS;
		}
		/**
		 * @return the priority
		 */
		public int getPriority() {
			return priority;
		}
		/**
		 * @param priority the priority to set
		 */
		public void setPriority(int priority) {
			this.priority = priority;
		}
		
		/**
		 * @return the offerId
		 */
		public String getOfferId() {
			return offerId;
		}
		/**
		 * @param offerId the offerId to set
		 */
		public void setOfferId(String offerId) {
			this.offerId = offerId;
		}
		/**
		 * @return the rtbRunning
		 */
		public int getRtbRunning() {
			return rtbRunning;
		}
		/**
		 * @param rtbRunning the rtbRunning to set
		 */
		public void setRtbRunning(int rtbRunning) {
			this.rtbRunning = rtbRunning;
		}
		/**
		 * @return the isReachalbe
		 */
		public byte getIsReachalbe() {
			return isReachalbe;
		}
		/**
		 * @param isReachalbe the isReachalbe to set
		 */
		public void setIsReachalbe(byte isReachalbe) {
			this.isReachalbe = isReachalbe;
		}
		/**
		 * @return the latency
		 */
		public int getLatency() {
			return latency;
		}
		/**
		 * @param latency the latency to set
		 */
		public void setLatency(int latency) {
			this.latency = latency;
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
		 * @return the placements
		 */
		public String getPlacements() {
			return placements;
		}
		/**
		 * @param placements the placements to set
		 */
		public void setPlacements(String placements) {
			this.placements = placements;
		}
		@Override
		public boolean equals(Object obj){
			if (obj == this)
				return true;
			if (obj instanceof CandidateOffer){
				CandidateOffer c = (CandidateOffer) obj;
				if (c.getUid()!=0 && this.getUid()!=0)
					return c.getUid()==this.getUid();
				else
					return c.getAffiliateId()==this.getAffiliateId() && c.getId()==this.getId();
			}
			return false;
		}

		@ Override
		public int hashCode(){
			return new String(this.getAffiliateId()+"-"+this.getId()).hashCode();
		}
		@Override
		public String toString(){
			StringBuffer sb = new StringBuffer();
//			sb.append(this.getId()).append(Message.delimiter);
//			sb.append(this.getAffiliateId()).append(Message.delimiter);
//			sb.append(this.getTrackUrl()).append(Message.delimiter);
//			sb.append(this.getPayoutType()).append(Message.delimiter);
//			sb.append(this.getDefaultPayout()).append(Message.delimiter);
//			sb.append(this.getCurrency()).append(Message.delimiter);
//			sb.append(this.getConversionCap()).append(Message.delimiter);
//			sb.append(this.getAppId()).append(Message.delimiter);
//			sb.append(this.getAppName()).append(Message.delimiter);
//			sb.append(this.getAppStoreUrl()).append(Message.delimiter);
//			sb.append(this.getRank()).append(Message.delimiter);
//			sb.append(this.getLastWeekRank()).append(Message.delimiter);
//			sb.append(this.getYesterdayRank()).append(Message.delimiter);
//			sb.append(this.getRankCountry()).append(Message.delimiter);
//			sb.append(this.getRankPlatform()).append(Message.delimiter);
			sb.append(this.getAffiliateId()).append(Message.delimiter);
			sb.append(this.getId()).append(Message.delimiter);
			sb.append(this.getPriority()).append(Message.delimiter);
			sb.append(this.getRank()).append(Message.delimiter);
			sb.append(this.getDefaultPayout()).append(Message.delimiter);
			return sb.toString();
		}
		@Override
		public int compareTo(CandidateOffer o) {
			if (o.getPriority()==priority){
				if (o.getRank()==rank)
					return new Float((o.getDefaultPayout()-defaultPayout)*100).intValue();
				else
					return o.getRank()>rank?-1:1;
					
			}else
				return o.getPriority()-priority;
		}
	}
