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
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OadrMockHttpDemandResponseEventMvc {

	private static final String DEMANDRESPONSEEVENT_ENDPOINT = "/DemandResponseEvent/";

	private ObjectMapper mapper = new ObjectMapper();

	@Resource
	private OadrMockHttpMvc oadrMockHttpMvc;

	public List<VenDemandResponseEventDto> getDemandResponseEventVenResponse(UserRequestPostProcessor authSession,
			Long eventId) throws Exception {
		return oadrMockHttpMvc.getRestJsonControllerAndExpectList(authSession,
				DEMANDRESPONSEEVENT_ENDPOINT + eventId + "/venResponse", HttpStatus.OK_200,
				VenDemandResponseEventDto.class);
	}

	public DemandResponseEventReadDto create(UserRequestPostProcessor authSession, DemandResponseEventCreateDto dto)
			throws Exception {

		MvcResult andReturn = oadrMockHttpMvc
				.perform(MockMvcRequestBuilders.post("/DemandResponseEvent/")
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION).content(mapper.writeValueAsBytes(dto))
						.header("Content-Type", "application/json"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED_201)).andReturn();
		MockHttpServletResponse mockHttpServletResponse = andReturn.getResponse();

		return mapper.readValue(mockHttpServletResponse.getContentAsString(), DemandResponseEventReadDto.class);

	}

}
