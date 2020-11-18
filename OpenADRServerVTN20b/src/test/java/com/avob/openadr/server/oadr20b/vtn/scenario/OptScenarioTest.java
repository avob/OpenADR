package com.avob.openadr.server.oadr20b.vtn.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.Oadr20bEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;
import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventReadDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDto;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOptDto;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiXmpp;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpDemandResponseEventMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVenMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockVen;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrParamBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OptScenarioTest {

	@Resource
	private OadrMockEiHttpMvc oadrMockEiHttpMvc;

	@Resource
	private OadrMockEiXmpp oadrMockEiXmpp;

	@Resource
	private OadrMockHttpVenMvc oadrMockHttpVenMvc;

	@Resource
	private OadrMockHttpDemandResponseEventMvc oadrMockHttpDemandResponseEventMvc;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Test
	public void test() throws Exception {
		for (Entry<String, UserRequestPostProcessor> entry : OadrDataBaseSetup.getTestVen().entrySet()) {
			VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, entry.getKey(),
					HttpStatus.OK_200);
			OadrMockVen mockVen = new OadrMockVen(ven, entry.getValue(), oadrMockEiHttpMvc, oadrMockEiXmpp,
					xmlSignatureService);
			_test(mockVen);
		}
	}

	public void _test(OadrMockVen mockVen) throws Exception {

		// test no opt configured
		LinkedMultiValueMap<String, String> params = OadrParamBuilder.builder().build();
		oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.FORBIDDEN_403);
		oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.USER_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.FORBIDDEN_403);

		// VEN CONTROLLER - get ven opt
		params = OadrParamBuilder.builder().build();
		List<VenOptDto> venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertTrue(venOpt.isEmpty());

		// test create valid vavailability opt on ven and marketContext using
		// xml signature
		String requestId = "requestId";
		Long createdDatetime = System.currentTimeMillis();
		VavailabilityType vavailabilityType = Oadr20bEiOptBuilders.newOadr20bVavailabilityBuilder()
				.addPeriod(createdDatetime, "PT1S").build();
		String optId = "optId";
		OptTypeType optType = OptTypeType.OPT_OUT;
		OptReasonEnumeratedType optReason = OptReasonEnumeratedType.NOT_PARTICIPATING;

		// EI OPT CONTROLLER - invalid target another VEN
		OadrCreateOptType oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId,
				mockVen.getVenId(), createdDatetime, vavailabilityType, optId, optType, optReason)
				.withMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		oadrCreateOptType.setEiTarget(new Oadr20bEiTargetTypeBuilder().addVenId("mouaiccool").build());
		OadrCreatedOptType opt = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				opt.getEiResponse().getResponseCode());
		assertEquals(requestId, opt.getEiResponse().getRequestID());
		assertEquals(optId, opt.getOptID());

		// EI OPT CONTROLLER - send OadrCreateOptType
		oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, mockVen.getVenId(),
				createdDatetime, vavailabilityType, optId, optType, optReason)
				.withMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		opt = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), opt.getEiResponse().getResponseCode());
		assertEquals(requestId, opt.getEiResponse().getRequestID());
		assertEquals(optId, opt.getOptID());

		// VEN CONTROLLER - test ven not found
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.build();
		oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, "mouaiccool", params,
				HttpStatus.NOT_ACCEPTABLE_406);

		// VEN CONTROLLER - test opt configured
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());
		assertEquals(mockVen.getVenId(), venOpt.get(0).getVenId());
		assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, venOpt.get(0).getMarketContext());

		// VEN CONTROLLER - test opt configured on
		// OadrDataBaseSetup.MARKET_CONTEXT_NAME: between
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());
		assertEquals(mockVen.getVenId(), venOpt.get(0).getVenId());
		assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, venOpt.get(0).getMarketContext());

		// VEN CONTROLLER - test opt configured on
		// OadrDataBaseSetup.MARKET_CONTEXT_NAME: after
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());
		assertEquals(mockVen.getVenId(), venOpt.get(0).getVenId());
		assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, venOpt.get(0).getMarketContext());

		// VEN CONTROLLER - test opt configured: after
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());
		assertEquals(mockVen.getVenId(), venOpt.get(0).getVenId());
		assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, venOpt.get(0).getMarketContext());

		// VEN CONTROLLER - test opt configured on
		// OadrDataBaseSetup.MARKET_CONTEXT_NAME: before
		params = OadrParamBuilder.builder().addEnd(createdDatetime + 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());
		assertEquals(mockVen.getVenId(), venOpt.get(0).getVenId());
		assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, venOpt.get(0).getMarketContext());

		// VEN CONTROLLER - test opt configured: before
		params = OadrParamBuilder.builder().addEnd(createdDatetime + 60 * 1000).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());
		assertEquals(mockVen.getVenId(), venOpt.get(0).getVenId());
		assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, venOpt.get(0).getMarketContext());

		// VEN CONTROLLER - test opt configured on OadrDataBaseSetup.MARKET_CONTEXT_NAME
		params = OadrParamBuilder.builder().addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());
		assertEquals(mockVen.getVenId(), venOpt.get(0).getVenId());
		assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, venOpt.get(0).getMarketContext());

		// VEN CONTROLLER - test opt configured on
		// OadrDataBaseSetup.MARKET_CONTEXT_NAME: outside
		// opt window frame
		params = OadrParamBuilder.builder().addEnd(createdDatetime - 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(0, venOpt.size());

		// VEN CONTROLLER - test no opt configured on
		// OadrDataBaseSetup.ANOTHER_MARKET_CONTEXT_NAME
		params = OadrParamBuilder.builder().addStart(createdDatetime - 1000).addEnd(createdDatetime + 1000)
				.addMarketContext(OadrDataBaseSetup.ANOTHER_MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(0, venOpt.size());

		// VEN CONTROLLER - test unknown marketContext
		params = OadrParamBuilder.builder().addStart(createdDatetime - 1000).addEnd(createdDatetime + 1000)
				.addMarketContext("mouaiccool").build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.NOT_ACCEPTABLE_406);

		// EI OPT CONTROLLER - test cannot set opt message about another VEN
		oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, "mouaiccool", createdDatetime,
				vavailabilityType, optId, optType, optReason).withMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME)
				.build();

		OadrCreatedOptType oadrCreatedOptType = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200,
				OadrCreatedOptType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCreatedOptType.getEiResponse().getResponseCode());
		assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
		assertEquals(optId, oadrCreatedOptType.getOptID());

		// EI OPT CONTROLLER - test create invalid vavailability opt on unknown ven
		// resource
		oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, mockVen.getVenId(),
				createdDatetime, vavailabilityType, optId, optType, optReason).addTargetedResource("fakeResource")
				.build();

		oadrCreatedOptType = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCreatedOptType.getEiResponse().getResponseCode());

		// EI OPT CONTROLLER - test create valid vavailability opt on ven resource
		// without
		// marketcontext
		oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, mockVen.getVenId(),
				createdDatetime, vavailabilityType, optId, optType, optReason)
				.addTargetedResource(OadrDataBaseSetup.VEN_RESOURCE_1).build();

		oadrCreatedOptType = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);

		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedOptType.getEiResponse().getResponseCode());
		assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
		assertEquals(optId, oadrCreatedOptType.getOptID());

		// test that NULL marketContext are grabbed when opt of a specific
		// marketContext are required (because NULL marketContext opt applied to
		// every marketContext)
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(2, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				"fakeResourceName", params, HttpStatus.NOT_ACCEPTABLE_406);

		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().addEnd(createdDatetime + 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().addEnd(createdDatetime + 60 * 1000).build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// VEN CONTROLLER - test previously created opt is linked to resource
		params = OadrParamBuilder.builder().build();
		venOpt = oadrMockHttpVenMvc.getVenResourceOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				OadrDataBaseSetup.VEN_RESOURCE_1, params, HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(1, venOpt.size());

		// create a dr event
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName(DemandResponseEventSignalNameEnum.SIMPLE);
		signal.setSignalType(DemandResponseEventSignalTypeEnum.LEVEL);
		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME);
		dto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.NEVER);
		dto.getActivePeriod().setDuration("PT1H");
		dto.getActivePeriod().setNotificationDuration("P1D");
		dto.getActivePeriod().setToleranceDuration("P1D");
		dto.getSignals().add(signal);
		// ensure event status is "active"
		dto.getActivePeriod().setStart(System.currentTimeMillis() - 10);
		dto.getTargets().add(new TargetDto(TargetTypeEnum.VEN, mockVen.getVenId()));
		dto.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
		dto.setPublished(true);
		DemandResponseEventReadDto create = oadrMockHttpDemandResponseEventMvc
				.create(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, dto, HttpStatus.CREATED_201);
		assertNotNull(create);

		// DEMANDE RESPONSE EVENT CONTROLLER - check created event
		List<DemandResponseEventFilter> filters = DemandResponseEventFilter.builder().addVenId(mockVen.getVenId())
				.addState(DemandResponseEventStateEnum.ACTIVE).build();
		List<DemandResponseEventReadDto> search = oadrMockHttpDemandResponseEventMvc
				.search(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, filters, HttpStatus.OK_200);
		assertNotNull(search);
		assertEquals(1, search.size());

		Long eventId = search.get(0).getId();
		Long modificationNumber = search.get(0).getDescriptor().getModificationNumber();

		// DEMANDE RESPONSE EVENT CONTROLLER - check created event
		DemandResponseEventReadDto demandResponseEventReadDto = oadrMockHttpDemandResponseEventMvc
				.get(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, HttpStatus.OK_200);
		assertNotNull(demandResponseEventReadDto);

		// DEMANDE RESPONSE EVENT CONTROLLER - check created event has no opt response
		VenDemandResponseEventDto demandResponseEventVenResponse = oadrMockHttpDemandResponseEventMvc
				.getDemandResponseEventVenResponse(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId,
						mockVen.getVenId(), HttpStatus.OK_200);
		assertNotNull(demandResponseEventVenResponse);
		assertNull(demandResponseEventVenResponse.getVenOpt());

		String eventOptId = "eventOptId";

		// EI OPT CONTROLLER - invalid not found related event
		oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, mockVen.getVenId(),
				createdDatetime, String.valueOf(eventId - 1), modificationNumber, eventOptId, optType, optReason)
				.build();
		oadrCreatedOptType = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
				oadrCreatedOptType.getEiResponse().getResponseCode());
		assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
		assertEquals(eventOptId, oadrCreatedOptType.getOptID());

		// EI OPT CONTROLLER - invalid modification number don't match
		oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, mockVen.getVenId(),
				createdDatetime, String.valueOf(eventId), modificationNumber + 1, eventOptId, optType, optReason)
				.build();
		oadrCreatedOptType = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_DATA_454),
				oadrCreatedOptType.getEiResponse().getResponseCode());
		assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
		assertEquals(eventOptId, oadrCreatedOptType.getOptID());

		// EI OPT CONTROLLER - test create opt related to specific event with different
		// optId
		oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, mockVen.getVenId(),
				createdDatetime, String.valueOf(eventId), modificationNumber, eventOptId, optType, optReason).build();
		oadrCreatedOptType = mockVen.opt(oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);

		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedOptType.getEiResponse().getResponseCode());
		assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
		assertEquals(eventOptId, oadrCreatedOptType.getOptID());

		// VEN CONTROLLER - test than last opt is not considered as vavailability opt
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.addMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(2, venOpt.size());

		// VEN CONTROLLER - test than last opt has been created: as it's a event related
		// opt on a
		// whole ven (without specifying resource), it does not create a VenOpt
		// but change VenDemandResponseEvent object related to ven and event
		// (override OadrCreatedEvent ven response)
		params = OadrParamBuilder.builder().build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(2, venOpt.size()); // no VenOpt created

		// DEMANDE RESPONSE EVENT CONTROLLER - check created event
		demandResponseEventVenResponse = oadrMockHttpDemandResponseEventMvc.getDemandResponseEventVenResponse(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, mockVen.getVenId(), HttpStatus.OK_200);
		assertNotNull(demandResponseEventVenResponse);
		assertNotNull(demandResponseEventVenResponse.getVenOpt());
		assertEquals(DemandResponseEventOptEnum.OPT_OUT, demandResponseEventVenResponse.getVenOpt());

		// EI OPT CONTROLLER - invalid mismatch payload venID and username auth session
		OadrCancelOptType oadrCancelOptType = Oadr20bEiOptBuilders
				.newOadr20bCancelOptBuilder(requestId, optId, "mouaiccool").build();
		OadrCanceledOptType oadrCanceledOptType = mockVen.opt(oadrCancelOptType, HttpStatus.OK_200,
				OadrCanceledOptType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCanceledOptType.getEiResponse().getResponseCode());

		// EI OPT CONTROLLER - send OadrCancelOptType
		oadrCancelOptType = Oadr20bEiOptBuilders.newOadr20bCancelOptBuilder(requestId, optId, mockVen.getVenId())
				.build();
		oadrCanceledOptType = mockVen.opt(oadrCancelOptType, HttpStatus.OK_200, OadrCanceledOptType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCanceledOptType.getEiResponse().getResponseCode());
		assertEquals(requestId, oadrCanceledOptType.getEiResponse().getRequestID());
		assertEquals(optId, oadrCanceledOptType.getOptID());

		// VEN CONTROLLER - test no opt configured
		params = OadrParamBuilder.builder().addStart(createdDatetime - 60 * 1000).addEnd(createdDatetime + 60 * 1000)
				.build();
		venOpt = oadrMockHttpVenMvc.getVenOpt(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
				HttpStatus.OK_200);
		assertNotNull(venOpt);
		assertEquals(0, venOpt.size());

		oadrMockHttpDemandResponseEventMvc.delete(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, eventId, HttpStatus.OK_200);

	}

}
