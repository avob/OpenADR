package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENEiReportService implements Oadr20bVENEiService{
	
	private static final String EI_SERVICE_NAME = "EiReport";

	protected static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiReportService.class);

	protected static final String METADATA_REPORT_SPECIFIER_ID = "METADATA";
	protected static final String METADATA_REPORT_RID = "METADATA";
	protected static final String NO_GRANULARITY = "P0D";

	@Resource
	private PayloadHandler payloadHandler;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public OadrRegisterReportType selfOadrRegisterReport(String requestId, String venId, String reportRequestId) {
		return Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId, venId, reportRequestId).build();
	}

	public OadrUpdatedReportType oadrUpdateReport(VtnSessionConfiguration vtnConfig,
			OadrUpdateReportType oadrUpdateReportType) {

		String requestID = oadrUpdateReportType.getRequestID();

		return Oadr20bEiReportBuilders.newOadr20bUpdatedReportBuilder(requestID,
				Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, vtnConfig.getVenSessionConfig().getVenId())
				.build();
	}

	public OadrRegisteredReportType oadrRegisterReport(VtnSessionConfiguration vtnConfig,
			OadrRegisterReportType oadrRegisterReportType) {

		String requestID = oadrRegisterReportType.getRequestID();

		return Oadr20bEiReportBuilders.newOadr20bRegisteredReportBuilder(requestID, HttpStatus.OK_200,
				vtnConfig.getVenSessionConfig().getVenId()).build();

	}

	public OadrCreatedReportType oadrCreateReport(VtnSessionConfiguration vtnConfig,
			OadrCreateReportType oadrCreateReportType) {

		String requestID = oadrCreateReportType.getRequestID();

		return Oadr20bEiReportBuilders.newOadr20bCreatedReportBuilder(requestID,
				Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, vtnConfig.getVenSessionConfig().getVenId())
				.build();
	}

	public void oadrCreatedReport(VtnSessionConfiguration vtnConfig, OadrCreatedReportType oadrCreatedReportType) {

	}

	public OadrCanceledReportType oadrCancelReport(VtnSessionConfiguration vtnConfig,
			OadrCancelReportType oadrCancelReportType) {

		String requestID = oadrCancelReportType.getRequestID();

		return Oadr20bEiReportBuilders.newOadr20bCanceledReportBuilder(requestID,
				Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, vtnConfig.getVenSessionConfig().getVenId())
				.build();
	}

	public Object handle(VtnSessionConfiguration multiConfig, OadrPayload oadrPayload)
			throws Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureException, OadrSecurityException {

		if (oadrPayload.getOadrSignedObject().getOadrUpdateReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrUpdateReport signed");
			return oadrUpdateReport(multiConfig, oadrPayload.getOadrSignedObject().getOadrUpdateReport());
		} else if (oadrPayload.getOadrSignedObject().getOadrCreateReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrCreateReport signed");
			return oadrCreateReport(multiConfig, oadrPayload.getOadrSignedObject().getOadrCreateReport());
		} else if (oadrPayload.getOadrSignedObject().getOadrRegisterReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrRegisterReport signed");
			return oadrRegisterReport(multiConfig, oadrPayload.getOadrSignedObject().getOadrRegisterReport());
		} else if (oadrPayload.getOadrSignedObject().getOadrCancelReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrCancelReport signed");
			return oadrCancelReport(multiConfig, oadrPayload.getOadrSignedObject().getOadrCancelReport());
		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiReport");
	}

	@Override
	public Object request(VtnSessionConfiguration multiConfig, Object unmarshal) {

		if (unmarshal instanceof OadrCancelReportType) {

			OadrCancelReportType oadrCancelReportType = (OadrCancelReportType) unmarshal;

			LOGGER.info(multiConfig.getVtnId() + " - OadrCancelReport");

			return oadrCancelReport(multiConfig, oadrCancelReportType);

		} else if (unmarshal instanceof OadrCreateReportType) {

			OadrCreateReportType oadrCreateReportType = (OadrCreateReportType) unmarshal;

			LOGGER.info(multiConfig.getVtnId() + " - OadrCreateReport");

			return oadrCreateReport(multiConfig, oadrCreateReportType);

		} else if (unmarshal instanceof OadrRegisterReportType) {

			OadrRegisterReportType oadrRegisterReportType = (OadrRegisterReportType) unmarshal;

			LOGGER.info(multiConfig.getVtnId() + " - OadrRegisterReport");

			return oadrRegisterReport(multiConfig, oadrRegisterReportType);

		} else if (unmarshal instanceof OadrUpdateReportType) {

			OadrUpdateReportType oadrUpdateReportType = (OadrUpdateReportType) unmarshal;

			LOGGER.info(multiConfig.getVtnId() + " - OadrUpdateReport");

			return oadrUpdateReport(multiConfig, oadrUpdateReportType);

		} else if (unmarshal instanceof OadrCanceledReportType) {

			LOGGER.info(multiConfig.getVtnId() + " - OadrCanceledReportType");

			return null;

		} else if (unmarshal instanceof OadrCreatedReportType) {

			LOGGER.info(multiConfig.getVtnId() + " - OadrCreatedReportType");

			return null;

		} else if (unmarshal instanceof OadrRegisteredReportType) {

			LOGGER.info(multiConfig.getVtnId() + " - OadrRegisteredReportType");

			return null;

		} else if (unmarshal instanceof OadrUpdatedReportType) {

			LOGGER.info(multiConfig.getVtnId() + " - OadrUpdatedReportType");

			return null;

		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, multiConfig.getVtnId())
				.withDescription("Unknown payload type for service: " + this.getServiceName()).build();
	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

}
