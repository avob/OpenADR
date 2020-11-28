package com.avob.openadr.server.oadr20b.ven;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;

public class VtnSessionConfiguration {

	private String sessionId;
	private String vtnId;
	private String vtnUrl;
	private String vtnXmppHost;
	private Integer vtnXmppPort;
	private String vtnXmppDomain;
	private String vtnXmppUser;
	private String vtnXmppPass;
	private String contextPath;
	private int port;
	private String venId;
	private String venIdFile;
	private String venName;
	private String venUrl;
	private List<String> trustCertificates;
	private String venPrivateKeyPath;
	private String venCertificatePath;
	private String basicUsername;
	private String basicPassword;
	private String digestUsername;
	private String digestPassword;
	private String digestRealm;
	private Long pullFrequencySeconds;
	private Boolean reportOnly;
	private Boolean xmlSignature;
	private Boolean pullModel;
	private Long replayProtectAcceptedDelaySecond;
	private SSLContext sslContext;
	private Map<String, OadrReportType> venRegisterReport;

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

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getVenIdFile() {
		return venIdFile;
	}

	public void setVenIdFile(String venIdFile) {
		this.venIdFile = venIdFile;
	}

	public String getVenName() {
		return venName;
	}

	public void setVenName(String venName) {
		this.venName = venName;
	}

	public String getVenUrl() {
		return venUrl;
	}

	public void setVenUrl(String venUrl) {
		this.venUrl = venUrl;
	}

	public List<String> getTrustCertificates() {
		return trustCertificates;
	}

	public void setTrustCertificates(List<String> trustCertificates) {
		this.trustCertificates = trustCertificates;
	}

	public String getVenPrivateKeyPath() {
		return venPrivateKeyPath;
	}

	public void setVenPrivateKeyPath(String venPrivateKeyPath) {
		this.venPrivateKeyPath = venPrivateKeyPath;
	}

	public String getVenCertificatePath() {
		return venCertificatePath;
	}

	public void setVenCertificatePath(String venCertificatePath) {
		this.venCertificatePath = venCertificatePath;
	}

	public String getBasicUsername() {
		return basicUsername;
	}

	public void setBasicUsername(String basicUsername) {
		this.basicUsername = basicUsername;
	}

	public String getBasicPassword() {
		return basicPassword;
	}

	public void setBasicPassword(String basicPassword) {
		this.basicPassword = basicPassword;
	}

	public String getDigestUsername() {
		return digestUsername;
	}

	public void setDigestUsername(String digestUsername) {
		this.digestUsername = digestUsername;
	}

	public String getDigestPassword() {
		return digestPassword;
	}

	public void setDigestPassword(String digestPassword) {
		this.digestPassword = digestPassword;
	}

	public String getDigestRealm() {
		return digestRealm;
	}

	public void setDigestRealm(String digestRealm) {
		this.digestRealm = digestRealm;
	}

	public String getVtnId() {
		return vtnId;
	}

	public void setVtnId(String vtnId) {
		this.vtnId = vtnId;
	}

	public String getVtnUrl() {
		return vtnUrl;
	}

	public void setVtnUrl(String vtnUrl) {
		this.vtnUrl = vtnUrl;
	}

	public boolean isDigestAuthenticationConfigured() {
		return this.getDigestUsername() != null && this.getDigestPassword() != null;
	}

	public boolean isBasicAuthenticationConfigured() {
		return this.getBasicUsername() != null && this.getBasicPassword() != null;
	}

	public String getVtnXmppHost() {
		return vtnXmppHost;
	}

	public void setVtnXmppHost(String vtnXmpphost) {
		this.vtnXmppHost = vtnXmpphost;
	}

	public Integer getVtnXmppPort() {
		return vtnXmppPort;
	}

	public void setVtnXmppPort(Integer vtnXmppPort) {
		this.vtnXmppPort = vtnXmppPort;
	}

	public String getVtnXmppUser() {
		return vtnXmppUser;
	}

	public void setVtnXmppUser(String vtnXmppUser) {
		this.vtnXmppUser = vtnXmppUser;
	}

	public String getVtnXmppPass() {
		return vtnXmppPass;
	}

	public void setVtnXmppPass(String vtnXmppPass) {
		this.vtnXmppPass = vtnXmppPass;
	}

	public String getVtnXmppDomain() {
		return vtnXmppDomain;
	}

	public void setVtnXmppDomain(String vtnXmppDomain) {
		this.vtnXmppDomain = vtnXmppDomain;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getSessionKey() {
		return vtnId + venUrl;
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

	public Long getReplayProtectAcceptedDelaySecond() {
		return replayProtectAcceptedDelaySecond;
	}

	public void setReplayProtectAcceptedDelaySecond(Long replayProtectAcceptedDelaySecond) {
		this.replayProtectAcceptedDelaySecond = replayProtectAcceptedDelaySecond;
	}

	public Map<String, OadrReportType> getVenRegisterReport() {
		return venRegisterReport;
	}

	public Optional<OadrReportType> getReport(String reportSpecifierId) {

		Optional<OadrReportType> desc = Optional.empty();
		OadrReportType oadrReportType = this.getVenRegisterReport().get(reportSpecifierId);
		if (oadrReportType != null) {
			desc = Optional.of(oadrReportType);
		}
		return desc;
	}

	public Optional<OadrReportDescriptionType> getReportDescription(String reportSpecifierId, String rid) {

		Optional<OadrReportDescriptionType> desc = Optional.empty();
		OadrReportType oadrReportType = this.getVenRegisterReport().get(reportSpecifierId);
		if (oadrReportType != null) {
			List<OadrReportDescriptionType> collect = oadrReportType.getOadrReportDescription().stream()
					.filter(description -> rid.equals(description.getRID())).collect(Collectors.toList());
			if (collect.size() == 1) {
				desc = Optional.of(collect.get(0));
			}
		}
		return desc;
	}

	public void setVenRegisterReport(Map<String, OadrReportType> venRegisterReport) {
		this.venRegisterReport = venRegisterReport;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
