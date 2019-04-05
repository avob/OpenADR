package com.avob.openadr.server.common.vtn.models.ven;

import com.avob.openadr.server.common.vtn.models.user.AbstractUserDto;

public class VenDto extends AbstractUserDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6150349820747401714L;

	private String oadrName;

	private String oadrProfil;

	private String transport;

	private String pushUrl;

	private Boolean httpPullModel;

	private Long lastUpdateDatetime;

	private String registrationId;

	private Boolean reportOnly;

	private Boolean xmlSignature;

	public String getOadrName() {
		return oadrName;
	}

	public void setOadrName(String oadrName) {
		this.oadrName = oadrName;
	}

	public String getOadrProfil() {
		return oadrProfil;
	}

	public void setOadrProfil(String oadrProfil) {
		this.oadrProfil = oadrProfil;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public Boolean getHttpPullModel() {
		return httpPullModel;
	}

	public void setHttpPullModel(Boolean httpPullModel) {
		this.httpPullModel = httpPullModel;
	}

	public Long getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Long lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public Boolean getReportOnly() {
		return reportOnly;
	}

	public void setReportOnly(Boolean reportOnly) {
		this.reportOnly = reportOnly;
	}

	public Boolean getXmlSignature() {
		return xmlSignature;
	}

	public void setXmlSignature(Boolean xmlSignature) {
		this.xmlSignature = xmlSignature;
	}
}
