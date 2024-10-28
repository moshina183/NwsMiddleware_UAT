package com.nws.api.model;

import java.sql.Timestamp;

public class TXNAuditSubRequestsLogs {
	
	private String subEsbServiceCode;
	private String subRequestId;
	private String subParentRequestId;
	private String subConsumerCode;
	private String subRequestUrl;
	private String subRequestMsg;
	private Timestamp subRequestTime;
	private Timestamp subResponseTime;
	private String subResponseCode;
	private String subResponseMsg;
	private Timestamp subTargetRequestTime;
	private String subTargetRequesMsg;
	private Timestamp subTargetResponseTime;
	private String subTargetResponseMsg;
	private String subTargetResponseCode;
	private String subStatus;
	private String subCreatedDate;
	private String subCreatedBy;
	private String subUpdateDate;
	private String subUpdateBy;
	private String subRemarks;
	private String subIsActive;
	private String subEsbCategoryCode;
	private String subRequestParams;
	private String subEsbReqId;
	private String subDataEncryption;
	private Integer callSequence;
	public String getSubEsbServiceCode() {
		return subEsbServiceCode;
	}
	public void setSubEsbServiceCode(String subEsbServiceCode) {
		this.subEsbServiceCode = subEsbServiceCode;
	}
	public String getSubRequestId() {
		return subRequestId;
	}
	public void setSubRequestId(String subRequestId) {
		this.subRequestId = subRequestId;
	}
	public String getSubParentRequestId() {
		return subParentRequestId;
	}
	public void setSubParentRequestId(String subParentRequestId) {
		this.subParentRequestId = subParentRequestId;
	}
	public String getSubConsumerCode() {
		return subConsumerCode;
	}
	public void setSubConsumerCode(String subConsumerCode) {
		this.subConsumerCode = subConsumerCode;
	}
	public String getSubRequestUrl() {
		return subRequestUrl;
	}
	public void setSubRequestUrl(String subRequestUrl) {
		this.subRequestUrl = subRequestUrl;
	}
	public String getSubRequestMsg() {
		return subRequestMsg;
	}
	public void setSubRequestMsg(String subRequestMsg) {
		this.subRequestMsg = subRequestMsg;
	}
	
	public Timestamp getSubRequestTime() {
		return subRequestTime;
	}
	public void setSubRequestTime(Timestamp subRequestTime) {
		this.subRequestTime = subRequestTime;
	}
	public Timestamp getSubResponseTime() {
		return subResponseTime;
	}
	public void setSubResponseTime(Timestamp subResponseTime) {
		this.subResponseTime = subResponseTime;
	}
	public String getSubResponseCode() {
		return subResponseCode;
	}
	public void setSubResponseCode(String httpStatus) {
		this.subResponseCode = httpStatus;
	}
	public String getSubResponseMsg() {
		return subResponseMsg;
	}
	public void setSubResponseMsg(String subResponseMsg) {
		this.subResponseMsg = subResponseMsg;
	}
	public Timestamp getSubTargetRequestTime() {
		return subTargetRequestTime;
	}
	public void setSubTargetRequestTime(Timestamp subTargetRequestTime) {
		this.subTargetRequestTime = subTargetRequestTime;
	}
	public String getSubTargetRequesMsg() {
		return subTargetRequesMsg;
	}
	public void setSubTargetRequesMsg(String subTargetRequesMsg) {
		this.subTargetRequesMsg = subTargetRequesMsg;
	}
	public Timestamp getSubTargetResponseTime() {
		return subTargetResponseTime;
	}
	public void setSubTargetResponseTime(Timestamp subTargetResponseTime) {
		this.subTargetResponseTime = subTargetResponseTime;
	}
	public String getSubTargetResponseMsg() {
		return subTargetResponseMsg;
	}
	public void setSubTargetResponseMsg(String subTargetResponseMsg) {
		this.subTargetResponseMsg = subTargetResponseMsg;
	}
	public String getSubTargetResponseCode() {
		return subTargetResponseCode;
	}
	public void setSubTargetResponseCode(String httpStatus) {
		this.subTargetResponseCode = httpStatus;
	}
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	public String getSubCreatedDate() {
		return subCreatedDate;
	}
	public void setSubCreatedDate(String subCreatedDate) {
		this.subCreatedDate = subCreatedDate;
	}
	public String getSubCreatedBy() {
		return subCreatedBy;
	}
	public void setSubCreatedBy(String subCreatedBy) {
		this.subCreatedBy = subCreatedBy;
	}
	public String getSubUpdateDate() {
		return subUpdateDate;
	}
	public void setSubUpdateDate(String subUpdateDate) {
		this.subUpdateDate = subUpdateDate;
	}
	public String getSubUpdateBy() {
		return subUpdateBy;
	}
	public void setSubUpdateBy(String subUpdateBy) {
		this.subUpdateBy = subUpdateBy;
	}
	public String getSubRemarks() {
		return subRemarks;
	}
	public void setSubRemarks(String subRemarks) {
		this.subRemarks = subRemarks;
	}
	public String getSubIsActive() {
		return subIsActive;
	}
	public void setSubIsActive(String subIsActive) {
		this.subIsActive = subIsActive;
	}
	public String getSubEsbCategoryCode() {
		return subEsbCategoryCode;
	}
	public void setSubEsbCategoryCode(String subEsbCategoryCode) {
		this.subEsbCategoryCode = subEsbCategoryCode;
	}
	public String getSubRequestParams() {
		return subRequestParams;
	}
	public void setSubRequestParams(String subRequestParams) {
		this.subRequestParams = subRequestParams;
	}
	public String getSubEsbReqId() {
		return subEsbReqId;
	}
	public void setSubEsbReqId(String subEsbReqId) {
		this.subEsbReqId = subEsbReqId;
	}
	public String getSubDataEncryption() {
		return subDataEncryption;
	}
	public void setSubDataEncryption(String subDataEncryption) {
		this.subDataEncryption = subDataEncryption;
	}
	public Integer getCallSequence() {
		return callSequence;
	}
	public void setCallSequence(Integer callSequence) {
		this.callSequence = callSequence;
	}
	@Override
	public String toString() {
		return "TXNAuditSubRequestsLogs [subEsbServiceCode=" + subEsbServiceCode + ", subRequestId=" + subRequestId
				+ ", subParentRequestId=" + subParentRequestId + ", subConsumerCode=" + subConsumerCode
				+ ", subRequestUrl=" + subRequestUrl + ", subRequestMsg=" + subRequestMsg + ", sunRequestTime="
				+ subRequestTime + ", subResponseTime=" + subResponseTime + ", subResponseCode=" + subResponseCode
				+ ", subResponseMsg=" + subResponseMsg + ", subTargetRequestTime=" + subTargetRequestTime
				+ ", subTargetRequesMsg=" + subTargetRequesMsg + ", subTargetResponseTime=" + subTargetResponseTime
				+ ", subTargetResponseMsg=" + subTargetResponseMsg + ", subTargetResponseCode=" + subTargetResponseCode
				+ ", subStatus=" + subStatus + ", subCreatedDate=" + subCreatedDate + ", subCreatedBy=" + subCreatedBy
				+ ", subUpdateDate=" + subUpdateDate + ", subUpdateBy=" + subUpdateBy + ", subRemarks=" + subRemarks
				+ ", subIsActive=" + subIsActive + ", subEsbCategoryCode=" + subEsbCategoryCode + ", subRequestParams="
				+ subRequestParams + ", subEsbReqId=" + subEsbReqId + ", subDataEncryption=" + subDataEncryption
				+ ", callSequence=" + callSequence + "]";
	}


}
