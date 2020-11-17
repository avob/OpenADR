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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class VtnSessionConfiguration {

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

	private String sessionId;
	private String vtnId;
	private String vtnUrl;
	private String vtnXmppHost;
	private Integer vtnXmppPort;
	private String vtnXmppDomain;
	private String vtnXmppUser;
	private String vtnXmppPass;
	private VenConfig venSessionConfig;
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

	private SSLContext sslContext;

	public VtnSessionConfiguration(String sessionId, Properties properties, VenConfig defaultVenSessionConfig) {
		this.sessionId = sessionId;
		setVenSessionConfig(new VenConfig(defaultVenSessionConfig));
		for (Map.Entry<Object, Object> e : properties.entrySet()) {
			String keyStr = (String) e.getKey();
			String prop = (String) e.getValue();
			if (VTN_ID.equals(keyStr)) {
				this.setVtnId(prop);
			} else if (VTN_ID_FILE.equals(keyStr)) {
				this.setVtnId(getIdFromFile(prop));
			} else if (VTN_URL.equals(keyStr)) {
				this.setVtnUrl(prop);
			} else if (VTN_XMPP_HOST.equals(keyStr)) {
				this.setVtnXmppHost(prop);
			} else if (VTN_XMPP_PORT.equals(keyStr)) {
				this.setVtnXmppPort(Integer.parseInt(prop));
			} else if (VTN_XMPP_DOMAIN.equals(keyStr)) {
				this.setVtnXmppDomain(prop);
			} else if (AUTHENTIFICATION_BASIC_USER.equals(keyStr)) {
				this.setBasicUsername(prop);
			} else if (AUTHENTIFICATION_BASIC_PASS.equals(keyStr)) {
				this.setBasicPassword(prop);
			} else if (AUTHENTIFICATION_DIGEST_USER.equals(keyStr)) {
				this.setDigestUsername(prop);
			} else if (AUTHENTIFICATION_DIGEST_PASS.equals(keyStr)) {
				this.setDigestPassword(prop);
			} else if (AUTHENTIFICATION_DIGEST_REALM.equals(keyStr)) {
				this.setDigestRealm(prop);
			} else if (AUTHENTIFICATION_XMPP_USER.equals(keyStr)) {
				this.setVtnXmppUser(prop);
			} else if (AUTHENTIFICATION_XMPP_PASS.equals(keyStr)) {
				this.setVtnXmppPass(prop);
			}

			else if (CONTEXT_PATH.equals(keyStr)) {
				this.setContextPath(prop);
			} else if (PORT.equals(keyStr)) {
				this.setPort(Integer.valueOf(prop));
			} else if (VEN_ID.equals(keyStr)) {
				this.setVenId(prop);
			} else if (VEN_ID_FILE.equals(keyStr)) {
				this.setVenId(getIdFromFile(prop));
			} else if (VEN_NAME.equals(keyStr)) {
				this.setVenName(prop);
			} else if (VEN_URL.equals(keyStr)) {
				this.setVenUrl(prop);
			} else if (TRUST_CERTIFICATES.equals(keyStr)) {
				this.setTrustCertificates(Arrays.asList(prop.split(",")));
			} else if (PRIVATE_KEY.equals(keyStr)) {
				this.setVenPrivateKeyPath(prop);
			} else if (CERTIFICATE.equals(keyStr)) {
				this.setVenCertificatePath(prop);
			}

		}

		String password = UUID.randomUUID().toString();
		TrustManagerFactory trustManagerFactory;
		try {
			trustManagerFactory = OadrPKISecurity.createTrustManagerFactory(this.getTrustCertificates());
			KeyManagerFactory keyManagerFactory = OadrPKISecurity.createKeyManagerFactory(this.getVenPrivateKeyPath(),
					this.getVenCertificatePath(), password);

			// SSL Context Factory
			sslContext = SSLContext.getInstance("TLS");

			// init ssl context
			String seed = UUID.randomUUID().toString();
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
					new SecureRandom(seed.getBytes()));
		} catch (OadrSecurityException | NoSuchAlgorithmException | KeyManagementException e) {
			throw new IllegalStateException(e);
		}

	}

	private String getIdFromFile(String filePath) {
		// set vtnId by reading vtnIdFile path first line
		Path path = Paths.get(filePath);
		File file = path.toFile();
		if (!file.exists()) {
			throw new IllegalArgumentException(
					"oadr.vtnid.file must be a valid file path containing venId as it's first line");
		}
		try (Stream<String> lines = Files.lines(path);) {
			Optional<String> findFirst = lines.findFirst();
			if (!findFirst.isPresent()) {
				throw new IllegalArgumentException(
						"oadr.vtnid.file must be a valid file path containing venId as it's first line");

			}
			return findFirst.get().trim();

		} catch (IOException exp) {
			throw new IllegalArgumentException(
					"oadr.vtnid.file must be a valid file path containing venId as it's first line", exp);

		}
	}

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

	public VenConfig getVenSessionConfig() {
		return venSessionConfig;
	}

	public void setVenSessionConfig(VenConfig venSessionConfig) {
		this.venSessionConfig = venSessionConfig;
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

}
