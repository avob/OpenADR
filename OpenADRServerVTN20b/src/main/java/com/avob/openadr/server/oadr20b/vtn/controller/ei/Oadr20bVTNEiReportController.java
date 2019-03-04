package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import java.security.Principal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCancelPartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bCreatePartyRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty.Oadr20bQueryRegistrationTypeApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCancelReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreatedReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bRegisterReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bUpdateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNEiReportController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiReportController.class);

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bVTNEiReportService reportService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@PreAuthorize("hasRole('ROLE_VEN') AND hasRole('ROLE_REGISTERED')")
	@RequestMapping(value = Oadr20bUrlPath.EI_REPORT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bUnmarshalException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bCreatePartyRegistrationTypeApplicationLayerException,
			Oadr20bCancelPartyRegistrationTypeApplicationLayerException,
			Oadr20bQueryRegistrationTypeApplicationLayerException, Oadr20bRegisterReportApplicationLayerException,
			Oadr20bUpdateReportApplicationLayerException, Oadr20bCancelReportApplicationLayerException,
			Oadr20bCreateReportApplicationLayerException, Oadr20bCreatedReportApplicationLayerException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException {

		Object unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());

		String username = principal.getName();

		if (unmarshal instanceof OadrPayload) {

			OadrPayload obj = (OadrPayload) unmarshal;

			return handle(username, payload, obj);

		} else if (unmarshal instanceof OadrRegisterReportType) {

			LOGGER.info(username + " - OadrRegisterReport");

			OadrRegisterReportType obj = (OadrRegisterReportType) unmarshal;

			return handle(username, obj, false);

		} else if (unmarshal instanceof OadrUpdateReportType) {

			LOGGER.info(username + " - OadrUpdateReport");

			OadrUpdateReportType obj = (OadrUpdateReportType) unmarshal;

			return handle(username, obj, false);

		} else if (unmarshal instanceof OadrCreatedReportType) {

			LOGGER.info(username + " - OadrCreatedReport");

			OadrCreatedReportType obj = (OadrCreatedReportType) unmarshal;

			return handle(username, obj, false);

		} else if (unmarshal instanceof OadrCreateReportType) {

			LOGGER.info(username + " - OadrCreateReport");

			OadrCreateReportType obj = (OadrCreateReportType) unmarshal;

			return handle(username, obj, false);

		} else if (unmarshal instanceof OadrCancelReportType) {

			LOGGER.info(username + " - OadrCancelReport");

			OadrCancelReportType obj = (OadrCancelReportType) unmarshal;

			return handle(username, obj, false);

		}

		return null;
	}

	private String handle(String username, String raw, OadrPayload oadrPayload)
			throws Oadr20bMarshalException, Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException,
			Oadr20bCreatePartyRegistrationTypeApplicationLayerException,
			Oadr20bCancelPartyRegistrationTypeApplicationLayerException,
			Oadr20bQueryRegistrationTypeApplicationLayerException, Oadr20bRegisterReportApplicationLayerException,
			Oadr20bUpdateReportApplicationLayerException, Oadr20bCancelReportApplicationLayerException,
			Oadr20bCreateReportApplicationLayerException, Oadr20bCreatedReportApplicationLayerException,
			Oadr20bXMLSignatureException {

		xmlSignatureService.validate(raw, oadrPayload);

		if (oadrPayload.getOadrSignedObject().getOadrCancelReport() != null) {

			LOGGER.info(username + " - OadrCancelReport signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCancelReport(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrCreateReport() != null) {

			LOGGER.info(username + " - OadrCreateReport signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCreateReport(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrCreatedReport() != null) {

			LOGGER.info(username + " - OadrCreatedReport signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrCreatedReport(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrUpdateReport() != null) {

			LOGGER.info(username + " - OadrUpdateReport signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrUpdateReport(), true);

		} else if (oadrPayload.getOadrSignedObject().getOadrRegisterReport() != null) {

			LOGGER.info(username + " - OadrRegisterReport signed");

			return handle(username, oadrPayload.getOadrSignedObject().getOadrRegisterReport(), true);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiReport");
	}

	private String handle(String username, OadrRegisterReportType oadrRegisterReport, boolean signed)
			throws Oadr20bRegisterReportApplicationLayerException, Oadr20bMarshalException,
			Oadr20bXMLSignatureException {

		reportService.checkMatchUsernameWithRequestVenId(username, oadrRegisterReport);

		return reportService.oadrRegisterReport(oadrRegisterReport, signed);

	}

	private String handle(String username, OadrUpdateReportType oadrUpdateReport, boolean signed)
			throws Oadr20bUpdateReportApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

		reportService.checkMatchUsernameWithRequestVenId(username, oadrUpdateReport);

		return reportService.oadrUpdateReport(oadrUpdateReport, signed);

	}

	private String handle(String username, OadrCreatedReportType oadrCreatedReport, boolean signed)
			throws Oadr20bCreatedReportApplicationLayerException, Oadr20bMarshalException,
			Oadr20bXMLSignatureException {

		reportService.checkMatchUsernameWithRequestVenId(username, oadrCreatedReport);

		return reportService.oadrCreatedReport(oadrCreatedReport, signed);

	}

	private String handle(String username, OadrCreateReportType oadrCreateReport, boolean signed)
			throws Oadr20bCreateReportApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {
		reportService.checkMatchUsernameWithRequestVenId(username, oadrCreateReport);

		return reportService.oadrCreateReport(oadrCreateReport, signed);

	}

	private String handle(String username, OadrCancelReportType oadrCancelReport, boolean signed)
			throws Oadr20bCancelReportApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {
		reportService.checkMatchUsernameWithRequestVenId(username, oadrCancelReport);

		return reportService.oadrCancelReport(oadrCancelReport, signed);

	}

}
