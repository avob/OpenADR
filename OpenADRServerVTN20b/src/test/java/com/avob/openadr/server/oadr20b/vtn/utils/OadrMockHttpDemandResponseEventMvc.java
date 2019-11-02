package com.avob.openadr.server.oadr20b.vtn.utils;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OadrMockHttpDemandResponseEventMvc {

	private static final String DEMANDRESPONSEEVENT_ENDPOINT = "/DemandResponseEvent/";

	private ObjectMapper mapper = new ObjectMapper();

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	public DemandResponseEventReadDto get(UserRequestPostProcessor authSession, Long eventId, int status)
			throws Exception {
		MvcResult andReturn = oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.get(DEMANDRESPONSEEVENT_ENDPOINT + eventId.toString()).with(authSession)
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
		if (HttpStatus.OK_200 != status) {
			return null;
		}
		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();
		return mapper.readValue(mockHttpServletResponse.getContentAsString(), DemandResponseEventReadDto.class);
	}

	public DemandResponseEventReadDto create(UserRequestPostProcessor authSession, DemandResponseEventCreateDto dto,
			int status) throws Exception {

		MvcResult andReturn = oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(DEMANDRESPONSEEVENT_ENDPOINT).with(authSession)
						.content(mapper.writeValueAsBytes(dto)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
		if (HttpStatus.CREATED_201 != status) {
			return null;
		}
		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		return mapper.readValue(mockHttpServletResponse.getContentAsString(), DemandResponseEventReadDto.class);

	}

	public void delete(UserRequestPostProcessor authSession, Long eventId, int status) throws Exception {
		oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.delete(DEMANDRESPONSEEVENT_ENDPOINT + eventId.toString())
						.with(authSession).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();

	}

	public DemandResponseEventReadDto cancel(UserRequestPostProcessor authSession, Long eventId, int status)
			throws Exception {
		MvcResult andReturn = oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(DEMANDRESPONSEEVENT_ENDPOINT + eventId.toString() + "/cancel")
						.with(authSession).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();

		return mapper.readValue(andReturn.getResponse().getContentAsString(), DemandResponseEventReadDto.class);
	}

	public List<VenDemandResponseEventDto> getDemandResponseEventVenResponse(UserRequestPostProcessor authSession,
			Long eventId, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				DEMANDRESPONSEEVENT_ENDPOINT + eventId + "/venResponse", status, VenDemandResponseEventDto.class);
	}

	public VenDemandResponseEventDto getDemandResponseEventVenResponse(UserRequestPostProcessor authSession,
			Long eventId, String venID, int status) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpect(authSession,
				DEMANDRESPONSEEVENT_ENDPOINT + eventId + "/venResponse/" + venID, status,
				VenDemandResponseEventDto.class);
	}

	public List<DemandResponseEventReadDto> search(UserRequestPostProcessor authSession,
			List<DemandResponseEventFilter> filters, int status) throws Exception {
		MvcResult andReturn = oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post(DEMANDRESPONSEEVENT_ENDPOINT + "/search").with(authSession)
						.content(mapper.writeValueAsString(filters)).header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();
		return Oadr20bTestUtils.convertMvcResultToDtoList(andReturn, DemandResponseEventReadDto.class);
	}

}
