package com.avob.openadr.server.oadr20b.vtn;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VtnConfig {

	public static final String CONTEXT_PATH_CONF = "oadr.server.context_path";
	public static final String PORT_CONF = "oadr.server.port";
	public static final String TRUSTED_CERTIFICATES_CONF = "oadr.security.ven.trustcertificate";
	public static final String PRIVATE_KEY_CONF = "oadr.security.vtn.key";
	public static final String CERTIFICATE_CONF = "oadr.security.vtn.cert";
	public static final String SUPPORT_PUSH_CONF = "oadr.supportPush";
	public static final String SUPPORT_UNSECURED_PHTTP_PUSH_CONF = "oadr.supportUnsecuredHttpPush";
	public static final String PULL_FREQUENCY_SECONDS_CONF = "oadr.pullFrequencySeconds";
	public static final String HOST_CONF = "oadr.server.host";
	public static final String VALIDATE_OADR_PAYLOAD_XSD_CONF = "oadr.validateOadrPayloadAgainstXsd";
	public static final String VTN_ID_CONF = "oadr.vtnid";
	public static final String SAVE_VEN_UPDATE_REPORT_CONF = "oadr.saveVenData";
	public static final String REPLAY_PROTECTACCEPTED_DELAY_SECONDS_CONF = "oadr.security.replayProtectAcceptedDelaySecond";
	public static final String CA_KEY_CONF = "oadr.security.ca.key";

	@Value("${" + CONTEXT_PATH_CONF + ":#{null}}")
	private String contextPath;

	@Value("${" + PORT_CONF + ":#{8443}}")
	private int port;

	@Value("#{'${" + TRUSTED_CERTIFICATES_CONF + "}'.split(',')}")
	private List<String> trustCertificates;

	@Value("${" + PRIVATE_KEY_CONF + "}")
	private String key;

	@Value("${" + CERTIFICATE_CONF + "}")
	private String cert;

	@Value("${" + SUPPORT_PUSH_CONF + "}")
	private Boolean supportPush;

	@Value("${" + SUPPORT_UNSECURED_PHTTP_PUSH_CONF + "}")
	private Boolean supportUnsecuredHttpPush;

	@Value("${" + PULL_FREQUENCY_SECONDS_CONF + "}")
	private Long pullFrequencySeconds;

	@Value("${" + HOST_CONF + ":localhost}")
	private String host;

	@Value("${" + VALIDATE_OADR_PAYLOAD_XSD_CONF + ":false}")
	private Boolean validateOadrPayloadAgainstXsd;

	@Value("${" + VTN_ID_CONF + "}")
	private String vtnId;

	@Value("${" + SAVE_VEN_UPDATE_REPORT_CONF + "}")
	private Boolean saveVenData;

	@Value("${" + REPLAY_PROTECTACCEPTED_DELAY_SECONDS_CONF + "}")
	private Long replayProtectAcceptedDelaySecond;

	@Value("${"+CA_KEY_CONF+":null}")
	private String ca;

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

	public String getCa() {
		return ca;
	}

	public void setCa(String ca) {
		this.ca = ca;
	}
}
