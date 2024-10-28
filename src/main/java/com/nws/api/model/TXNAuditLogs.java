package com.nws.api.model;

import java.sql.Timestamp;

public class TXNAuditLogs {
	
	private String esbServiceCode;
	private String requestId;
	private String consumerCode;
	private String requestUrl;
	private String requestMsg;
	private Timestamp requestTime;
	private Timestamp responseTime;
	private String responseCode;
	private String responseMsg;
	private Timestamp targetRequestTime;
	private String targetRequesMsg;
	private Timestamp targetResponseTime;
	private String targetResponseMsg;
	private String targetResponseCode;
	private String status;
	private String createdDate;
	private String createdBy;
	private String updateDate;
	private String updateBy;
	private String remarks;
	private String isActive;
	private String esbCategoryCode;
	private String requestParams;
	private String esbReqId;
	private String dataEncryption;
	
	public String getDataEncryption() {
		return dataEncryption;
	}
	public void setDataEncryption(String dataEncryption) {
		this.dataEncryption = dataEncryption;
	}
	public String getEsbServiceCode() {
		return esbServiceCode;
	}
	public void setEsbServiceCode(String esbServiceCode) {
		this.esbServiceCode = esbServiceCode;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getConsumerCode() {
		return consumerCode;
	}
	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getRequestMsg() {
		return requestMsg;
	}
	public void setRequestMsg(String requestMsg) {
		this.requestMsg = requestMsg;
	}
	public Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	public Timestamp getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public Timestamp getTargetRequestTime() {
		return targetRequestTime;
	}
	public void setTargetRequestTime(Timestamp targetRequestTime) {
		this.targetRequestTime = targetRequestTime;
	}
	public String getTargetRequesMsg() {
		return targetRequesMsg;
	}
	public void setTargetRequesMsg(String targetRequesMsg) {
		this.targetRequesMsg = targetRequesMsg;
	}
	public Timestamp getTargetResponseTime() {
		return targetResponseTime;
	}
	public void setTargetResponseTime(Timestamp targetResponseTime) {
		this.targetResponseTime = targetResponseTime;
	}
	public String getTargetResponseMsg() {
		return targetResponseMsg;
	}
	public void setTargetResponseMsg(String targetResponseMsg) {
		this.targetResponseMsg = targetResponseMsg;
	}
	public String getTargetResponseCode() {
		return targetResponseCode;
	}
	public void setTargetResponseCode(String httpStatus) {
		this.targetResponseCode = httpStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getEsbCategoryCode() {
		return esbCategoryCode;
	}
	public void setEsbCategoryCode(String esbCategoryCode) {
		this.esbCategoryCode = esbCategoryCode;
	}
	public String getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	public String getEsbReqId() {
		return esbReqId;
	}
	public void setEsbReqId(String esbReqId) {
		this.esbReqId = esbReqId;
	}
	@Override
	public String toString() {
		return "TXNAuditLogs [esbServiceCode=" + esbServiceCode + ", requestId=" + requestId + ", consumerCode="
				+ consumerCode + ", requestUrl=" + requestUrl + ", requestMsg=" + requestMsg + ", requestTime="
				+ requestTime + ", responseTime=" + responseTime + ", responseCode=" + responseCode + ", responseMsg="
				+ responseMsg + ", targetRequestTime=" + targetRequestTime + ", targetRequesMsg=" + targetRequesMsg
				+ ", targetResponseTime=" + targetResponseTime + ", targetResponseMsg=" + targetResponseMsg
				+ ", targetResponseCode=" + targetResponseCode + ", status=" + status + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", updateDate=" + updateDate + ", updateBy=" + updateBy + ", remarks="
				+ remarks + ", isActive=" + isActive + ", esbCategoryCode=" + esbCategoryCode + ", requestParams="
				+ requestParams + ", esbReqId=" + esbReqId + "]";
	}
	
	
	
	



}
