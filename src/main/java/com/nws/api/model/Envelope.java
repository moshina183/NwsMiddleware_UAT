package com.nws.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://schemas.xmlsoap.org/soap/envelope/", name = "Envelope")
public class Envelope {

	@XmlElement(name = "Body", required = true, namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	private Body body;
	
	@XmlElement
	private Object header;
	
	public Object getHeader() {
		return header;
	}

	public void setHeader(Object header) {
		this.header = header;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

}
