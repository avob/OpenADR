package com.avob.openadr.server.oadr20b.vtn;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.google.common.collect.Sets;

@Profile("test-functional")
@Configuration
public class EmbeddedSqlDatabaseInit {

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private VenService venService;

	@Resource
	private DemandResponseEventService demandeResponseEventService;

	@PostConstruct
	public void init() {
		String marketContextName = "http://oadr.avob.com";
		String marketContextDescription = "Avob Test Market Context";
		String marketcontextColor = "#90CAF9";
		VenMarketContext marketContext = venMarketContextService
				.prepare(new VenMarketContextDto(marketContextName, marketContextDescription, marketcontextColor));
		venMarketContextService.save(marketContext);

		marketContextName = "http://MarketContext1";
		marketContextDescription = "Test Market Context 1";
		marketcontextColor = "#ff8080";
		marketContext = venMarketContextService
				.prepare(new VenMarketContextDto(marketContextName, marketContextDescription, marketcontextColor));
		venMarketContextService.save(marketContext);

		String groupName = "OadrCACert";
		VenGroup oadrCaCert = venGroupService.prepare(new VenGroupDto(groupName));
		venGroupService.save(oadrCaCert);

		groupName = "CustomCACert";
		VenGroup customCaCert = venGroupService.prepare(new VenGroupDto(groupName));
		venGroupService.save(customCaCert);

		// ven1.oadr.com
		VenCreateDto dto = new VenCreateDto();
		String ven1Username = "4A:D1:2E:95:49:43:80:0B:8D:E9";
		dto.setUsername(ven1Username);
		dto.setAuthenticationType("x509");
		dto.setCommonName("ven1.oadr.com");
		dto.setOadrProfil("20b");
		Ven prepare = venService.prepare(dto);
		prepare.setVenMarketContexts(Sets.newHashSet(marketContext));
		prepare.setVenGroup(Sets.newHashSet(customCaCert));
		venService.save(prepare);

		// ven2.oadr.com
		String ven2Username = "B1:71:26:57:3C:53:0C:D9:23:47";
		dto = new VenCreateDto();
		dto.setUsername(ven2Username);
		dto.setAuthenticationType("x509");
		dto.setCommonName("ven2.oadr.com");
		dto.setOadrProfil("20b");
		prepare = venService.prepare(dto);
		prepare.setVenMarketContexts(Sets.newHashSet(marketContext));
		prepare.setVenGroup(Sets.newHashSet(customCaCert));
		venService.save(prepare);

		String eventId = "eventId";
		String duration = "PT1H";
		String notificationDuration = "P1D";
		Long start = System.currentTimeMillis();
		DemandResponseEventStateEnum state = DemandResponseEventStateEnum.ACTIVE;

		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");

		DemandResponseEventCreateDto eventDto = new DemandResponseEventCreateDto();
		eventDto.getDescriptor().setEventId(eventId);
		eventDto.getDescriptor().setState(state);
		eventDto.getActivePeriod().setStart(start);
		eventDto.getActivePeriod().setNotificationDuration(notificationDuration);

		eventDto.getDescriptor().setMarketContext(marketContext.getName());
		eventDto.getDescriptor().setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		eventDto.getDescriptor().setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		eventDto.getActivePeriod().setDuration(duration);
		eventDto.getActivePeriod().setNotificationDuration(notificationDuration);
		eventDto.getTargets().add(new DemandResponseEventTargetDto("ven", ven1Username));
		eventDto.getTargets().add(new DemandResponseEventTargetDto("ven", ven2Username));
		eventDto.getSignals().add(signal);

		demandeResponseEventService.create(eventDto);

	}
}
