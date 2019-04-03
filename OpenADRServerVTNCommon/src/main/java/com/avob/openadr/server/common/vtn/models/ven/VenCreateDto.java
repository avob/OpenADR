package com.avob.openadr.server.common.vtn.models.ven;

import com.avob.openadr.server.common.vtn.models.user.AbstractUserCreateDto;

public class VenCreateDto extends AbstractUserCreateDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3250807214482052191L;

	private String oadrName;

	private String oadrProfil;

	private String transport;

	private String pushUrl;

	private Boolean httpPullModel;

	private String authenticationType;

	private String needCertificateGeneration;

	private String commonName;

	public VenCreateDto() {
	}

	public VenCreateDto(String username) {
		this.setUsername(username);
	}

	public String getOadrName() {
		return oadrName;
	}

	public void setOadrName(String name) {
		this.oadrName = name;
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

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getNeedCertificateGeneration() {
		return needCertificateGeneration;
	}

	public void setNeedCertificateGeneration(String needCertificateGeneration) {
		this.needCertificateGeneration = needCertificateGeneration;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public Boolean getHttpPullModel() {
		return httpPullModel;
	}

	public void setHttpPullModel(Boolean httpPullModel) {
		this.httpPullModel = httpPullModel;
	}

}
