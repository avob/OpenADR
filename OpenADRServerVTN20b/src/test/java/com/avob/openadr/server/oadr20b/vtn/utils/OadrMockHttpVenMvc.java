package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.avob.openadr.server.common.vtn.models.ven.VenDto;
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

	public VenDto getVen(UserRequestPostProcessor authSession, String venId, int status) throws Exception {
		Class<VenDto> klass = VenDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpect(authSession, VEN_ENDPOINT + venId, status, klass);
	}

	public void reregister(UserRequestPostProcessor authSession, String venId, int status) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post(VEN_ENDPOINT + venId + "/registerparty/requestReregistration");
		builder.with(authSession);
		oadrMockHttpMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
	}

	public void cancelRegistration(UserRequestPostProcessor authSession, String venId, int status) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post(VEN_ENDPOINT + venId + "/registerparty/cancelPartyRegistration");
		builder.with(authSession);
		oadrMockHttpMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
	}

	public void requestRegisterReport(UserRequestPostProcessor authSession, String venId, int status) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post(VEN_ENDPOINT + venId + "/report_action/requestRegister");
		builder.with(authSession);
		oadrMockHttpMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
	}

	public void sendRegisterReport(UserRequestPostProcessor authSession, String venId, int status) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post(VEN_ENDPOINT + venId + "/report_action/sendRegister");
		builder.with(authSession);
		oadrMockHttpMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
	}

	public void sendCancelReport(UserRequestPostProcessor authSession, String venId,
			MultiValueMap<String, String> params, int status) throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post(VEN_ENDPOINT + venId + "/report_action/cancel").params(params);
		builder.with(authSession);
		oadrMockHttpMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
	}

	public List<ReportCapabilityDto> getVenReportAvailable(UserRequestPostProcessor authSession, String venId,
			MultiValueMap<String, String> params, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/available", status, ReportCapabilityDto.class, params);
	}

	public List<ReportCapabilityDescriptionDto> getVenReportAvailableDescription(UserRequestPostProcessor authSession,
			String venId, MultiValueMap<String, String> params, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/available/description", status, ReportCapabilityDescriptionDto.class,
				params);
	}

	public List<OtherReportCapabilityDto> searchVenReportAvailable(UserRequestPostProcessor authSession, String venId,
			int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/available/search", status, OtherReportCapabilityDto.class);
	}

	public List<OtherReportRequestDto> getVenReportRequested(UserRequestPostProcessor authSession, String venId,
			LinkedMultiValueMap<String, String> params, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/requested", status, OtherReportRequestDto.class, params);
	}

	public List<OtherReportRequestDto> searchVenReportRequested(UserRequestPostProcessor authSession, String venId,
			LinkedMultiValueMap<String, String> params, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/requested/search", status, OtherReportRequestDto.class, params);
	}

	public List<OtherReportRequestSpecifierDto> searchVenReportRequestedSpecifier(UserRequestPostProcessor authSession,
			String venId, OtherReportRequestSpecifierSearchCriteria criteria, int status) throws Exception {
		MvcResult andReturn = oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(VEN_ENDPOINT + venId + "/report/requested/specifier")
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE)
						.content(mapper.writeValueAsString(criteria)).with(authSession))
				.andExpect(status().is(status)).andReturn();
		return Oadr20bTestUtils.convertMvcResultToDtoList(andReturn, OtherReportRequestSpecifierDto.class);
	}

	public List<OtherReportDataFloatDto> getVenReportRequestedFloatData(UserRequestPostProcessor authSession,
			String venId, String reportSpecifierId, int status) throws Exception {
		Class<OtherReportDataFloatDto> klass = OtherReportDataFloatDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/float/" + reportSpecifierId, status, klass);
	}

	public List<OtherReportDataFloatDto> getVenReportRequestedSpecifierFloatData(UserRequestPostProcessor authSession,
			String venId, String reportSpecifierId, String rid, int status) throws Exception {
		Class<OtherReportDataFloatDto> klass = OtherReportDataFloatDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/float/" + reportSpecifierId + "/rid/" + rid, status, klass);
	}

	public List<OtherReportDataPayloadResourceStatusDto> getVenReportRequestedResourceStatusData(
			UserRequestPostProcessor authSession, String venId, String reportSpecifierId, int status) throws Exception {
		Class<OtherReportDataPayloadResourceStatusDto> klass = OtherReportDataPayloadResourceStatusDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/resourcestatus/" + reportSpecifierId, status, klass);
	}

	public List<OtherReportDataPayloadResourceStatusDto> getVenReportRequestedSpecifierResourceStatusData(
			UserRequestPostProcessor authSession, String venId, String reportSpecifierId, String rid, int status)
			throws Exception {
		Class<OtherReportDataPayloadResourceStatusDto> klass = OtherReportDataPayloadResourceStatusDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/resourcestatus/" + reportSpecifierId + "/rid/" + rid, status,
				klass);
	}

	public List<OtherReportDataKeyTokenDto> getVenReportRequestedKeyTokenData(UserRequestPostProcessor authSession,
			String venId, String reportSpecifierId, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/keytoken/" + reportSpecifierId, status,
				OtherReportDataKeyTokenDto.class);
	}

	public List<OtherReportDataKeyTokenDto> getVenReportRequestedSpecifierKeyTokenData(
			UserRequestPostProcessor authSession, String venId, String reportSpecifierId, String rid, int status)
			throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/report/data/keytoken/" + reportSpecifierId + "/rid/" + rid, status,
				OtherReportDataKeyTokenDto.class);
	}

	public void subscribe(UserRequestPostProcessor authSession, String venId,
			List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions, int status)
			throws JsonProcessingException, Exception {
		oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(VEN_ENDPOINT + venId + "/report/available/description/subscribe")
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE)
						.content(mapper.writeValueAsString(subscriptions)).with(authSession))
				.andExpect(status().is(status));
	}

	public void request(UserRequestPostProcessor authSession, String venId,
			List<OtherReportRequestDtoCreateRequestDto> requests, int status)
			throws JsonProcessingException, Exception {
		oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(VEN_ENDPOINT + venId + "/report/available/description/request")
						.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE)
						.content(mapper.writeValueAsString(requests)).with(authSession))
				.andExpect(status().is(status));
	}

	public void cancelSubscription(UserRequestPostProcessor authSession, String venId, String reportRequestId,
			int status) throws Exception {
		LinkedMultiValueMap<String, String> params = OadrParamBuilder.builder().addReportRequestId(reportRequestId)
				.build();
		oadrMockHttpMvc.perform(MockMvcRequestBuilders
				.post(VEN_ENDPOINT + venId + "/report/requested/cancelSubscription")
				.header(CONTENT_TYPE_HEADER_NAME, APPLICATION_JSON_HEADER_VALUE).params(params).with(authSession))
				.andExpect(status().is(status));
	}

	public List<VenOptDto> getVenOpt(UserRequestPostProcessor authSession, String venId,
			LinkedMultiValueMap<String, String> params, int status) throws Exception {
		Class<VenOptDto> klass = VenOptDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession, VEN_ENDPOINT + venId + "/opt", status,
				klass, params);
	}

	public List<VenOptDto> getVenResourceOpt(UserRequestPostProcessor authSession, String venId, String resourceId,
			LinkedMultiValueMap<String, String> params, int status) throws Exception {
		Class<VenOptDto> klass = VenOptDto.class;
		if (HttpStatus.OK_200 != status) {
			klass = null;
		}
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				VEN_ENDPOINT + venId + "/opt/resource/" + resourceId, status, klass, params);
	}

}
