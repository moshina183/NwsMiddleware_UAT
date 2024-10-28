package com.nws.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PaymentEvent {

	@XmlElement(name = "payorAccountId",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private double payorAccountId;
	@XmlElement(name = "serviceProviderId",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private String serviceProviderId;
	@XmlElement(name = "paymentAmount",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private double paymentAmount;
	@XmlElement(name = "paymentDate",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private Date paymentDate;
	@XmlElement(name = "matchType",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private Object matchType;
	@XmlElement(name = "matchValue",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private Object matchValue;
	@XmlElement(name = "paymentSource",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private String paymentSource;
	@XmlElement(name = "paymentStation",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private String paymentStation;
	@XmlElement(name = "paymentReference",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private double paymentReference;
	@XmlElement(name = "RRN",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private double RRN;
	@XmlElement(name = "notes",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private Object notes;
	@XmlElement(name = "tenderAuthorizationCode",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private String tenderAuthorizationCode;
	@XmlElement(name = "paymentMethod",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private String paymentMethod;
	
	public double getPayorAccountId() {
		return payorAccountId;
	}
	public void setPayorAccountId(double payorAccountId) {
		this.payorAccountId = payorAccountId;
	}
	public String getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
	public double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Object getMatchType() {
		return matchType;
	}
	public void setMatchType(Object matchType) {
		this.matchType = matchType;
	}
	public Object getMatchValue() {
		return matchValue;
	}
	public void setMatchValue(Object matchValue) {
		this.matchValue = matchValue;
	}
	public String getPaymentSource() {
		return paymentSource;
	}
	public void setPaymentSource(String paymentSource) {
		this.paymentSource = paymentSource;
	}
	public String getPaymentStation() {
		return paymentStation;
	}
	public void setPaymentStation(String paymentStation) {
		this.paymentStation = paymentStation;
	}
	public double getPaymentReference() {
		return paymentReference;
	}
	public void setPaymentReference(double paymentReference) {
		this.paymentReference = paymentReference;
	}
	public double getRRN() {
		return RRN;
	}
	public void setRRN(double rRN) {
		RRN = rRN;
	}
	public Object getNotes() {
		return notes;
	}
	public void setNotes(Object notes) {
		this.notes = notes;
	}
	public String getTenderAuthorizationCode() {
		return tenderAuthorizationCode;
	}
	public void setTenderAuthorizationCode(String tenderAuthorizationCode) {
		this.tenderAuthorizationCode = tenderAuthorizationCode;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
}
