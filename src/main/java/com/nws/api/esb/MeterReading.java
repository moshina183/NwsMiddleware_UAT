package com.nws.api.esb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeterReading {
	
	@JsonProperty("LEGACY_ID")
	private String LEGACY_ID;
	
	@JsonProperty("DATE_FROM")
	private String DATE_FROM;
	
	@JsonProperty("DATE_TO")
	private String DATE_TO;
	
	@JsonProperty("FREQUENCY")
	private String FREQUENCY;
	public String getLEGACY_ID() {
		return LEGACY_ID;
	}
	public void setLEGACY_ID(String lEGACY_ID) {
		LEGACY_ID = lEGACY_ID;
	}
	public String getDATE_FROM() {
		return DATE_FROM;
	}
	public void setDATE_FROM(String dATE_FROM) {
		DATE_FROM = dATE_FROM;
	}
	public String getDATE_TO() {
		return DATE_TO;
	}
	public void setDATE_TO(String dATE_TO) {
		DATE_TO = dATE_TO;
	}
	public String getFREQUENCY() {
		return FREQUENCY;
	}
	public void setFREQUENCY(String fREQUENCY) {
		FREQUENCY = fREQUENCY;
	}
	public MeterReading() {
		super();
	}
	public MeterReading(String lEGACY_ID, String dATE_FROM, String dATE_TO, String fREQUENCY) {
		super();
		LEGACY_ID = lEGACY_ID;
		DATE_FROM = dATE_FROM;
		DATE_TO = dATE_TO;
		FREQUENCY = fREQUENCY;
	}
	@Override
	public String toString() {
		return "MeterReading [LEGACY_ID=" + LEGACY_ID + ", DATE_FROM=" + DATE_FROM + ", DATE_TO=" + DATE_TO
				+ ", FREQUENCY=" + FREQUENCY + "]";
	}


}
