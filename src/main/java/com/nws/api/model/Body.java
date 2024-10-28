package com.nws.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://schemas.xmlsoap.org/soap/envelope/",name = "Body")
public class Body {

	@XmlElement(name = "CMAddPayEvt",  namespace = "http://ouaf.oracle.com/webservices/cm/CMAddPayEvt")
	private CMAddPayEvt cmAddPayEvt;

	public CMAddPayEvt getCmAddPayEvt() {
		return cmAddPayEvt;
	}

	public void setCmAddPayEvt(CMAddPayEvt cmAddPayEvt) {
		this.cmAddPayEvt = cmAddPayEvt;
	}

}
