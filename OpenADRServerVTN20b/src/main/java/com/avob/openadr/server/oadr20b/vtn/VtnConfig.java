package com.avob.openadr.server.oadr20b.vtn;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VtnConfig {
	@Value("${oadr.server.context_path:#{null}}")
	private String contextPath;

	@Value("${oadr.server.port:#{8443}}")
	private int port;

	@Value("#{'${oadr.security.ven.trustcertificate}'.split(',')}")
	private List<String> trustCertificates;

	@Value("${oadr.security.vtn.key}")
	private String key;

	@Value("${oadr.security.vtn.cert}")
	private String cert;

	@Value("${oadr.supportPush}")
	private Boolean supportPush;

	@Value("${oadr.supportUnsecuredHttpPush}")
	private Boolean supportUnsecuredHttpPush;

	@Value("${oadr.pullFrequencySeconds}")
	private Long pullFrequencySeconds;

	@Value("${oadr.server.host:localhost}")
	private String host;

	@Value("${oadr.validateOadrPayloadAgainstXsd:false}")
	private Boolean validateOadrPayloadAgainstXsd;

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Value("${oadr.saveVenData}")
	private Boolean saveVenData;
	
	@Value("${oadr.security.replayProtectAcceptedDelaySecond}")
	private Long replayProtectAcceptedDelaySecond;

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<String> getTrustCertificates() {
		return trustCertificates;
	}

	public void setTrustCertificates(List<String> trustCertificates) {
		this.trustCertificates = trustCertificates;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Boolean getValidateOadrPayloadAgainstXsd() {
		return validateOadrPayloadAgainstXsd;
	}

	public void setValidateOadrPayloadAgainstXsd(Boolean validateOadrPayloadAgainstXsd) {
		this.validateOadrPayloadAgainstXsd = validateOadrPayloadAgainstXsd;
	}

	public String getVtnId() {
		return vtnId;
	}

	public void setVtnId(String vtnId) {
		this.vtnId = vtnId;
	}

	public Boolean getSaveVenData() {
		return saveVenData;
	}

	public void setSaveVenData(Boolean saveVenData) {
		this.saveVenData = saveVenData;
	}

	public Long getReplayProtectAcceptedDelaySecond() {
		return replayProtectAcceptedDelaySecond;
	}

	public void setReplayProtectAcceptedDelaySecond(Long replayProtectAcceptedDelaySecond) {
		this.replayProtectAcceptedDelaySecond = replayProtectAcceptedDelaySecond;
	}
}
