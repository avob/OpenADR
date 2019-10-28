package com.avob.openadr.server.oadr20b.vtn.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescriptionSpecification;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;

@CrossOrigin
@RestController
@RequestMapping("/Report")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class ReportController {

	@Resource
	private VenService venService;

	@Resource
	private OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	private OtherReportCapabilityDescriptionService otherReportCapabilityDescriptionService;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

	@RequestMapping(value = "/available/search", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportCapabilityDto> viewOtherReportCapability(
			@RequestParam(value = "venID", required = false) List<String> venID,
			@RequestParam(value = "reportSpecifierId", required = false) String reportSpecifierId,
			HttpServletResponse response) throws Oadr20bMarshalException, OadrElementNotFoundException {

		List<OtherReportCapability> report = new ArrayList<>();
		if (venID != null && reportSpecifierId == null) {
			report = otherReportCapabilityService.findBySourceUsernameIn(venID);
		} else if (venID == null && reportSpecifierId != null) {
			report = otherReportCapabilityService.findByReportSpecifierId(reportSpecifierId);
		} else if (venID != null && reportSpecifierId != null) {
			report = otherReportCapabilityService.findBySourceUsernameInAndReportSpecifierId(venID, reportSpecifierId);
		} else {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
		}

		return oadr20bDtoMapper.mapList(report, OtherReportCapabilityDto.class);
	}

	@RequestMapping(value = "/available/description/search", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportCapabilityDescriptionDto> searchOtherReportCapabilityDescription(
			@RequestParam(value = "reportSpecifierId", required = false) String reportSpecifierId,
			@RequestParam(value = "reportName", required = false) ReportNameEnumeratedType reportName,
			@RequestParam(value = "reportType", required = false) ReportEnumeratedType reportType,
			@RequestParam(value = "readingType", required = false) ReadingTypeEnumeratedType readingType)

			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Specification<OtherReportCapabilityDescription> hasReportspecifierId = (reportSpecifierId != null)
				? OtherReportCapabilityDescriptionSpecification.hasReportspecifierId(reportSpecifierId)
				: null;
		Specification<OtherReportCapabilityDescription> hasReadingType = (readingType != null)
				? OtherReportCapabilityDescriptionSpecification.hasReadingType(readingType)
				: null;
		Specification<OtherReportCapabilityDescription> hasReportName = (reportName != null)
				? OtherReportCapabilityDescriptionSpecification.hasReportName(reportName)
				: null;
		Specification<OtherReportCapabilityDescription> hasReportType = (reportType != null)
				? OtherReportCapabilityDescriptionSpecification.hasReportType(reportType)
				: null;

		Specification<OtherReportCapabilityDescription> spec = Specification.where(hasReportspecifierId)
				.and(hasReadingType).and(hasReportName).and(hasReportType);

		List<OtherReportCapabilityDescription> search = otherReportCapabilityDescriptionService.search(spec);

		return oadr20bDtoMapper.mapList(search, OtherReportCapabilityDescriptionDto.class);
	}
}
