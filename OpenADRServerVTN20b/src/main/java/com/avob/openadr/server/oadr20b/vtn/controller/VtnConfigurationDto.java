package com.avob.openadr.server.oadr20b.vtn.controller;

public class VtnConfigurationDto {
	private String vtnId;

	private Boolean supportPush;

	private Boolean supportUnsecuredHttpPush;

	private Long pullFrequencySeconds;

	private Integer port;

	private String contextPath;

	private String host;

	private String oadrVersion = "OpenADR 20b";
	
	private boolean supportCertificateGeneration = false;
	
	private boolean xsdValidation = false;
	
	private Long xmlSignatureReplayProtectSecond;
	
	private boolean saveVenDate = false;

	public String getVtnId() {
		return vtnId;
	}

	public void setVtnId(String vtnId) {
		this.vtnId = vtnId;
	}

	public Boolean getSupportPush() {
		return supportPush;
	}

	public void setSupportPush(Boolean supportPush) {
		this.supportPush = supportPush;
	}

	public Boolean getSupportUnsecuredHttpPush() {
		return supportUnsecuredHttpPush;
	}

	public void setSupportUnsecuredHttpPush(Boolean supportUnsecuredHttpPush) {
		this.supportUnsecuredHttpPush = supportUnsecuredHttpPush;
	}

	public Long getPullFrequencySeconds() {
		return pullFrequencySeconds;
	}

	public void setPullFrequencySeconds(Long pullFrequencySeconds) {
		this.pullFrequencySeconds = pullFrequencySeconds;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getOadrVersion() {
		return oadrVersion;
	}

	public void setOadrVersion(String oadrVersion) {
		this.oadrVersion = oadrVersion;
	}

	public boolean isSupportCertificateGeneration() {
		return supportCertificateGeneration;
	}

	public void setSupportCertificateGeneration(boolean supportCertificateGeneration) {
		this.supportCertificateGeneration = supportCertificateGeneration;
	}

	public boolean isXsdValidation() {
		return xsdValidation;
	}

	public void setXsdValidation(boolean xsdValidation) {
		this.xsdValidation = xsdValidation;
	}

	public Long getXmlSignatureReplayProtectSecond() {
		return xmlSignatureReplayProtectSecond;
	}

	public void setXmlSignatureReplayProtectSecond(Long xmlSignatureReplayProtectSecond) {
		this.xmlSignatureReplayProtectSecond = xmlSignatureReplayProtectSecond;
	}

	public boolean isSaveVenDate() {
		return saveVenDate;
	}

	public void setSaveVenDate(boolean saveVenDate) {
		this.saveVenDate = saveVenDate;
	}
}
