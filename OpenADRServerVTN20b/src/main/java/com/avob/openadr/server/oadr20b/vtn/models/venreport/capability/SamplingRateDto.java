package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

public class SamplingRateDto {
	private String oadrMaxPeriod;

	private String oadrMinPeriod;

	private boolean oadrOnChange;

	public String getOadrMaxPeriod() {
		return oadrMaxPeriod;
	}

	public void setOadrMaxPeriod(String oadrMaxPeriod) {
		this.oadrMaxPeriod = oadrMaxPeriod;
	}

	public String getOadrMinPeriod() {
		return oadrMinPeriod;
	}

	public void setOadrMinPeriod(String oadrMinPeriod) {
		this.oadrMinPeriod = oadrMinPeriod;
	}

	public boolean isOadrOnChange() {
		return oadrOnChange;
	}

	public void setOadrOnChange(boolean oadrOnChange) {
		this.oadrOnChange = oadrOnChange;
	}
}
