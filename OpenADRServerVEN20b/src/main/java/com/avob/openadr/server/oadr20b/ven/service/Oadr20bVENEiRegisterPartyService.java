package com.avob.openadr.server.oadr20b.ven.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatePartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
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
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VenConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENEiRegisterPartyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiRegisterPartyService.class);

	@Resource
	protected VenConfig venConfig;

	@Resource
	protected MultiVtnConfig multiVtnConfig;

	@Resource
	protected StatePersistenceService statePersistenceService;

	private Map<String, OadrCreatedPartyRegistrationType> registration = new ConcurrentHashMap<String, OadrCreatedPartyRegistrationType>();

	protected List<Oadr20bVENEiRegisterPartyServiceListener> listeners;

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
			// pollService.cancelHttpScheduledPullRequestTask(vtnConfiguration,
			// false);
		} else {
			responseCode = Oadr20bApplicationLayerErrorCode.INVALID_ID_452;
		}

		return Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(requestID, responseCode, registrationID, venID).build();

	}

	public void clearRegistration(VtnSessionConfiguration vtnConfiguration) {
		try {
			statePersistenceService.deleteRegistration(venConfig.getVenId(), vtnConfiguration);
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
			loadRegistration = statePersistenceService.loadRegistration(venConfig.getVenId(), vtnConfiguration);

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
					.newOadr20bCreateReportBuilder(requestId, venConfig.getVenId())
					.addReportRequest(oadrReportRequestType).build();

			multiVtnConfig.getMultiClientConfig(vtnConfiguration).oadrCreateReport(build);

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
					statePersistenceService.deleteRegistration(venConfig.getVenId(), vtnConfiguration);
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
		}

		if (loadRegistration != null) {

			if (isSameConfig(vtnConfiguration)) {
				LOGGER.debug("Ven already registered using registrationId: " + loadRegistration.getRegistrationID());
				LOGGER.debug("        xmlSignature: " + venConfig.getXmlSignature());
				LOGGER.debug("        reportOnly  : " + venConfig.getReportOnly());
				LOGGER.debug("        pullModel   : " + venConfig.getPullModel());
				setRegistration(vtnConfiguration, loadRegistration);
				if (listeners != null) {
					final OadrCreatedPartyRegistrationType reg = loadRegistration;
					listeners.forEach(listener -> listener.onRegistrationSuccess(vtnConfiguration, reg));
				}
				return;
			} else {
				this.reinitRegistration(vtnConfiguration);
			}

		}

		LOGGER.info("Ven is not yet registered");

		Boolean pullModel = venConfig.getPullModel();
		String transportAddress = null;
		if (!pullModel) {
			transportAddress = venConfig.getVenUrl();
		}
		boolean reportOnly = venConfig.getReportOnly();
		OadrTransportType transportType = OadrTransportType.SIMPLE_HTTP;
		String venName = venConfig.getVenName();
		boolean xmlSignature = venConfig.getXmlSignature();
		String requestId = "";
		Oadr20bCreatePartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, venConfig.getVenId(),
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrHttpPullModel(pullModel).withOadrTransportAddress(transportAddress)
				.withOadrReportOnly(reportOnly).withOadrTransportName(transportType).withOadrVenName(venName)
				.withOadrXmlSignature(xmlSignature);
		if (registrationId != null) {
			builder.withRegistrationId(registrationId);
		}

		OadrCreatePartyRegistrationType createPartyRegistration = builder.build();

		try {

			LOGGER.info("Ven try to register...");

			loadRegistration = multiVtnConfig.getMultiClientConfig(vtnConfiguration)
					.oadrCreatePartyRegistration(createPartyRegistration);

			if (loadRegistration.getEiResponse().getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
				statePersistenceService.persistRegistration(venConfig.getVenId(), vtnConfiguration, loadRegistration);
				LOGGER.info(
						"Ven has successfully register using registrationId: " + loadRegistration.getRegistrationID());
				LOGGER.debug("        xmlSignature: " + venConfig.getXmlSignature());
				LOGGER.debug("        reportOnly  : " + venConfig.getReportOnly());
				LOGGER.debug("        pullModel   : " + venConfig.getPullModel());
			} else {
				LOGGER.error("Ven has failed to register - responseCode: "
						+ loadRegistration.getEiResponse().getResponseCode() + ", responseDescription: "
						+ loadRegistration.getEiResponse().getResponseDescription());
			}

			setRegistration(vtnConfiguration, loadRegistration);

			if (listeners != null) {
				final OadrCreatedPartyRegistrationType reg = loadRegistration;
				listeners.forEach(listener -> listener.onRegistrationSuccess(vtnConfiguration, reg));
			}
			return;

		} catch (Oadr20bHttpLayerException e) {
			LOGGER.error("Fail to create registration: HttpLayerException[" + e.getErrorCode() + "]: "
					+ e.getErrorMessage());

		} catch (Oadr20bMarshalException e) {
			LOGGER.error("Fail to create registration", e);
		} catch (Oadr20bException e) {
			LOGGER.error("Fail to create registration", e);
		} catch (IOException e) {
			LOGGER.error("Fail to create registration", e);
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error("Fail to sign request payload", e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("Fail to validate response xml signature", e);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		if (listeners != null) {
			listeners.forEach(listener -> listener.onRegistrationError(vtnConfiguration));
		}
		setRegistration(vtnConfiguration, null);
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
		if (listeners == null) {
			listeners = new ArrayList<Oadr20bVENEiRegisterPartyServiceListener>();
		}
		listeners.add(listener);
	}

}
