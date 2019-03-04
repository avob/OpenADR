package com.avob.openadr.server.oadr20b.vtn.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.model.oadr20b.dto.ReportCapabilityDescriptionDto;
import com.avob.openadr.model.oadr20b.dto.ReportCapabilityDto;
import com.avob.openadr.model.oadr20b.dto.ReportRequestDto;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;

@RestController
@RequestMapping("/Vtn")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class Oadr20bVtnController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVtnController.class);

	@Resource
	private SelfReportCapabilityService selfReportCapabilityService;

	@Resource
	private SelfReportCapabilityDescriptionService selfReportCapabilityDescriptionService;

	@Resource
	private SelfReportRequestService selfReportRequestService;

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	@ResponseBody
	public VtnConfigurationDto viewConf() throws Oadr20bMarshalException {

		VtnConfigurationDto dto = new VtnConfigurationDto();
		dto.setContextPath(vtnConfig.getContextPath());
		dto.setHost(vtnConfig.getHost());
		dto.setPort(vtnConfig.getPort());
		dto.setPullFrequencySeconds(vtnConfig.getPullFrequencySeconds());
		dto.setSupportPush(vtnConfig.getSupportPush());
		dto.setSupportUnsecuredHttpPush(vtnConfig.getSupportUnsecuredHttpPush());
		String oadr20bFingerprint;
		try {
			oadr20bFingerprint = OadrHttpSecurity.getOadr20bFingerprint(vtnConfig.getCert());
			dto.setVtnId(oadr20bFingerprint);
		} catch (OadrSecurityException e) {
			LOGGER.error("", e);
		}
		
		dto.setSupportCertificateGeneration(vtnConfig.getCaKey() != null && vtnConfig.getCaCert() != null);
		dto.setXmlSignatureReplayProtectSecond(vtnConfig.getReplayProtectAcceptedDelaySecond());
		dto.setSaveVenDate(vtnConfig.getSaveVenData());
		dto.setXsdValidation(vtnConfig.getValidateOadrPayloadAgainstXsd());
		return dto;
	}

	@RequestMapping(value = "/report/available", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportCapabilityDto> viewSelfReportCapability() throws Oadr20bMarshalException {

		return oadr20bDtoMapper.mapList(selfReportCapabilityService.findAll(), ReportCapabilityDto.class);
	}

	@RequestMapping(value = "/report/available/{reportSpecifierId}", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportCapabilityDescriptionDto> viewOtherReportCapabilityDescription(
			@PathVariable("reportSpecifierId") String reportSpecifierId, HttpServletResponse response)
			throws Oadr20bMarshalException {

		SelfReportCapability reportCapability = selfReportCapabilityService.findByReportSpecifierId(reportSpecifierId);
		if (reportCapability == null) {
			response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
			return null;
		}

		List<SelfReportCapabilityDescription> findByOtherReportCapability = selfReportCapabilityDescriptionService
				.findBySelfReportCapability(reportCapability);
		return oadr20bDtoMapper.mapList(findByOtherReportCapability, ReportCapabilityDescriptionDto.class);
	}

	@RequestMapping(value = "/report/requested", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportRequestDto> viewReportRequest() throws Oadr20bMarshalException {

		return oadr20bDtoMapper.mapList(selfReportRequestService.findAll(), ReportRequestDto.class);
	}

}
