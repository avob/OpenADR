package com.avob.openadr.server.oadr20b.vtn.utils;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDto;

@Service
public class OadrMockHttpVtnMvc {

	private static final String VTN_ENDPOINT = "/Vtn/";

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	public List<ReportCapabilityDto> getVtnReportAvailable(UserRequestPostProcessor authSession) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession, VTN_ENDPOINT + "report/available",
				HttpStatus.OK_200, ReportCapabilityDto.class);
	}

	public List<ReportCapabilityDescriptionDto> getVtnReportAvailableDescription(UserRequestPostProcessor authSession,
			String reportSpecifierId) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VTN_ENDPOINT + "report/available/" + reportSpecifierId, HttpStatus.OK_200,
				ReportCapabilityDescriptionDto.class);
	}

	public List<OtherReportRequestDto> getVtnReportRequested(UserRequestPostProcessor authSession) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession, VTN_ENDPOINT + "/report/requested",
				HttpStatus.OK_200, OtherReportRequestDto.class);
	}

}
