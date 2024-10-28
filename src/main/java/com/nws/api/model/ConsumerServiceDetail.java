package com.nws.api.model;

public class ConsumerServiceDetail {
	
	private long consumerId;
	private long esbServiceId;
	private long esbCategoryId;
	
	private String consumerCode;	
	private String esbServiceCode;
	private String esbCategoryCode;
	private String status;
	private String dataEncryption;
	
	
	public long getConsumerId() {
		return consumerId;
	}
	public void setConsumerId(long consumerId) {
		this.consumerId = consumerId;
	}
	public long getEsbServiceId() {
		return esbServiceId;
	}
	public void setEsbServiceId(long esbServiceId) {
		this.esbServiceId = esbServiceId;
	}
	public long getEsbCategoryId() {
		return esbCategoryId;
	}
	public void setEsbCategoryId(long esbCategoryId) {
		this.esbCategoryId = esbCategoryId;
	}
	public String getConsumerCode() {
		return consumerCode;
	}
	public void setConsumerCode(String consumerCode) {
		this.consumerCode = consumerCode;
	}
	public String getEsbServiceCode() {
		return esbServiceCode;
	}
	public void setEsbServiceCode(String esbServiceCode) {
		this.esbServiceCode = esbServiceCode;
	}
	
	public String getEsbCategoryCode() {
		return esbCategoryCode;
	}
	public void setEsbCategoryCode(String esbCategoryCode) {
		this.esbCategoryCode = esbCategoryCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDataEncryption() {
		return dataEncryption;
	}
	public void setDataEncryption(String dataEncryption) {
		this.dataEncryption = dataEncryption;
	}


}
