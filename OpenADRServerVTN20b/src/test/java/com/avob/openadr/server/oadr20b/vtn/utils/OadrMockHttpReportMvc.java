package com.avob.openadr.server.oadr20b.vtn.utils;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDto;

@Service
public class OadrMockHttpReportMvc {

	private static final String REPORT_ENDPOINT = "/Report/";

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	public List<OtherReportCapabilityDto> searchReportAvailable(UserRequestPostProcessor authSession,
			LinkedMultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession, REPORT_ENDPOINT + "/available/search",
				HttpStatus.OK_200, OtherReportCapabilityDto.class, params);
	}

	public List<OtherReportCapabilityDescriptionDto> searchReportAvailabledescription(
			UserRequestPostProcessor authSession, LinkedMultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				REPORT_ENDPOINT + "/available/description/search", HttpStatus.OK_200,
				OtherReportCapabilityDescriptionDto.class, params);
	}

}
