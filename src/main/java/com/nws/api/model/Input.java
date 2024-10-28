package com.nws.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Input {
	
	@XmlElement(name = "paymentEvent",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private PaymentEvent paymentEvent;

	public PaymentEvent getPaymentEvent() {
		return paymentEvent;
	}

	public void setPaymentEvent(PaymentEvent paymentEvent) {
		this.paymentEvent = paymentEvent;
	}
	
}
