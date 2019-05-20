package com.avob.openadr.server.oadr20b.vtn.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;

@RestController
@RequestMapping("/Report")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class ReportController {

	@Resource
	private VenService venService;

	@Resource
	private OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

	@RequestMapping(value = "/available/search", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportCapabilityDto> viewOtherReportCapability(@RequestParam("venID") List<String> venID,
			@RequestParam(value = "reportSpecifierId", required = false) String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		List<OtherReportCapability> findBySourceUsernameIn = otherReportCapabilityService.findBySourceUsernameIn(venID);

		return oadr20bDtoMapper.mapList(findBySourceUsernameIn, OtherReportCapabilityDto.class);
	}
}
