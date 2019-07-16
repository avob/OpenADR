package com.avob.openadr.server.oadr20b.ven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VenConfig implements Cloneable {

	@Value("${oadr.venid:#{null}}")
	private String venId;

	@Value("${oadr.venid.file:#{null}}")
	private String venIdFile;

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

	@Value("${oadr.security.authentication.digest.realm:#{null}}")
	private String digestRealm;

	@Value("${oadr.security.replayProtectAcceptedDelaySecond}")
	private Long replayProtectAcceptedDelaySecond;

	@PostConstruct
	public void init() {
		if (venId == null && venIdFile == null || venId != null && venIdFile != null) {
			throw new IllegalArgumentException("oadr.venid or oadr.venid.file must be configured");
		} else if (venIdFile != null) {
			// set venId by reading venIdFile path first line
			Path path = Paths.get(venIdFile);
			File file = path.toFile();
			if (!file.exists()) {
				throw new IllegalArgumentException(
						"oadr.venid.must be a valid file path containing venId as it's first line");
			}
			try (Stream<String> lines = Files.lines(path);) {
				Optional<String> findFirst = lines.findFirst();
				if (!findFirst.isPresent()) {
					throw new IllegalArgumentException(
							"oadr.venid.must be a valid file path containing venId as it's first line");

				}
				venId = findFirst.get().trim();
			} catch (IOException e) {
				throw new IllegalArgumentException(
						"oadr.venid.must be a valid file path containing venId as it's first line", e);

			}
		}
	}

	public String getVenId() {
		return venId;
	}

	public String getVenName() {
		return venName;
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

	public String getVenUrl() {
		return venUrl;
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

	public void setBasicUsername(String basicUsername) {
		this.basicUsername = basicUsername;
	}

	public void setBasicPassword(String basicPassword) {
		this.basicPassword = basicPassword;
	}

	public void setDigestUsername(String digestUsername) {
		this.digestUsername = digestUsername;
	}

	public void setDigestPassword(String digestPassword) {
		this.digestPassword = digestPassword;
	}

	public VenConfig clone() {
		VenConfig o = null;
		try {
			o = (VenConfig) super.clone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		return o;
	}

	public String getDigestRealm() {
		return digestRealm;
	}

	public void setDigestRealm(String digestRealm) {
		this.digestRealm = digestRealm;
	}

}
