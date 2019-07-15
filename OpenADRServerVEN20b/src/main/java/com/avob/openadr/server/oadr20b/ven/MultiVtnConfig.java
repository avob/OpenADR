package com.avob.openadr.server.oadr20b.ven;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import com.avob.openadr.client.http.OadrHttpClientBuilder;
import com.avob.openadr.client.http.oadr20b.OadrHttpClient20b;
import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20bBuilder;
import com.avob.openadr.client.xmpp.oadr20b.OadrXmppException;
import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.exception.OadrVTNInitializationException;
import com.avob.openadr.server.oadr20b.ven.xmpp.XmppVenListener;

@Configuration
public class MultiVtnConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiVtnConfig.class);

	@Resource
	private XmppVenListener xmppVenListeners;

	@Resource
	private VenConfig venConfig;

	@Autowired
	private Environment env;

	private Map<String, VtnSessionConfiguration> multiConfig = new HashMap<String, VtnSessionConfiguration>();

	private Map<String, OadrHttpVenClient20b> multiHttpClientConfig = new HashMap<String, OadrHttpVenClient20b>();

	private Map<String, OadrXmppVenClient20b> multiXmppClientConfig = new HashMap<String, OadrXmppVenClient20b>();

	private void configureClient(VtnSessionConfiguration session)
			throws OadrSecurityException, JAXBException, OadrVTNInitializationException {

		if (session.getVtnUrl() != null) {
			OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(session.getVtnUrl())
					.withTrustedCertificate(
							new ArrayList<String>(session.getVenSessionConfig().getVtnTrustCertificate().values()))
					.withPooling(1, 1).withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());

			if (session.isBasicAuthenticationConfigured()) {
				LOGGER.info("Init HTTP VEN client with basic authentication");
				builder.withDefaultBasicAuthentication(session.getVtnUrl(),
						session.getVenSessionConfig().getBasicUsername(),
						session.getVenSessionConfig().getBasicPassword());

			} else if (session.isDigestAuthenticationConfigured()) {
				LOGGER.info("Init HTTP VEN client with digest authentication");
				builder.withDefaultDigestAuthentication(session.getVtnUrl(),
						session.getVenSessionConfig().getDigestRealm(), "",
						session.getVenSessionConfig().getDigestUsername(),
						session.getVenSessionConfig().getDigestPassword());

			} else {
				builder.withX509Authentication(session.getVenSessionConfig().getVenPrivateKeyPath(),
						session.getVenSessionConfig().getVenCertificatePath());
			}

			OadrHttpVenClient20b client = null;
			if (venConfig.getXmlSignature()) {
				client = new OadrHttpVenClient20b(
						new OadrHttpClient20b(builder.build(), session.getVenSessionConfig().getVenPrivateKeyPath(),
								session.getVenSessionConfig().getVenCertificatePath(),
								session.getVenSessionConfig().getReplayProtectAcceptedDelaySecond()));
			} else {
				client = new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build()));
			}

			getMultiHttpClientConfig().put(session.getVtnId(), client);
		} else if (session.getVtnXmppHost() != null && session.getVtnXmppPort() != null) {

			String keystorePassword = UUID.randomUUID().toString();

			KeyStore keystore;
			try {
				keystore = OadrHttpSecurity.createKeyStore(session.getVenSessionConfig().getVenPrivateKeyPath(),
						session.getVenSessionConfig().getVenCertificatePath(), keystorePassword);
				KeyStore truststore = OadrHttpSecurity
						.createTrustStore(session.getVenSessionConfig().getVtnTrustCertificate());

				// init key manager factory
				KeyStore createKeyStore = keystore;
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(createKeyStore, keystorePassword.toCharArray());

				// init trust manager factory
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(truststore);

				// SSL Context Factory
				SSLContext sslContext = SSLContext.getInstance("TLS");

				// init ssl context
				String seed = UUID.randomUUID().toString();

				sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom(seed.getBytes()));

				OadrXmppClient20bBuilder builder = new OadrXmppClient20bBuilder()
						.withHostAndPort(session.getVtnXmppHost(), session.getVtnXmppPort())
						.withVenID(session.getVtnId()).withResource("client").withSSLContext(sslContext)
						.withListener(xmppVenListeners);

				if (session.getVtnXmppUser() != null && session.getVtnXmppPass() != null) {
					builder.withPassword(session.getVtnXmppPass());
				}

				OadrXmppClient20b oadrXmppClient20b = builder.build();

				OadrXmppVenClient20b venClient = null;
				if (venConfig.getXmlSignature()) {
					venClient = new OadrXmppVenClient20b(oadrXmppClient20b,
							session.getVenSessionConfig().getVenPrivateKeyPath(),
							session.getVenSessionConfig().getVenCertificatePath(),
							session.getVenSessionConfig().getReplayProtectAcceptedDelaySecond());
				} else {
					venClient = new OadrXmppVenClient20b(oadrXmppClient20b);
				}
				getMultiXmppClientConfig().put(session.getVtnId(), venClient);

			} catch (KeyStoreException e) {
				throw new OadrVTNInitializationException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new OadrVTNInitializationException(e);
			} catch (CertificateException e) {
				throw new OadrVTNInitializationException(e);
			} catch (IOException e) {
				throw new OadrVTNInitializationException(e);
			} catch (OadrSecurityException e) {
				throw new OadrVTNInitializationException(e);
			} catch (UnrecoverableKeyException e) {
				throw new OadrVTNInitializationException(e);
			} catch (OadrXmppException e) {
				throw new OadrVTNInitializationException(e);
			} catch (KeyManagementException e) {
				throw new OadrVTNInitializationException(e);
			}

		}

	}

	@SuppressWarnings("rawtypes")
	private Map<String, Properties> loadVtnConf() {

		String dynamicConfigurationPattern = "oadr.vtn.";
		Map<String, Properties> perVtnProperties = new HashMap<>();
		Properties props = new Properties();
		MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
		StreamSupport.stream(propSrcs.spliterator(), false).filter(ps -> ps instanceof EnumerablePropertySource)
				.map(ps -> ((EnumerablePropertySource) ps).getPropertyNames()).flatMap(Arrays::<String>stream)
				.forEach(propName -> {
					if (propName.contains(dynamicConfigurationPattern)) {
						String replaceAll = propName.replaceAll(dynamicConfigurationPattern, "");
						String key = replaceAll.split("\\.")[0];
						Properties vtnProps = perVtnProperties.get(key);
						if (vtnProps == null) {
							vtnProps = new Properties();
						}
						String propKey = replaceAll.replaceAll(key + ".", "");
						vtnProps.put(dynamicConfigurationPattern + propKey, env.getProperty(propName));
						perVtnProperties.put(key, vtnProps);

						props.setProperty(propName, env.getProperty(propName));

					}
				});

		return perVtnProperties;
	}

	@PostConstruct
	public void init() {
		Map<String, Properties> loadVtnConf = loadVtnConf();
		for (Entry<String, Properties> entry : loadVtnConf.entrySet()) {

			try {
				VtnSessionConfiguration session = new VtnSessionConfiguration(entry.getValue(), venConfig);
				LOGGER.debug("Valid vtn configuration: " + entry.getKey());
				LOGGER.info(session.toString());
				configureClient(session);
				getMultiConfig().put(session.getVtnId(), session);
			} catch (OadrSecurityException e) {
				LOGGER.error("Dynamic Vtn conf key: " + entry.getKey() + " is not a valid vtn configuration", e);
			} catch (JAXBException e) {
				LOGGER.error("Dynamic Vtn conf key: " + entry.getKey() + " is not a valid vtn configuration", e);
			} catch (OadrVTNInitializationException e) {
				LOGGER.error("Dynamic Vtn conf key: " + entry.getKey() + " is not a valid vtn configuration", e);
			}
		}

		if (getMultiConfig().isEmpty()) {
			throw new IllegalArgumentException("No Vtn configuration has been found");
		}

	}

	public Map<String, VtnSessionConfiguration> getMultiConfig() {
		return multiConfig;
	}

	public VtnSessionConfiguration getMultiConfig(String vtnId) {
		return multiConfig.get(vtnId);
	}

	private Map<String, OadrHttpVenClient20b> getMultiHttpClientConfig() {
		return multiHttpClientConfig;
	}

	public OadrHttpVenClient20b getMultiHttpClientConfig(VtnSessionConfiguration vtnConfiguration) {
		return multiHttpClientConfig.get(vtnConfiguration.getVtnId());
	}

	/**
	 * only for test purpose
	 * 
	 * @param vtnConfiguration
	 * @param client
	 */
	public void setMultiHttpClientConfigClient(VtnSessionConfiguration vtnConfiguration, OadrHttpVenClient20b client) {
		multiHttpClientConfig.put(vtnConfiguration.getVtnId(), client);
	}

	public void oadrCreateReport(VtnSessionConfiguration vtnConfiguration, OadrCreateReportType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(vtnConfiguration.getVtnId()).oadrCreateReport(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(vtnConfiguration.getVtnId()).oadrCreateReport(payload);
		}
	}

	public void oadrUpdateReport(VtnSessionConfiguration vtnConfiguration, OadrUpdateReportType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(vtnConfiguration.getVtnId()).oadrUpdateReport(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(vtnConfiguration.getVtnId()).oadrUpdateReport(payload);
		}
	}

	public void oadrCreateOpt(VtnSessionConfiguration vtnConfiguration, OadrCreateOptType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(vtnConfiguration.getVtnId()).oadrCreateOpt(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(vtnConfiguration.getVtnId()).oadrCreateOpt(payload);
		}
	}

	public void oadrCancelOptType(VtnSessionConfiguration vtnConfiguration, OadrCancelOptType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(vtnConfiguration.getVtnId()).oadrCancelOptType(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(vtnConfiguration.getVtnId()).oadrCancelOptType(payload);
		}
	}

	public void oadrRegisterReport(VtnSessionConfiguration vtnConfiguration, OadrRegisterReportType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(vtnConfiguration.getVtnId()).oadrRegisterReport(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(vtnConfiguration.getVtnId()).oadrRegisterReport(payload);
		}
	}

	public OadrXmppVenClient20b getMultiXmppClientConfig(VtnSessionConfiguration vtnConfiguration) {
		return multiXmppClientConfig.get(vtnConfiguration.getVtnId());
	}

	public Map<String, OadrXmppVenClient20b> getMultiXmppClientConfig() {
		return multiXmppClientConfig;
	}

	public void setMultiXmppClientConfig(Map<String, OadrXmppVenClient20b> multiXmppClientConfig) {
		this.multiXmppClientConfig = multiXmppClientConfig;
	}

}
