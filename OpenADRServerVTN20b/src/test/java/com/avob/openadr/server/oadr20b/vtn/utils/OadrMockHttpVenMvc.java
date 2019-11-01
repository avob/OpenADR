package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOptDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloatDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyTokenDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatusDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateSubscriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierSearchCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OadrMockHttpVenMvc {

	private static final String APPLICATION_JSON_HEADER_VALUE = "application/json";
	private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

	private static final String VEN_ENDPOINT = "/Ven/";

	private ObjectMapper mapper = new ObjectMapper();

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	public List<ReportCapabilityDto> getVenReportAvailable(UserRequestPostProcessor authSession, String venId,
			MultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/available", HttpStatus.OK_200, ReportCapabilityDto.class, params);
	}

	public List<ReportCapabilityDescriptionDto> getVenReportAvailableDescription(UserRequestPostProcessor authSession,
			String venId, MultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/available/description", HttpStatus.OK_200,
				ReportCapabilityDescriptionDto.class, params);
	}

	public List<OtherReportCapabilityDto> searchVenReportAvailable(UserRequestPostProcessor authSession, String venId,
			LinkedMultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/available/search", HttpStatus.OK_200, OtherReportCapabilityDto.class,
				params);
	}

	public List<OtherReportRequestDto> getVenReportRequested(UserRequestPostProcessor authSession, String venId,
			LinkedMultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/requested", HttpStatus.OK_200, OtherReportRequestDto.class, params);
	}

	public List<OtherReportRequestDto> searchVenReportRequested(UserRequestPostProcessor authSession, String venId,
			LinkedMultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/requested/search", HttpStatus.OK_200, OtherReportRequestDto.class,
				params);
	}

	public List<OtherReportRequestSpecifierDto> searchVenReportRequestedSpecifier(UserRequestPostProcessor authSession,
			String venId, OtherReportRequestSpecifierSearchCriteria criteria) throws Exception {
		MvcResult andReturn = oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(VEN_ENDPOINT + venId + "/report/requested/specifier")
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE)
						.content(mapper.writeValueAsString(criteria)).with(authSession))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();
		return Oadr20bTestUtils.convertMvcResultToDtoList(andReturn, OtherReportRequestSpecifierDto.class);
	}

	public List<OtherReportDataFloatDto> getVenReportRequestedFloatData(UserRequestPostProcessor authSession,
			String venId, String reportSpecifierId) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/float/" + reportSpecifierId, HttpStatus.OK_200,
				OtherReportDataFloatDto.class);
	}

	public List<OtherReportDataFloatDto> getVenReportRequestedSpecifierFloatData(UserRequestPostProcessor authSession,
			String venId, String reportSpecifierId, String rid) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/float/" + reportSpecifierId + "/rid/" + rid, HttpStatus.OK_200,
				OtherReportDataFloatDto.class);
	}

	public List<OtherReportDataPayloadResourceStatusDto> getVenReportRequestedResourceStatusData(
			UserRequestPostProcessor authSession, String venId, String reportSpecifierId) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/resourcestatus/" + reportSpecifierId, HttpStatus.OK_200,
				OtherReportDataPayloadResourceStatusDto.class);
	}

	public List<OtherReportDataPayloadResourceStatusDto> getVenReportRequestedSpecifierResourceStatusData(
			UserRequestPostProcessor authSession, String venId, String reportSpecifierId, String rid) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/resourcestatus/" + reportSpecifierId + "/rid/" + rid,
				HttpStatus.OK_200, OtherReportDataPayloadResourceStatusDto.class);
	}

	public List<OtherReportDataKeyTokenDto> getVenReportRequestedKeyTokenData(UserRequestPostProcessor authSession,
			String venId, String reportSpecifierId) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/keytoken/" + reportSpecifierId, HttpStatus.OK_200,
				OtherReportDataKeyTokenDto.class);
	}

	public List<OtherReportDataKeyTokenDto> getVenReportRequestedSpecifierKeyTokenData(
			UserRequestPostProcessor authSession, String venId, String reportSpecifierId, String rid) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/keytoken/" + reportSpecifierId + "/rid/" + rid, HttpStatus.OK_200,
				OtherReportDataKeyTokenDto.class);
	}

	public void subscribe(UserRequestPostProcessor authSession, String venId,
			List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions) throws JsonProcessingException, Exception {
		oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(VEN_ENDPOINT + venId + "/report/available/description/subscribe")
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE)
						.content(mapper.writeValueAsString(subscriptions)).with(authSession))
				.andExpect(status().is(HttpStatus.OK_200));
	}

	public void request(UserRequestPostProcessor authSession, String venId,
			List<OtherReportRequestDtoCreateRequestDto> requests) throws JsonProcessingException, Exception {
		oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(VEN_ENDPOINT + venId + "/report/available/description/request")
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE)
						.content(mapper.writeValueAsString(requests)).with(authSession))
				.andExpect(status().is(HttpStatus.OK_200));
	}

	public void cancelSubscription(UserRequestPostProcessor authSession, String venId, String reportRequestId)
			throws Exception {
		LinkedMultiValueMap<String, String> params = OadrParamBuilder.builder().addReportRequestId(reportRequestId)
				.build();
		oadrMockHttpMvc.perform(MockMvcRequestBuilders
				.post(VEN_ENDPOINT + venId + "/report/requested/cancelSubscription")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).params(params).with(authSession))
				.andExpect(status().is(HttpStatus.OK_200));
	}

	public List<VenOptDto> getVenOpt(UserRequestPostProcessor authSession, String venId,
			LinkedMultiValueMap<String, String> params) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession, VEN_ENDPOINT + venId + "/opt",
				HttpStatus.OK_200, VenOptDto.class, params);
	}

}
