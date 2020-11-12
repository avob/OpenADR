package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.Filter;

import org.eclipse.jetty.http.HttpStatus;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avob.openadr.client.http.oadr20b.vtn.OadrHttpVtnClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushService;

@Service
public class OadrMockEiHttpMvc {

	private MockMvc mockMvc;

	private Oadr20bJAXBContext jaxbContext;

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private WebApplicationContext wac;

	@Resource
	private Oadr20bPushService oadr20bPushService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	private List<InvocationOnMock> response = new ArrayList<>();

	@PostConstruct
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();

		OadrHttpVtnClient20b unsecure = Mockito.mock(OadrHttpVtnClient20b.class);
		oadr20bPushService.setOadrHttpVtnClient20b(unsecure);
		oadr20bPushService.setSecuredOadrHttpVtnClient20b(unsecure);
		Mockito.doAnswer((Answer<?>) new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				response.add(0, invocation);
				OadrCancelPartyRegistrationType argument = (OadrCancelPartyRegistrationType) invocation.getArgument(1);
				OadrCanceledPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
						.newOadr20bCanceledPartyRegistrationBuilder(
								Oadr20bResponseBuilders.newOadr20bEiResponseOK(argument.getRequestID()),
								argument.getRegistrationID(), argument.getVenID())
						.build();
				return build;
			}
		}).when(unsecure).oadrCancelPartyRegistrationType(Mockito.any(String.class),
				Mockito.any(OadrCancelPartyRegistrationType.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			response.add(0, invocation);
			OadrCancelReportType argument = (OadrCancelReportType) invocation.getArgument(1);
			return Oadr20bEiReportBuilders
					.newOadr20bCanceledReportBuilder(argument.getRequestID(), HttpStatus.OK_200, argument.getVenID())
					.build();
		}).when(unsecure).oadrCancelReport(Mockito.any(String.class), Mockito.any(OadrCancelReportType.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			response.add(0, invocation);
			OadrCreateReportType argument = (OadrCreateReportType) invocation.getArgument(1);
			return Oadr20bEiReportBuilders
					.newOadr20bCreatedReportBuilder(argument.getRequestID(), HttpStatus.OK_200, argument.getVenID())
					.build();
		}).when(unsecure).oadrCreateReport(Mockito.any(String.class), Mockito.any(OadrCreateReportType.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			response.add(0, invocation);
			OadrDistributeEventType argument = (OadrDistributeEventType) invocation.getArgument(1);
			return Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(
							Oadr20bResponseBuilders
									.newOadr20bEiResponseBuilder(argument.getRequestID(), HttpStatus.OK_200).build(),
							argument.getVtnID())
					.build();
		}).when(unsecure).oadrDistributeEvent(Mockito.any(String.class), Mockito.any(OadrDistributeEventType.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			response.add(0, invocation);
			OadrRegisterReportType argument = (OadrRegisterReportType) invocation.getArgument(1);
			return Oadr20bEiReportBuilders
					.newOadr20bRegisteredReportBuilder(argument.getRequestID(), HttpStatus.OK_200, argument.getVenID())
					.build();
		}).when(unsecure).oadrRegisterReport(Mockito.any(String.class), Mockito.any(OadrRegisterReportType.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			response.add(0, invocation);
			OadrRequestReregistrationType argument = (OadrRequestReregistrationType) invocation.getArgument(1);
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(
					Oadr20bResponseBuilders.newOadr20bEiResponseBuilder("", HttpStatus.OK_200).build(),
					argument.getVenID()).build();
		}).when(unsecure).oadrRequestReregistrationType(Mockito.any(String.class),
				Mockito.any(OadrRequestReregistrationType.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			response.add(0, invocation);
			OadrUpdateReportType argument = (OadrUpdateReportType) invocation.getArgument(1);
			return Oadr20bEiReportBuilders
					.newOadr20bUpdatedReportBuilder(argument.getRequestID(), HttpStatus.OK_200, argument.getVenID())
					.build();
		}).when(unsecure).oadrUpdateReport(Mockito.any(String.class), Mockito.any(OadrUpdateReportType.class));

	}

	public Optional<InvocationOnMock> popResponse() {
		if (response.isEmpty()) {
			return Optional.empty();
		}
		Optional<InvocationOnMock> of = Optional.of(response.get(0));
		response.remove(0);
		return of;
	}

	public ResultActions perform(RequestBuilder requestBuilder) throws Exception {
		return mockMvc.perform(requestBuilder);
	}

	@SuppressWarnings("unchecked")
	public <T> T postEiAndExpect(String endpoint, UserRequestPostProcessor authSession, Object payload, int status,
			Class<T> klass) {
		String content = null;
		try {

			if (payload instanceof String) {
				content = (String) payload;
			} else {
				content = jaxbContext.marshalRoot(payload);
			}

		} catch (Oadr20bMarshalException e) {
			fail("Payload can't be marshalled");
		}
		MvcResult andReturn;
		try {

			andReturn = this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint).content(content).with(authSession))
					.andExpect(MockMvcResultMatchers.status().is(status)).andReturn();

			try {
				if (String.class.equals(klass)) {
					return (T) andReturn.getResponse().getContentAsString();
				}

				Object obj = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString());
				if (!klass.equals(obj.getClass())) {
					fail("Response payload(" + obj.getClass().getSimpleName() + ") can't be cast to expected class: "
							+ klass.getSimpleName());
				}
				return klass.cast(obj);
			} catch (Oadr20bUnmarshalException e) {
				e.printStackTrace();
				fail("Response payload can't be cast to expected class: " + klass.getSimpleName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Mock can't perform desired request");

		}
		return null;
	}

}
