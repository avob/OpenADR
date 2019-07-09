package com.avob.openadr.server.oadr20b.ven.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatePartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENEiRegisterPartyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiRegisterPartyService.class);

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	protected StatePersistenceService statePersistenceService;

	private Map<String, OadrCreatedPartyRegistrationType> registration = new ConcurrentHashMap<String, OadrCreatedPartyRegistrationType>();

	private List<Oadr20bVENEiRegisterPartyServiceListener> listeners;

	public interface Oadr20bVENEiRegisterPartyServiceListener {
		public void onRegistrationSuccess(VtnSessionConfiguration vtnConfiguration,
				OadrCreatedPartyRegistrationType registration);

		public void onRegistrationError(VtnSessionConfiguration vtnConfiguration);
	}

	public OadrResponseType oadrRequestReregistration(VtnSessionConfiguration vtnConfiguration,
			OadrRequestReregistrationType oadrRequestReregistrationType) {

		String requestId = "";
		String venID = oadrRequestReregistrationType.getVenID();
		int responseCode = HttpStatus.OK_200;
		reinitRegistration(vtnConfiguration);

		return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestId, responseCode, venID).build();
	}

	public OadrCanceledPartyRegistrationType oadrCancelPartyRegistration(VtnSessionConfiguration vtnConfiguration,
			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType) {

		String venID = oadrCancelPartyRegistrationType.getVenID();

		String requestID = oadrCancelPartyRegistrationType.getRequestID();
		String registrationID = oadrCancelPartyRegistrationType.getRegistrationID();
		int responseCode = HttpStatus.OK_200;
		if (getRegistration(vtnConfiguration).getRegistrationID().equals(registrationID)) {
			clearRegistration(vtnConfiguration);
		} else {
			responseCode = Oadr20bApplicationLayerErrorCode.INVALID_ID_452;
		}

		return Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(requestID, responseCode, registrationID, venID).build();

	}

	public void clearRegistration(VtnSessionConfiguration vtnConfiguration) {
		try {
			statePersistenceService.deleteRegistration(vtnConfiguration.getVenSessionConfig().getVenId(),
					vtnConfiguration);
			setRegistration(vtnConfiguration, null);
		} catch (IOException e) {
			LOGGER.error("", e);
		}
	}

	public void initRegistration(VtnSessionConfiguration vtnConfiguration) {
		this.initRegistration(vtnConfiguration, null);
	}

	public void reinitRegistration(VtnSessionConfiguration vtnConfiguration) {
		String registrationId = (getRegistration(vtnConfiguration) != null)
				? getRegistration(vtnConfiguration).getRegistrationID()
				: null;
		clearRegistration(vtnConfiguration);
		this.initRegistration(vtnConfiguration, registrationId);
	}

	private boolean isSameConfig(VtnSessionConfiguration vtnConfiguration) {
		return true;
	}

	private void initRegistration(VtnSessionConfiguration vtnConfiguration, String registrationId) {
		OadrCreatedPartyRegistrationType loadRegistration = null;
		try {
			loadRegistration = statePersistenceService
					.loadRegistration(vtnConfiguration.getVenSessionConfig().getVenId(), vtnConfiguration);

			String requestId = "0";
			String reportRequestId = "0";
			String reportSpecifierId = "METADATA";
			String granularity = "P0D";
			String reportBackDuration = "P0D";
			OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
					.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity,
							reportBackDuration)
					.addSpecifierPayload(null, ReadingTypeEnumeratedType.DIRECT_READ, reportSpecifierId).build();
			OadrCreateReportType build = Oadr20bEiReportBuilders
					.newOadr20bCreateReportBuilder(requestId, vtnConfiguration.getVenSessionConfig().getVenId())
					.addReportRequest(oadrReportRequestType).build();

			multiVtnConfig.oadrCreateReport(vtnConfiguration, build);

		} catch (Oadr20bUnmarshalException e) {
			LOGGER.error("", e);
		} catch (IOException e) {
			LOGGER.error("", e);
		} catch (Oadr20bException e) {
			LOGGER.error("", e);
		} catch (Oadr20bHttpLayerException e) {
			if (e.getErrorCode() == HttpStatus.FORBIDDEN_403) {
				loadRegistration = null;
				try {
					statePersistenceService.deleteRegistration(vtnConfiguration.getVenSessionConfig().getVenId(),
							vtnConfiguration);
				} catch (IOException e1) {
					LOGGER.error("", e1);
				}
				LOGGER.warn("Local registration have been rejected by VTN, trying manual registration");
			} else {
				LOGGER.error("", e);
			}

		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error("", e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("", e);
		} catch (NotConnectedException e) {
			LOGGER.error("", e);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("", e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
		}

		if (loadRegistration != null) {

			if (isSameConfig(vtnConfiguration)) {
				LOGGER.info("Ven already registered using registrationId: " + loadRegistration.getRegistrationID());
				LOGGER.info("        xmlSignature: " + vtnConfiguration.getVenSessionConfig().getXmlSignature());
				LOGGER.info("        reportOnly  : " + vtnConfiguration.getVenSessionConfig().getReportOnly());
				LOGGER.info("        pullModel   : " + vtnConfiguration.getVenSessionConfig().getPullModel());
				setRegistration(vtnConfiguration, loadRegistration);
				if (getListeners() != null) {
					final OadrCreatedPartyRegistrationType reg = loadRegistration;
					getListeners().forEach(listener -> listener.onRegistrationSuccess(vtnConfiguration, reg));
				}
				return;
			} else {
				this.reinitRegistration(vtnConfiguration);
			}

		}

		LOGGER.info("Ven is not yet registered");

		if (vtnConfiguration.getVtnUrl() != null) {
			try {

				Boolean pullModel = vtnConfiguration.getVenSessionConfig().getPullModel();
				String transportAddress = null;
				if (!pullModel) {
					transportAddress = vtnConfiguration.getVenSessionConfig().getVenUrl();
				}
				boolean reportOnly = vtnConfiguration.getVenSessionConfig().getReportOnly();
				OadrTransportType transportType = OadrTransportType.SIMPLE_HTTP;
				String venName = vtnConfiguration.getVenSessionConfig().getVenName();
				boolean xmlSignature = vtnConfiguration.getVenSessionConfig().getXmlSignature();
				String requestId = "";
				Oadr20bCreatePartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
						.newOadr20bCreatePartyRegistrationBuilder(requestId,
								vtnConfiguration.getVenSessionConfig().getVenId(),
								SchemaVersionEnumeratedType.OADR_20B.value())
						.withOadrHttpPullModel(pullModel).withOadrTransportAddress(transportAddress)
						.withOadrReportOnly(reportOnly).withOadrTransportName(transportType).withOadrVenName(venName)
						.withOadrXmlSignature(xmlSignature);
				if (registrationId != null) {
					builder.withRegistrationId(registrationId);
				}

				OadrCreatePartyRegistrationType createPartyRegistration = builder.build();

				LOGGER.info("Ven try to register using HTTP...");

				loadRegistration = multiVtnConfig.getMultiHttpClientConfig(vtnConfiguration)
						.oadrCreatePartyRegistration(createPartyRegistration);

				if (loadRegistration.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
					register(vtnConfiguration, loadRegistration);
				} else {
					LOGGER.error("Ven has failed to register - responseCode: "
							+ loadRegistration.getEiResponse().getResponseCode() + ", responseDescription: "
							+ loadRegistration.getEiResponse().getResponseDescription());
				}

				return;

			} catch (Oadr20bHttpLayerException e) {
				LOGGER.error("Fail to create registration: HttpLayerException[" + e.getErrorCode() + "]: "
						+ e.getErrorMessage());

			} catch (Oadr20bException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (Oadr20bXMLSignatureException e) {
				LOGGER.error("Fail to sign request payload", e);
			} catch (Oadr20bXMLSignatureValidationException e) {
				LOGGER.error("Fail to validate response xml signature", e);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
			if (getListeners() != null) {
				getListeners().forEach(listener -> listener.onRegistrationError(vtnConfiguration));
			}
			setRegistration(vtnConfiguration, null);
		} else if (vtnConfiguration.getVtnXmppHost() != null && vtnConfiguration.getVtnXmppPort() != null) {
			LOGGER.info("Ven try to register using XMPP...");

			try {
				OadrXmppVenClient20b xmppClient = multiVtnConfig.getMultiXmppClientConfig(vtnConfiguration);

				Boolean pullModel = vtnConfiguration.getVenSessionConfig().getPullModel();
				String transportAddress = xmppClient.getConnectionJid().toString();

				boolean reportOnly = vtnConfiguration.getVenSessionConfig().getReportOnly();
				OadrTransportType transportType = OadrTransportType.XMPP;
				String venName = vtnConfiguration.getVenSessionConfig().getVenName();
				boolean xmlSignature = vtnConfiguration.getVenSessionConfig().getXmlSignature();
				String requestId = "";
				Oadr20bCreatePartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
						.newOadr20bCreatePartyRegistrationBuilder(requestId,
								vtnConfiguration.getVenSessionConfig().getVenId(),
								SchemaVersionEnumeratedType.OADR_20B.value())
						.withOadrHttpPullModel(pullModel).withOadrTransportAddress(transportAddress)
						.withOadrReportOnly(reportOnly).withOadrTransportName(transportType).withOadrVenName(venName)
						.withOadrXmlSignature(xmlSignature);
				if (registrationId != null) {
					builder.withRegistrationId(registrationId);
				}

				OadrCreatePartyRegistrationType createPartyRegistration = builder.build();

				xmppClient.oadrCreatePartyRegistration(createPartyRegistration);
			} catch (XmppStringprepException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (NotConnectedException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (Oadr20bException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (Oadr20bHttpLayerException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (Oadr20bXMLSignatureException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (Oadr20bXMLSignatureValidationException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (Oadr20bMarshalException e) {
				LOGGER.error("Fail to create registration", e);
			} catch (InterruptedException e) {
				LOGGER.error("Fail to create registration", e);
			}
		}

	}

	public void register(VtnSessionConfiguration vtnConfiguration, OadrCreatedPartyRegistrationType registration)
			throws Oadr20bMarshalException, IOException {
		setRegistration(vtnConfiguration, registration);
		// session do not have to be persisted for xmpp vtn profil
		if (vtnConfiguration.getVtnUrl() != null) {
			statePersistenceService.persistRegistration(vtnConfiguration.getVenSessionConfig().getVenId(),
					vtnConfiguration, registration);
		}

		LOGGER.info("Ven has successfully register using registrationId: " + registration.getRegistrationID());
		LOGGER.debug("        xmlSignature: " + vtnConfiguration.getVenSessionConfig().getXmlSignature());
		LOGGER.debug("        reportOnly  : " + vtnConfiguration.getVenSessionConfig().getReportOnly());
		LOGGER.debug("        pullModel   : " + vtnConfiguration.getVenSessionConfig().getPullModel());
		if (getListeners() != null) {
			final OadrCreatedPartyRegistrationType reg = registration;
			getListeners().forEach(listener -> listener.onRegistrationSuccess(vtnConfiguration, reg));
		}
	}

	public OadrCreatedPartyRegistrationType getRegistration(VtnSessionConfiguration vtnConfiguration) {
		return registration.get(vtnConfiguration.getVtnId());
	}

	private void setRegistration(VtnSessionConfiguration vtnConfiguration,
			OadrCreatedPartyRegistrationType registration) {
		if (registration == null) {
			this.registration.remove(vtnConfiguration.getVtnId());
		} else {
			this.registration.put(vtnConfiguration.getVtnId(), registration);
		}
	}

	public void addListener(Oadr20bVENEiRegisterPartyServiceListener listener) {
		if (getListeners() == null) {
			setListeners(new ArrayList<Oadr20bVENEiRegisterPartyServiceListener>());
		}
		
		if(!getListeners().contains(listener)) {
			getListeners().add(listener);
		}
		
	}

	public String handle(VtnSessionConfiguration vtnConfig, String raw, OadrPayload oadrPayload)
			throws Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureException, OadrSecurityException {
		xmlSignatureService.validate(raw, oadrPayload, vtnConfig);

		if (oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration() != null) {
			LOGGER.info(vtnConfig.getVtnId() + " - OadrCancelPartyRegistrationType signed");
			return handle(vtnConfig, oadrPayload.getOadrSignedObject().getOadrCancelPartyRegistration(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrRequestReregistration() != null) {
			LOGGER.info(vtnConfig.getVtnId() + " - OadrRequestReregistrationType signed");
			return handle(vtnConfig, oadrPayload.getOadrSignedObject().getOadrRequestReregistration(), true);
		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	public String handle(VtnSessionConfiguration vtnConfig, OadrRequestReregistrationType oadrRequestReregistrationType,
			boolean signed) throws Oadr20bMarshalException, Oadr20bXMLSignatureException, OadrSecurityException {

		OadrResponseType response = this.oadrRequestReregistration(vtnConfig, oadrRequestReregistrationType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, vtnConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;

	}

	public String handle(VtnSessionConfiguration vtnConfig,
			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType, boolean signed)
			throws Oadr20bMarshalException, Oadr20bXMLSignatureException, OadrSecurityException {

		OadrCanceledPartyRegistrationType response = this.oadrCancelPartyRegistration(vtnConfig,
				oadrCancelPartyRegistrationType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, vtnConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;

	}

	public String request(String username, String payload)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException {

		Object unmarshal = jaxbContext.unmarshal(payload);

		VtnSessionConfiguration vtnConfig = multiVtnConfig.getMultiConfig(username);

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(vtnConfig, payload, oadrPayload);

		} else if (unmarshal instanceof OadrRequestReregistrationType) {

			OadrRequestReregistrationType oadrRequestReregistrationType = (OadrRequestReregistrationType) unmarshal;

			LOGGER.info(username + " - OadrRequestReregistrationType");

			return handle(vtnConfig, oadrRequestReregistrationType, false);

		} else if (unmarshal instanceof OadrCancelPartyRegistrationType) {

			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = (OadrCancelPartyRegistrationType) unmarshal;

			LOGGER.info(username + " - OadrCancelPartyRegistrationType");

			return handle(vtnConfig, oadrCancelPartyRegistrationType, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiEventService");
	}

	public List<Oadr20bVENEiRegisterPartyServiceListener> getListeners() {
		return listeners;
	}


	private void setListeners(List<Oadr20bVENEiRegisterPartyServiceListener> listeners) {
		this.listeners = listeners;
	}

}
