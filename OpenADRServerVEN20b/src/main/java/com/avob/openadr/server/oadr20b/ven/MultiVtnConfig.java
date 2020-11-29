package com.avob.openadr.server.oadr20b.ven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
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
import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bRegisterReportBuilder;
import com.avob.openadr.model.oadr20b.ei.ReportSpecifierType;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrSamplingRateType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bInvalidReportRequestException;
import com.avob.openadr.server.oadr20b.ven.exception.OadrVTNInitializationException;
import com.avob.openadr.server.oadr20b.ven.xmpp.XmppVenListener;

@Configuration
public class MultiVtnConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiVtnConfig.class);

	@Resource
	private XmppVenListener xmppVenListeners;

	@Resource
	private VtnSessionFactory vtnSessionFactory;

	@Autowired
	private Environment env;

	private Map<String, VtnSessionConfiguration> multiConfig = new HashMap<String, VtnSessionConfiguration>();

	private Map<String, OadrHttpVenClient20b> multiHttpClientConfig = new HashMap<String, OadrHttpVenClient20b>();

	private Map<String, OadrXmppVenClient20b> multiXmppClientConfig = new HashMap<String, OadrXmppVenClient20b>();

	private Map<String, Map<String, OadrReportType>> reports = new HashMap<>();

	private List<String> knownVtnId = new ArrayList<>();

	private void configureClient(VtnSessionConfiguration session)
			throws OadrSecurityException, JAXBException, OadrVTNInitializationException {

		if (session.getVtnXmppHost() != null && session.getVtnXmppPort() != null) {
			configureXMPPClient(session);
		} else if (session.getVtnUrl() != null) {
			configureHTTPClient(session);
		} else {
			throw new IllegalStateException(String.format(
					"Invalid config: %s - vtnUrl must be defined for HTTP ven, xmppHost and xmppPort for XMPP ven",
					session.getSessionId()));
		}
	}

	private void configureHTTPClient(VtnSessionConfiguration session)
			throws OadrSecurityException, JAXBException, OadrVTNInitializationException {
		LOGGER.info("Init HTTP VEN client");
		OadrHttpClientBuilder builder = new OadrHttpClientBuilder().withDefaultHost(session.getVtnUrl())
				.withTrustedCertificate(new ArrayList<String>(session.getTrustCertificates())).withPooling(1, 1)
				.withProtocol(Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());

		if (session.isBasicAuthenticationConfigured()) {

			builder.withDefaultBasicAuthentication(session.getVtnUrl(), session.getBasicUsername(),
					session.getBasicPassword());

		} else if (session.isDigestAuthenticationConfigured()) {
			builder.withDefaultDigestAuthentication(session.getVtnUrl(), session.getDigestRealm(), "",
					session.getDigestUsername(), session.getDigestPassword());

		} else {
			builder.withX509Authentication(session.getVenPrivateKeyPath(), session.getVenCertificatePath());
		}

		OadrHttpVenClient20b client = null;
		if (session.getXmlSignature()) {
			client = new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build(), session.getVenPrivateKeyPath(),
					session.getVenCertificatePath(), session.getReplayProtectAcceptedDelaySecond()));
		} else {
			client = new OadrHttpVenClient20b(new OadrHttpClient20b(builder.build()));
		}

		putMultiHttpClientConfig(session, client);
	}

	private void configureXMPPClient(VtnSessionConfiguration session)
			throws OadrSecurityException, JAXBException, OadrVTNInitializationException {
		try {

			LOGGER.info("Init XMPP VEN client");
			String password = UUID.randomUUID().toString();
			SSLContext sslContext = OadrPKISecurity.createSSLContext(session.getVenPrivateKeyPath(),
					session.getVenCertificatePath(), session.getTrustCertificates(), password);

			OadrXmppClient20bBuilder builder = new OadrXmppClient20bBuilder()
					.withHostAndPort(session.getVtnXmppHost(), session.getVtnXmppPort()).withVenID(session.getVenId())
					.withResource("client").withSSLContext(sslContext).withListener(xmppVenListeners);

			if (session.getVtnXmppDomain() != null) {
				builder.withDomain(session.getVtnXmppDomain());
			}

			if (session.getVtnXmppUser() != null && session.getVtnXmppPass() != null) {
				builder.withPassword(session.getVtnXmppPass());
			}

			OadrXmppClient20b oadrXmppClient20b = builder.build();

			OadrXmppVenClient20b venClient = null;
			if (session.getXmlSignature()) {
				venClient = new OadrXmppVenClient20b(oadrXmppClient20b, session.getVenPrivateKeyPath(),
						session.getVenCertificatePath(), session.getReplayProtectAcceptedDelaySecond());
			} else {
				venClient = new OadrXmppVenClient20b(oadrXmppClient20b);
			}

			String connectionJid = venClient.getBareConnectionJid();
			session.setVenUrl(connectionJid);
			putMultiXmppClientConfig(session, venClient);

		} catch (OadrSecurityException | OadrXmppException e) {
			throw new OadrVTNInitializationException(e);
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
				VtnSessionConfiguration session = vtnSessionFactory.createVtnSession(entry.getKey(), entry.getValue());

				LOGGER.debug("Valid vtn configuration: " + entry.getKey());
				LOGGER.info(session.toString());
				configureClient(session);
				multiConfig.put(getSessionKey(session.getVtnId(), session.getVenUrl()), session);

				knownVtnId.add(session.getVtnId());

				if (session.getVenRegisterReport() != null) {

					Map<String, OadrReportType> map = new HashMap<>();
					session.getVenRegisterReport().values().forEach(report -> {
						map.put(report.getReportSpecifierID(), report);
					});
					reports.put(getSessionKey(session.getVtnId(), session.getVenUrl()), map);

				}

			} catch (OadrSecurityException e) {
				LOGGER.error("Dynamic Vtn conf key: " + entry.getKey() + " is not a valid vtn configuration", e);
			} catch (JAXBException e) {
				LOGGER.error("Dynamic Vtn conf key: " + entry.getKey() + " is not a valid vtn configuration", e);
			} catch (OadrVTNInitializationException e) {
				LOGGER.error("Dynamic Vtn conf key: " + entry.getKey() + " is not a valid vtn configuration", e);
			}
		}

		if (multiConfig.isEmpty()) {
			throw new IllegalArgumentException("No Vtn configuration has been found");
		}

	}

	public OadrRegisterReportType getVenRegisterReport(VtnSessionConfiguration vtnConfig) {
		String requestId = UUID.randomUUID().toString();
		Oadr20bRegisterReportBuilder builder = Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId,
				vtnConfig.getVenId());
		if (vtnConfig.getVenRegisterReport() != null) {
			builder.addOadrReport(new ArrayList<>(vtnConfig.getVenRegisterReport().values()));
		}
		return builder.build();
	}

	public void checkReportSpecifier(VtnSessionConfiguration vtnConfig, String requestId, String reportRequestId,
			ReportSpecifierType reportSpecifier) throws Oadr20bInvalidReportRequestException {

		String reportSpecifierID = reportSpecifier.getReportSpecifierID();
		if (reports.get(getSessionKey(vtnConfig.getVtnId(), vtnConfig.getVenUrl())) == null || !reports
				.get(getSessionKey(vtnConfig.getVtnId(), vtnConfig.getVenUrl())).containsKey(reportSpecifierID)) {
			throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461,
							vtnConfig.getVenId())
					.withDescription(
							String.format("Invalid create report request: %s - report specifier %s not suported",
									reportRequestId, reportSpecifierID))
					.build());
		}

		OadrReportType report = reports.get(getSessionKey(vtnConfig.getVtnId(), vtnConfig.getVenUrl()))
				.get(reportSpecifierID);
		Map<String, OadrReportDescriptionType> reportDescriptions = report.getOadrReportDescription().stream()
				.collect(Collectors.toMap(desc -> {
					return getReportDescriptionUID(desc);
				}, Function.identity()));

		DurationPropType granularity = reportSpecifier.getGranularity();
		DurationPropType reportBackDuration = reportSpecifier.getReportBackDuration();

		Long granularityMillis = Oadr20bFactory.xmlDurationToMillisecond(granularity.getDuration());
		Long reportBackDurationMillis = Oadr20bFactory.xmlDurationToMillisecond(reportBackDuration.getDuration());

		if (reportBackDurationMillis < granularityMillis) {
			throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.INVALID_DATA_454,
							vtnConfig.getVenId())
					.withDescription(String.format(
							"Invalid create report request: %s - Granularity duration must be less than report back duration",
							reportRequestId))
					.build());
		}

		List<SpecifierPayloadType> specifierPayload = reportSpecifier.getSpecifierPayload();
		for (SpecifierPayloadType specifier : specifierPayload) {
			String reportDescriptionUID = getReportDescriptionUID(specifier);

			if (!reportDescriptions.containsKey(reportDescriptionUID)) {
				throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
						.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461,
								vtnConfig.getVenId())
						.withDescription(
								String.format("Invalid create report request: %s - report specifier %s not suported",
										reportRequestId, reportSpecifierID))
						.build());
			}

			OadrReportDescriptionType oadrReportDescriptionType = reportDescriptions.get(reportDescriptionUID);
			OadrSamplingRateType oadrSamplingRate = oadrReportDescriptionType.getOadrSamplingRate();

			Long minSamplingRateMillis = null;
			Long maxSamplingRateMillis = null;
			boolean oadrOnChange = false;
			if (oadrSamplingRate != null) {
				if (oadrSamplingRate.getOadrMinPeriod() != null) {
					minSamplingRateMillis = Oadr20bFactory
							.xmlDurationToMillisecond(oadrSamplingRate.getOadrMinPeriod());
					if (granularityMillis < minSamplingRateMillis) {
						throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
								.newOadr20bResponseBuilder(requestId,
										Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461, vtnConfig.getVenId())
								.withDescription(String.format(
										"Invalid create report request: %s - granularity must be greater than every report description oadrMinPeriod if defined",
										reportRequestId, reportSpecifierID))
								.build());
					}
				}
				if (oadrSamplingRate.getOadrMinPeriod() != null) {
					maxSamplingRateMillis = Oadr20bFactory
							.xmlDurationToMillisecond(oadrSamplingRate.getOadrMaxPeriod());
					if (reportBackDurationMillis > maxSamplingRateMillis) {
						throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
								.newOadr20bResponseBuilder(requestId,
										Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461, vtnConfig.getVenId())
								.withDescription(String.format(
										"Invalid create report request: %s - report back duration must be greater than every report description oadrMaxPeriod if defined",
										reportRequestId, reportSpecifierID))
								.build());
					}
				}
				oadrOnChange = oadrSamplingRate.isOadrOnChange();
			}

			if (!oadrOnChange && (granularityMillis == 0 || reportBackDurationMillis == 0)) {
				throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
						.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461,
								vtnConfig.getVenId())
						.withDescription(String.format(
								"Invalid create report request: %s - granularity and report back duration equals 0 while at least on report description do not support oadrOnChange",
								reportRequestId, reportSpecifierID))
						.build());
			}

		}
	}

	private String getReportDescriptionUID(SpecifierPayloadType description) {
		StringBuilder builder = new StringBuilder().append(description.getRID());
		if (description.getReadingType() != null) {
			builder.append(description.getReadingType());
		}
		if (description.getItemBase() != null) {
			builder.append(description.getItemBase().getName().toString());
		}
		return builder.toString();
	}

	private String getReportDescriptionUID(OadrReportDescriptionType description) {
		StringBuilder builder = new StringBuilder().append(description.getRID());
		if (description.getReadingType() != null) {
			builder.append(description.getReadingType());
		}
		if (description.getItemBase() != null) {
			builder.append(description.getItemBase().getName().toString());
		}
		return builder.toString();
	}

	public void oadrCreateReport(VtnSessionConfiguration vtnConfiguration, OadrCreateReportType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCreateReport(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCreateReport(payload);
		}
	}

	public void oadrUpdateReport(VtnSessionConfiguration vtnConfiguration, OadrUpdateReportType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrUpdateReport(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrUpdateReport(payload);
		}
	}

	public void oadrCreateOpt(VtnSessionConfiguration vtnConfiguration, OadrCreateOptType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCreateOpt(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCreateOpt(payload);
		}
	}

	public void oadrCancelOptType(VtnSessionConfiguration vtnConfiguration, OadrCancelOptType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCancelOptType(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCancelOptType(payload);
		}
	}

	public void oadrRegisterReport(VtnSessionConfiguration vtnConfiguration, OadrRegisterReportType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrRegisterReport(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrRegisterReport(payload);
		}
	}

	public void oadrCreatedEvent(VtnSessionConfiguration vtnConfiguration, OadrCreatedEventType payload)
			throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException, XmppStringprepException, NotConnectedException,
			Oadr20bMarshalException, InterruptedException {
		if (vtnConfiguration.getVtnUrl() != null) {
			multiHttpClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCreatedEvent(payload);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			multiXmppClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()))
					.oadrCreatedEvent(payload);
		}
	}

	public OadrXmppVenClient20b getMultiXmppClientConfig(VtnSessionConfiguration vtnConfiguration) {
		return multiXmppClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()));
	}

	public void putMultiXmppClientConfig(VtnSessionConfiguration vtnConfiguration, OadrXmppVenClient20b venClient) {
		multiXmppClientConfig.put(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()), venClient);
	}

	public void putMultiHttpClientConfig(VtnSessionConfiguration vtnConfiguration, OadrHttpVenClient20b venClient) {
		multiHttpClientConfig.put(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()), venClient);
	}

	public void setMultiXmppClientConfig(Map<String, OadrXmppVenClient20b> multiXmppClientConfig) {
		this.multiXmppClientConfig = multiXmppClientConfig;
	}

	public Map<String, VtnSessionConfiguration> getMultiConfig() {
		return multiConfig;
	}

//	public VtnSessionConfiguration getMultiConfig(String vtnId) {
//		return multiConfig.get(vtnId);
//	}

	public OadrHttpVenClient20b getMultiHttpClientConfig(VtnSessionConfiguration vtnConfiguration) {
		return multiHttpClientConfig.get(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()));
	}

	public void setMultiHttpClientConfigClient(VtnSessionConfiguration vtnConfiguration, OadrHttpVenClient20b client) {
		multiHttpClientConfig.put(getSessionKey(vtnConfiguration.getVtnId(), vtnConfiguration.getVenUrl()), client);
	}

	public boolean isKnownVtnId(String vtnId) {
		return knownVtnId.contains(vtnId);
	}

	public VtnSessionConfiguration getMultiConfig(String vtnId, String venPushUrl) {
		return multiConfig.get(getSessionKey(vtnId, venPushUrl));
	}

	public VtnSessionConfiguration getMultiConfig(String sessionKey) {
		return multiConfig.get(sessionKey);
	}

	private String getSessionKey(String vtnId, String venPushUrl) {
		return new StringBuilder().append(vtnId).append(venPushUrl).toString();
	}

}
