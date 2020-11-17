package com.avob.openadr.server.oadr20b.ven;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VenConfig {

	@Value("${oadr.pullFrequencySeconds:#{null}}")
	private Long pullFrequencySeconds;

	@Value("${oadr.reportOnly}")
	private Boolean reportOnly;

	@Value("${oadr.xmlSignature}")
	private Boolean xmlSignature;

	@Value("${oadr.pullModel}")
	private Boolean pullModel;

	@Value("${oadr.security.replayProtectAcceptedDelaySecond:#{null}}")
	private Long replayProtectAcceptedDelaySecond;

	@Value("${oadr.security.validateOadrPayloadAgainstXsdFilePath:#{null}}")
	private String validateOadrPayloadAgainstXsdFilePath;

	public static final String VALIDATE_OADR_PAYLOAD_XSD_CONF = "oadr.validateOadrPayloadAgainstXsd";
	public static final String VALIDATE_OADR_PAYLOAD_XSD_FILEPATH_CONF = "oadr.security.validateOadrPayloadAgainstXsdFilePath";

	public VenConfig() {
	}

	public VenConfig(VenConfig clone) {
		this.pullFrequencySeconds = clone.getPullFrequencySeconds();
		this.pullModel = clone.getPullModel();
		this.replayProtectAcceptedDelaySecond = clone.getReplayProtectAcceptedDelaySecond();
		this.reportOnly = clone.getReportOnly();
		this.xmlSignature = clone.getXmlSignature();
		this.validateOadrPayloadAgainstXsdFilePath = clone.getValidateOadrPayloadAgainstXsdFilePath();
	}

	public Long getPullFrequencySeconds() {
		return pullFrequencySeconds;
	}

	public Boolean getReportOnly() {
		return reportOnly;
	}

	public Boolean getXmlSignature() {
		return xmlSignature;
	}

	public Boolean getPullModel() {
		return pullModel;
	}

	public Long getReplayProtectAcceptedDelaySecond() {
		return replayProtectAcceptedDelaySecond;
	}

	public String getValidateOadrPayloadAgainstXsdFilePath() {
		return validateOadrPayloadAgainstXsdFilePath;
	}


}
