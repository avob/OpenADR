package com.avob.openadr.server.oadr20b.ven.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiReportService;
import com.avob.openadr.server.oadr20b.ven.service.XmlSignatureService;

@ConditionalOnExpression("#{!${oadr.pullModel}}")
@PreAuthorize("hasRole('ROLE_VTN')")
@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVENEiReportController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiRegisterPartyController.class);

	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private Oadr20bVENEiReportService reportService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public Oadr20bVENEiReportController() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@RequestMapping(value = Oadr20bUrlPath.EI_REPORT_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String request(@RequestBody String payload, Principal principal)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException {

		Object unmarshal = jaxbContext.unmarshal(payload);

		String username = principal.getName();

		VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(username);

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(multiConfig, oadrPayload);

		} else if (unmarshal instanceof OadrCancelReportType) {

			LOGGER.debug(payload);

			OadrCancelReportType oadrCancelReportType = (OadrCancelReportType) unmarshal;

			LOGGER.info(username + " - OadrCancelReport");

			return handle(multiConfig, oadrCancelReportType, false);

		} else if (unmarshal instanceof OadrCreateReportType) {

			LOGGER.debug(payload);

			OadrCreateReportType oadrCreateReportType = (OadrCreateReportType) unmarshal;

			LOGGER.info(username + " - OadrCreateReport");

			return handle(multiConfig, oadrCreateReportType, false);

		} else if (unmarshal instanceof OadrRegisterReportType) {

			LOGGER.debug(payload);

			OadrRegisterReportType oadrRegisterReportType = (OadrRegisterReportType) unmarshal;

			LOGGER.info(username + " - OadrRegisterReport");

			return handle(multiConfig, oadrRegisterReportType, false);

		} else if (unmarshal instanceof OadrUpdateReportType) {

			LOGGER.debug(payload);

			OadrUpdateReportType oadrUpdateReportType = (OadrUpdateReportType) unmarshal;

			LOGGER.info(username + " - OadrUpdateReport");

			return handle(multiConfig, oadrUpdateReportType, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiReport");
	}

	private String handle(VtnSessionConfiguration multiConfig, OadrUpdateReportType oadrUpdateReportType,
			boolean signed) throws Oadr20bXMLSignatureException, Oadr20bMarshalException {

		OadrUpdatedReportType response = reportService.oadrUpdateReport(multiConfig, oadrUpdateReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	private String handle(VtnSessionConfiguration multiConfig, OadrRegisterReportType oadrRegisterReportType,
			boolean signed) throws Oadr20bXMLSignatureException, Oadr20bMarshalException {

		OadrRegisteredReportType response = reportService.oadrRegisterReport(multiConfig, oadrRegisterReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	private String handle(VtnSessionConfiguration multiConfig, OadrCreateReportType oadrCreateReportType,
			boolean signed) throws Oadr20bXMLSignatureException, Oadr20bMarshalException {

		OadrCreatedReportType response = reportService.oadrCreateReport(multiConfig, oadrCreateReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	private String handle(VtnSessionConfiguration multiConfig, OadrCancelReportType oadrCancelReportType,
			boolean signed) throws Oadr20bXMLSignatureException, Oadr20bMarshalException {

		OadrCanceledReportType response = reportService.oadrCancelReport(multiConfig, oadrCancelReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	private String handle(VtnSessionConfiguration multiConfig, OadrPayload oadrPayload)
			throws Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureException {
		xmlSignatureService.validate(oadrPayload);

		if (oadrPayload.getOadrSignedObject().getOadrUpdateReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrUpdateReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrUpdateReport(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrCreateReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrCreateReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrCreateReport(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrRegisterReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrRegisterReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrRegisterReport(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrCancelReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrCancelReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrCancelReport(), true);
		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiReport");
	}

}
