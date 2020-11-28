package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import javax.persistence.Embeddable;

@Embeddable
public class SamplingRate {

	private String oadrMaxPeriod;

	private String oadrMinPeriod;

	private Boolean oadrOnChange;

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

	public Boolean isOadrOnChange() {
		return oadrOnChange;
	}

	public void setOadrOnChange(Boolean oadrOnChange) {
		this.oadrOnChange = oadrOnChange;
	}

}
