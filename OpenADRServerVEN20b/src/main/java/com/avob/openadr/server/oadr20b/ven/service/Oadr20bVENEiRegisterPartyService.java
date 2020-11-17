package com.avob.openadr.server.oadr20b.ven.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatePartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENEiRegisterPartyService implements Oadr20bVENEiService {

	private static final String EI_SERVICE_NAME = "EiRegisterParty";

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiRegisterPartyService.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bPollService oadrPollService;

	private Map<String, OadrCreatedPartyRegistrationType> registration = new ConcurrentHashMap<String, OadrCreatedPartyRegistrationType>();

	private List<Oadr20bVENEiRegisterPartyServiceListener> listeners;

	public interface Oadr20bVENEiRegisterPartyServiceListener {
		public void onRegistrationSuccess(VtnSessionConfiguration vtnConfiguration,
				OadrCreatedPartyRegistrationType registration);

		public void onRegistrationError(VtnSessionConfiguration vtnConfiguration,
				OadrCreatedPartyRegistrationType registration);
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
			oadrPollService.cancelPoll(vtnConfiguration, false);
		} else {
			responseCode = Oadr20bApplicationLayerErrorCode.INVALID_ID_452;
		}

		return Oadr20bEiRegisterPartyBuilders.newOadr20bCanceledPartyRegistrationBuilder(
				Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestID, responseCode).build(), registrationID,
				venID).build();
	}

	public void clearRegistration(VtnSessionConfiguration vtnConfiguration) {
		setRegistration(vtnConfiguration, null);
	}

	public void reinitRegistration(VtnSessionConfiguration vtnConfiguration) {
		clearRegistration(vtnConfiguration);
		oadrPollService.cancelPoll(vtnConfiguration, true);
		this.initRegistration(vtnConfiguration);
	}

	public void initRegistration(VtnSessionConfiguration vtnConfiguration) {

		String requestId = "0";

		try {
			if (vtnConfiguration.getVtnUrl() != null) {
				OadrQueryRegistrationType queryRegistration = Oadr20bEiRegisterPartyBuilders
						.newOadr20bQueryRegistrationBuilder(requestId)
						.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value()).build();

				OadrCreatedPartyRegistrationType oadrQueryRegistrationType = multiVtnConfig
						.getMultiHttpClientConfig(vtnConfiguration).oadrQueryRegistrationType(queryRegistration);

				this.oadrCreatedPartyRegistration(vtnConfiguration, oadrQueryRegistrationType);
			} else {

				OadrCreatePartyRegistrationType createPartyRegistration = Oadr20bEiRegisterPartyBuilders
						.newOadr20bCreatePartyRegistrationBuilder(requestId,
								vtnConfiguration.getVenId(), "xmpp")
						.withOadrTransportName(OadrTransportType.XMPP)
						.withOadrTransportAddress(
								multiVtnConfig.getMultiXmppClientConfig(vtnConfiguration).getConnectionJid())
						.withOadrReportOnly(vtnConfiguration.getReportOnly())
						.withOadrVenName(vtnConfiguration.getVenName())
						.withOadrXmlSignature(vtnConfiguration.getXmlSignature())
						.withOadrHttpPullModel(vtnConfiguration.getPullModel()).build();

				multiVtnConfig.getMultiXmppClientConfig(vtnConfiguration)
						.oadrCreatePartyRegistration(createPartyRegistration);
			}

		} catch (Oadr20bException | Oadr20bHttpLayerException | Oadr20bXMLSignatureException
				| Oadr20bXMLSignatureValidationException | Oadr20bMarshalException | OadrSecurityException | IOException
				| NotConnectedException e) {
			LOGGER.error("Fail to query registration", e);
		} catch (InterruptedException e) {
			LOGGER.error("Fail to query registration", e);
			Thread.currentThread().interrupt();
		}

	}

	public void register(VtnSessionConfiguration vtnConfiguration, OadrCreatedPartyRegistrationType registration) {
		if (registration.getRegistrationID() != null) {
			setRegistration(vtnConfiguration, registration);

			if (vtnConfiguration.getPullModel()) {
				oadrPollService.initPoll(vtnConfiguration, registration.getOadrRequestedOadrPollFreq());
			}

			LOGGER.info("Ven has successfully register using registrationId: " + registration.getRegistrationID());
			LOGGER.debug("        xmlSignature: " + vtnConfiguration.getXmlSignature());
			LOGGER.debug("        reportOnly  : " + vtnConfiguration.getReportOnly());
			LOGGER.debug("        pullModel   : " + vtnConfiguration.getPullModel());

			if (getListeners() != null) {
				final OadrCreatedPartyRegistrationType reg = registration;
				getListeners().forEach(listener -> listener.onRegistrationSuccess(vtnConfiguration, reg));
			}
		} else {
			LOGGER.info("Ven cannot register to vtn: " + vtnConfiguration.getVtnId());

			if (getListeners() != null) {
				final OadrCreatedPartyRegistrationType reg = registration;
				getListeners().forEach(listener -> listener.onRegistrationError(vtnConfiguration, reg));
			}
		}

	}

	public OadrCreatedPartyRegistrationType getRegistration(VtnSessionConfiguration vtnConfiguration) {
		return registration.get(vtnConfiguration.getSessionId());
	}

	private void setRegistration(VtnSessionConfiguration vtnConfiguration,
			OadrCreatedPartyRegistrationType registration) {
		if (registration == null) {
			this.registration.remove(vtnConfiguration.getSessionId());
		} else {
			this.registration.put(vtnConfiguration.getSessionId(), registration);
		}
	}

	public void addListener(Oadr20bVENEiRegisterPartyServiceListener listener) {
		if (getListeners() == null) {
			setListeners(new ArrayList<Oadr20bVENEiRegisterPartyServiceListener>());
		}

		if (!getListeners().contains(listener)) {
			getListeners().add(listener);
		}

	}

	public void oadrCreatedPartyRegistration(VtnSessionConfiguration vtnConfig,
			OadrCreatedPartyRegistrationType oadrCreatedPartyRegistrationType)
			throws Oadr20bMarshalException, Oadr20bXMLSignatureException, OadrSecurityException, IOException {

		if (getRegistration(vtnConfig) != null && getRegistration(vtnConfig).getRegistrationID() != null
				&& getRegistration(vtnConfig).getRegistrationID()
						.equals(oadrCreatedPartyRegistrationType.getRegistrationID())) {
			return;
		} else if (oadrCreatedPartyRegistrationType.getRegistrationID() != null) {
			this.register(vtnConfig, oadrCreatedPartyRegistrationType);
		}

		String venName = vtnConfig.getVenName();

		boolean xmlSignature = vtnConfig.getXmlSignature();
		boolean reportOnly = vtnConfig.getReportOnly();
		String requestId = "";
		Oadr20bCreatePartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, vtnConfig.getVenId(),
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withRegistrationId(oadrCreatedPartyRegistrationType.getRegistrationID());

		if (vtnConfig.getVtnUrl() != null) {

			Boolean pullModel = vtnConfig.getPullModel();
			String transportAddress = null;
			if (!pullModel) {
				transportAddress = vtnConfig.getVenUrl();
			}

			OadrTransportType transportType = OadrTransportType.SIMPLE_HTTP;

			builder.withOadrHttpPullModel(pullModel).withOadrTransportAddress(transportAddress)
					.withOadrReportOnly(reportOnly).withOadrTransportName(transportType).withOadrVenName(venName)

					.withOadrXmlSignature(xmlSignature);

		} else if (vtnConfig.getVtnXmppHost() != null && vtnConfig.getVtnXmppPort() != null) {

			OadrXmppVenClient20b xmppClient = multiVtnConfig.getMultiXmppClientConfig(vtnConfig);

			String transportAddress = xmppClient.getConnectionJid();

			OadrTransportType transportType = OadrTransportType.XMPP;

			builder.withOadrTransportAddress(transportAddress).withOadrReportOnly(reportOnly)
					.withOadrHttpPullModel(false).withOadrTransportName(transportType).withOadrVenName(venName)
					.withOadrXmlSignature(xmlSignature);

		}

		OadrCreatePartyRegistrationType createPartyRegistration = builder.build();

		try {

			if (vtnConfig.getVtnUrl() != null) {
				OadrCreatedPartyRegistrationType oadrQueryRegistrationType = multiVtnConfig
						.getMultiHttpClientConfig(vtnConfig).oadrCreatePartyRegistration(createPartyRegistration);
				this.oadrCreatedPartyRegistration(vtnConfig, oadrQueryRegistrationType);
			} else {
				multiVtnConfig.getMultiXmppClientConfig(vtnConfig).oadrCreatePartyRegistration(createPartyRegistration);
			}

		} catch (NotConnectedException | Oadr20bException | Oadr20bHttpLayerException
				| Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("", e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
			Thread.currentThread().interrupt();
		}

	}

	public Object request(VtnSessionConfiguration vtnConfig, Object unmarshal) {

		if (unmarshal instanceof OadrRequestReregistrationType) {

			OadrRequestReregistrationType oadrRequestReregistrationType = (OadrRequestReregistrationType) unmarshal;

			LOGGER.info(vtnConfig.getVtnId() + " - OadrRequestReregistrationType");

			return oadrRequestReregistration(vtnConfig, oadrRequestReregistrationType);

		} else if (unmarshal instanceof OadrCancelPartyRegistrationType) {

			OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = (OadrCancelPartyRegistrationType) unmarshal;

			LOGGER.info(vtnConfig.getVtnId() + " - OadrCancelPartyRegistrationType");

			return oadrCancelPartyRegistration(vtnConfig, oadrCancelPartyRegistrationType);

		} else if (unmarshal instanceof OadrCreatedPartyRegistrationType) {

			OadrCreatedPartyRegistrationType oadrCreatedPartyRegistrationType = (OadrCreatedPartyRegistrationType) unmarshal;

			LOGGER.info(vtnConfig.getVtnId() + " - OadrCreatedPartyRegistrationType");

			this.register(vtnConfig, oadrCreatedPartyRegistrationType);

			return null;

		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453,
						vtnConfig.getVtnId())
				.withDescription("Unknown payload type for service: " + this.getServiceName()).build();
	}

	public List<Oadr20bVENEiRegisterPartyServiceListener> getListeners() {
		return listeners;
	}

	private void setListeners(List<Oadr20bVENEiRegisterPartyServiceListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

}
