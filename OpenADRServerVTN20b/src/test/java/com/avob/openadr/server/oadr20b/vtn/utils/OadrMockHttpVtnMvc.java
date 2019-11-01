package com.avob.openadr.server.oadr20b.vtn.utils;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.controller.VtnConfigurationDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDto;

@Service
public class OadrMockHttpVtnMvc {

	private static final String VTN_ENDPOINT = "/Vtn/";

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	public List<ReportCapabilityDto> getVtnReportAvailable(UserRequestPostProcessor authSession, int status)
			throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession, VTN_ENDPOINT + "report/available",
				status, ReportCapabilityDto.class);
	}

	public List<ReportCapabilityDescriptionDto> getVtnReportAvailableDescription(UserRequestPostProcessor authSession,
			String reportSpecifierId, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VTN_ENDPOINT + "report/available/" + reportSpecifierId, status, ReportCapabilityDescriptionDto.class);
	}

	public List<OtherReportRequestDto> getVtnReportRequested(UserRequestPostProcessor authSession, int status)
			throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession, VTN_ENDPOINT + "/report/requested",
				status, OtherReportRequestDto.class);
	}

	public VtnConfigurationDto getConfiguration(UserRequestPostProcessor authSession, int status) throws Exception {
		Class<VtnConfigurationDto> klass = VtnConfigurationDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpect(authSession, VTN_ENDPOINT + "/configuration", status,
				klass);

	}

}
