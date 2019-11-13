package com.avob.openadr.server.oadr20b.vtn.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.ReportRequestDto;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;

@CrossOrigin
@RestController
@RequestMapping("/Vtn")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class Oadr20bVtnController {

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
	public VtnConfigurationDto viewConf() throws Oadr20bMarshalException, OadrSecurityException {

		VtnConfigurationDto dto = oadr20bDtoMapper.map(vtnConfig, VtnConfigurationDto.class);
		dto.setVtnId(vtnConfig.getOadr20bFingerprint());
		dto.setSupportCertificateGeneration(vtnConfig.getCaKey() != null && vtnConfig.getCaCert() != null);
		dto.setXmlSignatureReplayProtectSecond(vtnConfig.getReplayProtectAcceptedDelaySecond());
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
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		SelfReportCapability reportCapability = selfReportCapabilityService.findByReportSpecifierId(reportSpecifierId);
		if (reportCapability == null) {
			throw new OadrElementNotFoundException(
					"Self ReportCapabilityDescription with reporSpecifierId: " + reportSpecifierId + " not found");
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
