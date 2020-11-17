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

//	private SSLContext sslContext;

	public VenConfig() {
	}

	public VenConfig(VenConfig clone) {
//		this.basicPassword = clone.getBasicPassword();
//		this.basicUsername = clone.getBasicUsername();
//		this.digestPassword = clone.getDigestPassword();
//		this.digestRealm = clone.getDigestRealm();
//		this.digestUsername = clone.getDigestUsername();
		this.pullFrequencySeconds = clone.getPullFrequencySeconds();
		this.pullModel = clone.getPullModel();
		this.replayProtectAcceptedDelaySecond = clone.getReplayProtectAcceptedDelaySecond();
		this.reportOnly = clone.getReportOnly();
//		this.trustCertificates = clone.getTrustCertificates();
//		this.venCertificatePath = clone.getVenCertificatePath();
//		this.venId = clone.getVenId();
//		this.venIdFile = clone.getVenIdFile();
//		this.venName = clone.getVenName();
//		this.venPrivateKeyPath = clone.getVenPrivateKeyPath();
//		this.venUrl = clone.getVenUrl();
		this.xmlSignature = clone.getXmlSignature();
		this.validateOadrPayloadAgainstXsdFilePath = clone.getValidateOadrPayloadAgainstXsdFilePath();

	}
//
//	@PostConstruct
//	public void init() {
//		if (venId == null && getVenIdFile() == null || venId != null && getVenIdFile() != null) {
//			throw new IllegalArgumentException("oadr.venid or oadr.venid.file must be configured");
//		} else if (getVenIdFile() != null) {
//			// set venId by reading venIdFile path first line
//			Path path = Paths.get(getVenIdFile());
//			File file = path.toFile();
//			if (!file.exists()) {
//				throw new IllegalArgumentException(
//						"oadr.venid.must be a valid file path containing venId as it's first line");
//			}
//			try (Stream<String> lines = Files.lines(path);) {
//				Optional<String> findFirst = lines.findFirst();
//				if (!findFirst.isPresent()) {
//					throw new IllegalArgumentException(
//							"oadr.venid.must be a valid file path containing venId as it's first line");
//
//				}
//				venId = findFirst.get().trim();
//			} catch (IOException e) {
//				throw new IllegalArgumentException(
//						"oadr.venid.must be a valid file path containing venId as it's first line", e);
//
//			}
//		}
//		String password = UUID.randomUUID().toString();
//		TrustManagerFactory trustManagerFactory;
//		try {
//			trustManagerFactory = OadrPKISecurity.createTrustManagerFactory(trustCertificates);
//			KeyManagerFactory keyManagerFactory = OadrPKISecurity.createKeyManagerFactory(this.getVenPrivateKeyPath(),
//					this.getVenCertificatePath(), password);
//
//			// SSL Context Factory
//			sslContext = SSLContext.getInstance("TLS");
//
//			// init ssl context
//			String seed = UUID.randomUUID().toString();
//			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
//					new SecureRandom(seed.getBytes()));
//		} catch (OadrSecurityException | NoSuchAlgorithmException | KeyManagementException e) {
//			throw new IllegalStateException(e);
//		}
//	}

//	public String getVenId() {
//		return venId;
//	}
//
//	public String getVenName() {
//		return venName;
//	}

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

//	public String getVenUrl() {
//		return venUrl;
//	}
//
//	public String getBasicUsername() {
//		return basicUsername;
//	}
//
//	public String getBasicPassword() {
//		return basicPassword;
//	}
//
//	public String getDigestUsername() {
//		return digestUsername;
//	}
//
//	public String getDigestPassword() {
//		return digestPassword;
//	}
//
//	public String getVenPrivateKeyPath() {
//		return venPrivateKeyPath;
//	}
//
//	public String getVenCertificatePath() {
//		return venCertificatePath;
//	}

	public Long getReplayProtectAcceptedDelaySecond() {
		return replayProtectAcceptedDelaySecond;
	}

//	public void setBasicUsername(String basicUsername) {
//		this.basicUsername = basicUsername;
//	}
//
//	public void setBasicPassword(String basicPassword) {
//		this.basicPassword = basicPassword;
//	}
//
//	public void setDigestUsername(String digestUsername) {
//		this.digestUsername = digestUsername;
//	}
//
//	public void setDigestPassword(String digestPassword) {
//		this.digestPassword = digestPassword;
//	}
//
//	public String getDigestRealm() {
//		return digestRealm;
//	}
//
//	public void setDigestRealm(String digestRealm) {
//		this.digestRealm = digestRealm;
//	}
//
//	public List<String> getTrustCertificates() {
//		return trustCertificates;
//	}
//
//	public String getVenIdFile() {
//		return venIdFile;
//	}

	public String getValidateOadrPayloadAgainstXsdFilePath() {
		return validateOadrPayloadAgainstXsdFilePath;
	}

//	public SSLContext getSslContext() {
//
//		return sslContext;
//	}

//	public String getContextPath() {
//		return contextPath;
//	}
//
//	public int getPort() {
//		return port;
//	}

}
