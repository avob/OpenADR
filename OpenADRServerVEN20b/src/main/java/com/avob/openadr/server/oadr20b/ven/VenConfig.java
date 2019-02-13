package com.avob.openadr.server.oadr20b.ven;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VenConfig {

	@Value("${oadr.venid}")
	private String venId;

	@Value("${oadr.venName:#{null}}")
	private String venName;

	@Value("${oadr.pullFrequencySeconds:#{null}}")
	private Long pullFrequencySeconds;

	@Value("${oadr.reportOnly}")
	private Boolean reportOnly;

	@Value("${oadr.xmlSignature}")
	private Boolean xmlSignature;

	@Value("${oadr.pullModel}")
	private Boolean pullModel;

	@Value("${oadr.venUrl:#{null}}")
	private String venUrl;

	@Value("#{'${oadr.security.vtn.trustcertificate}'.split(',')}")
	private List<String> trustCertificates;

	@Value("${oadr.security.ven.key:#{null}}")
	private String venPrivateKeyPath;

	@Value("${oadr.security.ven.cert:#{null}}")
	private String venCertificatePath;

	@Value("${oadr.security.authentication.basic.username:#{null}}")
	private String basicUsername;

	@Value("${oadr.security.authentication.basic.password:#{null}}")
	private String basicPassword;

	@Value("${oadr.security.authentication.digest.username:#{null}}")
	private String digestUsername;

	@Value("${oadr.security.authentication.digest.password:#{null}}")
	private String digestPassword;

	@Value("${oadr.security.replayProtectAcceptedDelaySecond}")
	private Long replayProtectAcceptedDelaySecond;

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getVenName() {
		return venName;
	}

	public void setVenName(String venName) {
		this.venName = venName;
	}

	public Long getPullFrequencySeconds() {
		return pullFrequencySeconds;
	}

	public void setPullFrequencySeconds(Long pullFrequencySeconds) {
		this.pullFrequencySeconds = pullFrequencySeconds;
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

	public Boolean getPullModel() {
		return pullModel;
	}

	public void setPullModel(Boolean pullModel) {
		this.pullModel = pullModel;
	}

	public String getVenUrl() {
		return venUrl;
	}

	public void setVenUrl(String venUrl) {
		this.venUrl = venUrl;
	}

	public boolean isBasicAuthenticationConfigured() {
		return basicUsername != null && basicPassword != null;
	}

	public boolean isDigestAuthenticationConfigured() {
		return digestUsername != null && digestPassword != null;
	}

	public Map<String, String> getVtnTrustCertificate() {
		Map<String, String> trustedCertificates = new HashMap<String, String>();
		int i = 0;
		for (String path : trustCertificates) {
			trustedCertificates.put("cert_" + (i++), path);
		}
		return trustedCertificates;
	}

	public String getBasicUsername() {
		return basicUsername;
	}

	public String getBasicPassword() {
		return basicPassword;
	}

	public String getDigestUsername() {
		return digestUsername;
	}

	public String getDigestPassword() {
		return digestPassword;
	}

	public String getVenPrivateKeyPath() {
		return venPrivateKeyPath;
	}

	public String getVenCertificatePath() {
		return venCertificatePath;
	}

	public Long getReplayProtectAcceptedDelaySecond() {
		return replayProtectAcceptedDelaySecond;
	}

}
