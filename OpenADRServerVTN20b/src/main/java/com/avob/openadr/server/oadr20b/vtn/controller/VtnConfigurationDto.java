package com.avob.openadr.server.oadr20b.vtn.controller;

public class VtnConfigurationDto {
	private String vtnId;

	private Boolean supportPush;

	private Boolean supportUnsecuredHttpPush;

	private Integer pullFrequencySeconds;

	private Integer port;

	private String contextPath;

	private String host;

	private String oadrVersion = "OpenADR 20b";

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

	public Integer getPullFrequencySeconds() {
		return pullFrequencySeconds;
	}

	public void setPullFrequencySeconds(Integer pullFrequencySeconds) {
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
}
