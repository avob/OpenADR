package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiEventType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Oadr20bEventControllerTest {

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private OadrMockMvc oadrMockMvc;

	private JAXBElement<EiEventType> createPayload(String eventId, EiTargetType target, String marketContextName) {
		long timestampStart = System.currentTimeMillis();
		String eventXmlDuration = "PT1H";
		String toleranceXmlDuration = "PT5M";
		String notificationXmlDuration = "P1D";
		EiActivePeriodType period = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
				eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

		long start = System.currentTimeMillis();
		float currentValue = 3f;
		String xmlDuration = "PT1H";
		String signalId = "0";
		String signalName = "simple";
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		String intervalId = "0";
		EiEventSignalType eiEventSignal = Oadr20bEiEventBuilders
				.newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(Oadr20bEiBuilders.newOadr20bSignalIntervalTypeBuilder(intervalId, start, xmlDuration,
						Lists.newArrayList(currentValue)).build())
				.build();

		long datetime = System.currentTimeMillis();
		long modificationNumber = 0L;
		Long priority = 0L;
		EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
		String comment = "";
		EventDescriptorType descriptor = Oadr20bEiEventBuilders
				.newOadr20bEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextName, status)
				.withPriority(priority).withVtnComment(comment).build();

		OadrEvent event = Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder().withActivePeriod(period)
				.withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal).build();

		return Oadr20bFactory.createEiEvent(event.getEiEvent());
	}

	@Test
	public void test() throws Exception {

		List<DemandResponseEvent> find = demandResponseEventService.find(OadrDataBaseSetup.VEN,
				DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(0, find.size());

		// invalid: no payload
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, "mouaiccool",
				HttpStatus.NOT_ACCEPTABLE_406);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(0, find.size());

		// valid: ven
		EiTargetType target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(OadrDataBaseSetup.VEN).build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId1", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME), HttpStatus.CREATED_201);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(1, find.size());

		// valid: group
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId(OadrDataBaseSetup.GROUP).build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId2", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME), HttpStatus.CREATED_201);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(2, find.size());

		// invalid: unknown group name
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId("unknown").build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId3", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME),
				HttpStatus.NOT_ACCEPTABLE_406);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(2, find.size());

		// invalid: unknown venID
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId("unknown").build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId4", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME),
				HttpStatus.NOT_ACCEPTABLE_406);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(2, find.size());

		// invalid: MUST specify ven
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addResourceId(OadrDataBaseSetup.VEN_RESOURCE_1)
				.build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId5", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME),
				HttpStatus.NOT_ACCEPTABLE_406);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(2, find.size());

		// valid: ven + resource
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(OadrDataBaseSetup.VEN)
				.addResourceId(OadrDataBaseSetup.VEN_RESOURCE_1).build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId6", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME), HttpStatus.CREATED_201);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(3, find.size());

		// valid: group + resource
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId(OadrDataBaseSetup.GROUP)
				.addResourceId(OadrDataBaseSetup.VEN_RESOURCE_1).build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId7", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME), HttpStatus.CREATED_201);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(4, find.size());

		// invalid: ven do not belong to marketcontext
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId(OadrDataBaseSetup.GROUP)
				.addResourceId(OadrDataBaseSetup.VEN_RESOURCE_1).build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId8", target, OadrDataBaseSetup.ANOTHER_MARKET_CONTEXT_NAME),
				HttpStatus.NOT_ACCEPTABLE_406);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(4, find.size());

		// invalid: ven do not belong to marketcontext
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(OadrDataBaseSetup.VEN)
				.addResourceId(OadrDataBaseSetup.VEN_RESOURCE_1).build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId9", target, OadrDataBaseSetup.ANOTHER_MARKET_CONTEXT_NAME),
				HttpStatus.NOT_ACCEPTABLE_406);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(4, find.size());

		// invalid: unknown marketcontext
		target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(OadrDataBaseSetup.VEN)
				.addResourceId(OadrDataBaseSetup.VEN_RESOURCE_1).build();
		oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				createPayload("eventId10", target, "mouaiccool"), HttpStatus.NOT_ACCEPTABLE_406);

		find = demandResponseEventService.find(OadrDataBaseSetup.VEN, DemandResponseEventStateEnum.ACTIVE);
		assertNotNull(find);
		assertEquals(4, find.size());

		// create and send OadrRequestEvent to EiEvent API - limit 1
		OadrRequestEventType oadrRequestEvent = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(OadrDataBaseSetup.VEN, "0").withReplyLimit(1L).build();

		OadrDistributeEventType unmarshal = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				oadrRequestEvent, HttpStatus.OK_200, OadrDistributeEventType.class);
		assertEquals(1, unmarshal.getOadrEvent().size());

		// create and send OadrRequestEvent to EiEvent API - no limit
		oadrRequestEvent = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(OadrDataBaseSetup.VEN, "0").build();

		unmarshal = oadrMockMvc.postEiEventAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, oadrRequestEvent,
				HttpStatus.OK_200, OadrDistributeEventType.class);
		assertEquals(4, unmarshal.getOadrEvent().size());

		for (DemandResponseEvent event : demandResponseEventService.findAll()) {
			demandResponseEventService.delete(event.getId());
		}

	}

}
