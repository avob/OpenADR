package com.avob.openadr.server.common.vtn.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilter;
import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilterType;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
public class VenServiceTest {

	private Long start = System.currentTimeMillis();
	private List<Ven> vens = new ArrayList<Ven>();
	private List<DemandResponseEvent> events = new ArrayList<DemandResponseEvent>();
	private VenMarketContext marketContext = null;
	private DemandResponseEvent event1 = null;
	private DemandResponseEvent event2 = null;
	private DemandResponseEvent event3 = null;

	private String event1Id = "event1Id";
	private String event2Id = "event2Id";
	private String event3Id = "event3Id";

	private Ven ven1;
	private Ven ven2;
	private Ven ven3;

	@Resource
	private VenService venService;

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Test
	public void prepareTest() {
		String username = "ven1";
		Ven prepare = venService.prepare(username);
		assertNotNull(prepare);
		assertNull(prepare.getId());
		assertNull(prepare.getBasicPassword());
		assertNull(prepare.getDigestPassword());
		assertEquals(username, prepare.getUsername());

		venService.save(prepare);
		assertNotNull(prepare);
		assertNotNull(prepare.getId());
		assertNull(prepare.getBasicPassword());
		assertNull(prepare.getDigestPassword());
		assertEquals(username, prepare.getUsername());

		venService.delete(prepare);

		String password = "ven1";
		prepare = venService.prepare(username, password);
		assertNotNull(prepare);
		assertNull(prepare.getId());
		assertNotNull(prepare.getBasicPassword());
		assertNotNull(prepare.getDigestPassword());
		assertEquals(username, prepare.getUsername());

		String profil = "20a";
		String transport = "http";
		VenCreateDto dto = new VenCreateDto();
		dto.setUsername(username);
		dto.setPassword(password);
		dto.setOadrProfil(profil);
		dto.setTransport(transport);

		prepare = venService.prepare(dto);
		assertNotNull(prepare);
		assertNull(prepare.getId());
		assertNotNull(prepare.getBasicPassword());
		assertNotNull(prepare.getDigestPassword());
		assertEquals(username, prepare.getUsername());
		assertEquals(profil, prepare.getOadrProfil());
		assertEquals(transport, prepare.getTransport());

	}

	@Test
	public void venMetadataTest() {

		String groupName = "group1";
		VenGroup group = venGroupService.prepare(new VenGroupDto(groupName));
		venGroupService.save(group);

		String marketContextName = "http://oadr.avob.com";
		VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
		venMarketContextService.save(marketContext);

		String venID = "ven1";
		Ven ven = venService.prepare(new VenCreateDto(venID));
		String registrationId = "registrationId";
		ven.setRegistrationId(registrationId);
		ven.setVenGroup(Sets.newHashSet(group));
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven = venService.save(ven);

		Ven ven2 = venService.save(venService.prepare("ven2"));
		Ven ven3 = venService.save(venService.prepare("ven3"));

		VenResource res1 = venResourceService.prepare(ven, new VenResourceDto(venID + "_res0"));
		venResourceService.save(res1);
		VenResource res2 = venResourceService.prepare(ven, new VenResourceDto(venID + "_res1"));
		venResourceService.save(res2);

		List<VenResource> resources = venResourceService.findByVen(ven);
		assertEquals(2, resources.size());

		List<Ven> vens = venService.findByGroupName(Lists.newArrayList(group.getName()));
		assertEquals(1, vens.size());

		vens = venService.findByMarketContextName(Lists.newArrayList(marketContext.getName()));
		assertEquals(1, vens.size());

		vens = venService.findByUsernameInAndVenMarketContextsContains(Lists.newArrayList("ven1", "ven2", "ven4"),
				marketContext);
		assertEquals(1, vens.size());

		Ven findOne = venService.findOneByRegistrationId(registrationId);
		assertNotNull(findOne);
		assertEquals(venID, findOne.getUsername());

		findOne = venService.findOneByUsername(venID);
		assertNotNull(findOne);
		assertEquals(venID, findOne.getUsername());

		assertEquals(3L, venService.count());

		venService.delete(ven);
		venService.delete(ven2);
		venService.delete(ven3);
		venGroupService.delete(group);
		venMarketContextService.delete(marketContext);

		assertEquals(0L, venService.count());
		assertEquals(0L, venResourceService.count());
		assertEquals(0L, venGroupService.count());
		assertEquals(0L, venMarketContextService.count());

	}

	private VenFilter getFilter(VenFilterType type, String value) {
		VenFilter filter = new VenFilter();
		filter.setType(type);
		filter.setValue(value);
		return filter;
	}

	@Test
	public void searchTest() {
		String marketContextName = "http://oadr.avob.com";
		marketContext = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
		venMarketContextService.save(marketContext);

		String groupName = "group1";
		VenGroup group = venGroupService.prepare(new VenGroupDto(groupName));
		venGroupService.save(group);

		ven1 = venService.prepare("ven1");
		ven1.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven1.setVenGroup(Sets.newHashSet(group));
		ven1 = venService.save(ven1);
		vens.add(ven1);

		ven2 = venService.prepare("ven2");
		ven2.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven2 = venService.save(ven2);
		vens.add(ven2);

		ven3 = venService.prepare("ven3");
		ven3.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven3.setVenGroup(Sets.newHashSet(group));
		ven3 = venService.save(ven3);
		vens.add(ven3);

		String duration = "PT1H";
		String notificationDuration = "P1D";

		DemandResponseEventStateEnum state = DemandResponseEventStateEnum.ACTIVE;

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId(event1Id);
		dto.getDescriptor().setState(state);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getActivePeriod().setStart(start);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setDuration(duration);
		dto.getActivePeriod().setNotificationDuration(notificationDuration);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", "ven1"));
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", "ven2"));
		dto.getSignals().add(signal);
		dto.setPublished(true);
		event1 = demandResponseEventService.create(dto);
		events.add(event1);

		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId(event2Id);
		dto.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getActivePeriod().setStart(start);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setDuration(duration);
		dto.getActivePeriod().setNotificationDuration(notificationDuration);
		dto.getActivePeriod().setRampUpDuration("PT1H");
		dto.getActivePeriod().setRecoveryDuration("PT1H");
		dto.getDescriptor().setVtnComment("comment");
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", "ven2"));
		dto.getSignals().add(signal);
		dto.setPublished(true);
		event2 = demandResponseEventService.create(dto);
		events.add(event2);

		dto = new DemandResponseEventCreateDto();
		dto.getDescriptor().setEventId(event3Id);
		dto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		dto.getDescriptor().setState(state);
		dto.getActivePeriod().setStart(start);
		dto.getDescriptor().setMarketContext(marketContext.getName());
		dto.getActivePeriod().setDuration(duration);
		dto.getActivePeriod().setNotificationDuration(notificationDuration);
		dto.getTargets().add(new DemandResponseEventTargetDto("ven", "ven3"));
		dto.getSignals().add(signal);
		dto.setPublished(true);
		event3 = demandResponseEventService.create(dto);
		events.add(event3);

		List<VenFilter> filters = new ArrayList<>();
		Page<Ven> search = venService.search(filters);
		assertEquals(3, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		search = venService.search(filters);
		assertEquals(2, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.EVENT, event2Id));
		search = venService.search(filters);
		assertEquals(1, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.EVENT, event3Id));
		search = venService.search(filters);
		assertEquals(1, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		filters.add(getFilter(VenFilterType.EVENT, event2Id));
		search = venService.search(filters);
		assertEquals(2, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		filters.add(getFilter(VenFilterType.EVENT, event2Id));
		filters.add(getFilter(VenFilterType.EVENT, event3Id));
		search = venService.search(filters);
		assertEquals(3, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.IS_REGISTERED, Boolean.TRUE.toString()));
		search = venService.search(filters);
		assertEquals(0, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.IS_REGISTERED, Boolean.FALSE.toString()));
		search = venService.search(filters);
		assertEquals(3, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.GROUP, groupName));
		search = venService.search(filters);
		assertEquals(2, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.GROUP, groupName));
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		search = venService.search(filters);
		assertEquals(1, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.MARKET_CONTEXT, marketContextName));
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		search = venService.search(filters);
		assertEquals(2, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.MARKET_CONTEXT, marketContextName));
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		filters.add(getFilter(VenFilterType.VEN, "ven1"));
		search = venService.search(filters);
		assertEquals(1, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.MARKET_CONTEXT, marketContextName));
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		filters.add(getFilter(VenFilterType.VEN, "ven"));
		search = venService.search(filters);
		assertEquals(2, search.getTotalElements());

		filters = new ArrayList<>();
		filters.add(getFilter(VenFilterType.MARKET_CONTEXT, marketContextName));
		filters.add(getFilter(VenFilterType.EVENT, event1Id));
		filters.add(getFilter(VenFilterType.VEN, "mouaiccool"));
		search = venService.search(filters);
		assertEquals(0, search.getTotalElements());

		demandResponseEventService.delete(events);
		venService.delete(vens);
		venMarketContextService.delete(marketContext);
		venGroupService.delete(group);
	}
}
