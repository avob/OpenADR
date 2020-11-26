package com.avob.openadr.server.oadr20b.ven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

@Service
public class VtnSessionFactory {

	private static final String VTN_ID = "oadr.vtn.vtnid";
	private static final String VTN_ID_FILE = "oadr.vtn.vtnid.file";
	private static final String VTN_URL = "oadr.vtn.vtnUrl";

	private static final String VTN_XMPP_HOST = "oadr.vtn.xmpp.host";
	private static final String VTN_XMPP_PORT = "oadr.vtn.xmpp.port";
	private static final String VTN_XMPP_DOMAIN = "oadr.vtn.xmpp.domain";

	private static final String AUTHENTIFICATION_BASIC_USER = "oadr.vtn.security.authentication.basic.username";
	private static final String AUTHENTIFICATION_BASIC_PASS = "oadr.vtn.security.authentication.basic.password";
	private static final String AUTHENTIFICATION_DIGEST_USER = "oadr.vtn.security.authentication.digest.username";
	private static final String AUTHENTIFICATION_DIGEST_PASS = "oadr.vtn.security.authentication.digest.password";
	private static final String AUTHENTIFICATION_DIGEST_REALM = "oadr.vtn.security.authentication.digest.realm";

	private static final String AUTHENTIFICATION_XMPP_USER = "oadr.vtn.security.authentication.xmpp.username";
	private static final String AUTHENTIFICATION_XMPP_PASS = "oadr.vtn.security.authentication.xmpp.password";

	private static final String CONTEXT_PATH = "oadr.vtn.server.context_path";
	private static final String PORT = "oadr.vtn.server.port";
	private static final String VEN_ID = "oadr.vtn.venid";

	private static final String VEN_ID_FILE = "oadr.vtn.venid.file";
	private static final String VEN_NAME = "oadr.vtn.venName";
	private static final String VEN_URL = "oadr.vtn.venUrl";
	private static final String TRUST_CERTIFICATES = "oadr.vtn.security.vtn.trustcertificate";
	private static final String PRIVATE_KEY = "oadr.vtn.security.ven.key";
	private static final String CERTIFICATE = "oadr.vtn.security.ven.cert";

	private static final String PULL_FREQUENCY = "oadr.pullFrequencySeconds";
	private static final String REPORT_ONLY = "oadr.reportOnly";
	private static final String XML_SIGNATURE = "oadr.xmlSignature";
	private static final String PULL_MODEL = "oadr.pullModel";
	private static final String REPLAY_PROTECTION_DELAY = "oadr.security.replayProtectAcceptedDelaySecond";
	private static final String VEN_REGISTER_REPORT_FILE = "oadr.vtn.venRegisterReport.file";

	@Resource
	private Oadr20bJAXBContext oadr20bJAXBContext;

	@Value("${oadr.pullFrequencySeconds:#{null}}")
	private Long defaultPullFrequencySeconds;

	@Value("${oadr.reportOnly}")
	private Boolean defaultReportOnly;

	@Value("${oadr.xmlSignature}")
	private Boolean defaultXmlSignature;

	@Value("${oadr.pullModel}")
	private Boolean defaultPullModel;

	@Value("${oadr.security.replayProtectAcceptedDelaySecond:#{null}}")
	private Long defaultReplayProtectAcceptedDelaySecond;

	public VtnSessionConfiguration createVtnSession(String sessionId, Properties properties) {
		VtnSessionConfiguration session = new VtnSessionConfiguration();
		session.setSessionId(sessionId);

		session.setReportOnly(defaultReportOnly);
		session.setXmlSignature(defaultXmlSignature);
		session.setPullModel(defaultPullModel);
		session.setReplayProtectAcceptedDelaySecond(defaultReplayProtectAcceptedDelaySecond);
		session.setPullFrequencySeconds(defaultPullFrequencySeconds);

		for (Map.Entry<Object, Object> e : properties.entrySet()) {
			String keyStr = (String) e.getKey();
			String prop = (String) e.getValue();
			if (VTN_ID.equals(keyStr)) {
				session.setVtnId(prop);
			} else if (VTN_ID_FILE.equals(keyStr)) {
				session.setVtnId(getIdFromFile(prop, VTN_ID_FILE));
			} else if (VTN_URL.equals(keyStr)) {
				session.setVtnUrl(prop);
			} else if (VTN_XMPP_HOST.equals(keyStr)) {
				session.setVtnXmppHost(prop);
			} else if (VTN_XMPP_PORT.equals(keyStr)) {
				session.setVtnXmppPort(Integer.parseInt(prop));
			} else if (VTN_XMPP_DOMAIN.equals(keyStr)) {
				session.setVtnXmppDomain(prop);
			} else if (AUTHENTIFICATION_BASIC_USER.equals(keyStr)) {
				session.setBasicUsername(prop);
			} else if (AUTHENTIFICATION_BASIC_PASS.equals(keyStr)) {
				session.setBasicPassword(prop);
			} else if (AUTHENTIFICATION_DIGEST_USER.equals(keyStr)) {
				session.setDigestUsername(prop);
			} else if (AUTHENTIFICATION_DIGEST_PASS.equals(keyStr)) {
				session.setDigestPassword(prop);
			} else if (AUTHENTIFICATION_DIGEST_REALM.equals(keyStr)) {
				session.setDigestRealm(prop);
			} else if (AUTHENTIFICATION_XMPP_USER.equals(keyStr)) {
				session.setVtnXmppUser(prop);
			} else if (AUTHENTIFICATION_XMPP_PASS.equals(keyStr)) {
				session.setVtnXmppPass(prop);
			} else if (CONTEXT_PATH.equals(keyStr)) {
				session.setContextPath(prop);
			} else if (PORT.equals(keyStr)) {
				session.setPort(Integer.valueOf(prop));
			} else if (VEN_ID.equals(keyStr)) {
				session.setVenId(prop);
			} else if (VEN_ID_FILE.equals(keyStr)) {
				session.setVenId(getIdFromFile(prop, VEN_ID_FILE));
			} else if (VEN_NAME.equals(keyStr)) {
				session.setVenName(prop);
			} else if (VEN_URL.equals(keyStr)) {
				session.setVenUrl(prop);
			} else if (TRUST_CERTIFICATES.equals(keyStr)) {
				session.setTrustCertificates(Arrays.asList(prop.split(",")));
			} else if (PRIVATE_KEY.equals(keyStr)) {
				session.setVenPrivateKeyPath(prop);
			} else if (CERTIFICATE.equals(keyStr)) {
				session.setVenCertificatePath(prop);
			} else if (PULL_FREQUENCY.equals(keyStr)) {
				session.setPullFrequencySeconds(Long.valueOf(prop));
			} else if (REPORT_ONLY.equals(keyStr)) {
				session.setReportOnly(Boolean.valueOf(prop));
			} else if (XML_SIGNATURE.equals(keyStr)) {
				session.setXmlSignature(Boolean.valueOf(prop));
			} else if (PULL_MODEL.equals(keyStr)) {
				session.setPullModel(Boolean.valueOf(prop));
			} else if (REPLAY_PROTECTION_DELAY.equals(keyStr)) {
				session.setReplayProtectAcceptedDelaySecond(Long.valueOf(prop));
			} else if (VEN_REGISTER_REPORT_FILE.equals(keyStr)) {
				OadrRegisterReportType registerReportFromFile = getRegisterReportFromFile(prop);
				session.setVenRegisterReport(registerReportFromFile.getOadrReport().stream()
						.collect(Collectors.toMap((OadrReportType r) -> r.getReportSpecifierID(), r -> r)));
			}

		}

		String password = UUID.randomUUID().toString();
		TrustManagerFactory trustManagerFactory;
		try {
			trustManagerFactory = OadrPKISecurity.createTrustManagerFactory(session.getTrustCertificates());
			KeyManagerFactory keyManagerFactory = OadrPKISecurity
					.createKeyManagerFactory(session.getVenPrivateKeyPath(), session.getVenCertificatePath(), password);

			// SSL Context Factory
			SSLContext sslContext = SSLContext.getInstance("TLS");

			// init ssl context
			String seed = UUID.randomUUID().toString();
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
					new SecureRandom(seed.getBytes()));

			session.setSslContext(sslContext);

		} catch (OadrSecurityException | NoSuchAlgorithmException | KeyManagementException e) {
			throw new IllegalStateException(e);
		}

		return session;

	}

	private String getIdFromFile(String filePath, String property) {
		// set vtnId by reading vtnIdFile path first line
		Path path = Paths.get(filePath);
		File file = path.toFile();
		if (!file.exists()) {
			throw new IllegalArgumentException(
					String.format("%s must be a valid file path containing venId as it's first line", property));
		}
		try (Stream<String> lines = Files.lines(path);) {
			Optional<String> findFirst = lines.findFirst();
			if (!findFirst.isPresent()) {
				throw new IllegalArgumentException(
						String.format("%s must be a valid file path containing venId as it's first line", property));

			}
			return findFirst.get().trim();

		} catch (IOException e) {
			throw new IllegalArgumentException(
					String.format("%s must be a valid file path containing venId as it's first line", property), e);

		}
	}

	private OadrRegisterReportType getRegisterReportFromFile(String filePath) {
		Path path = Paths.get(filePath);
		File file = path.toFile();
		if (!file.exists()) {
			throw new IllegalArgumentException(
					"oadr.vtn.venRegisterReport.file must be a valid file path containing XML formatted OadrRegisterReportType");
		}

		try {
			return oadr20bJAXBContext.unmarshal(file, OadrRegisterReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			throw new IllegalArgumentException(
					"oadr.vtn.venRegisterReport.file must be a valid file path containing XML formatted OadrRegisterReportType",
					e);
		}

	}

}
