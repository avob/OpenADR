package com.avob.openadr.dummy;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.server.oadrvtn20b.api.DemandResponseControllerApi;
import com.avob.server.oadrvtn20b.api.MarketContextControllerApi;
import com.avob.server.oadrvtn20b.handler.ApiException;
import com.avob.server.oadrvtn20b.handler.ApiResponse;
import com.avob.server.oadrvtn20b.model.DemandResponseEventActivePeriodDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventCreateDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventDescriptorDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventDescriptorDto.OadrProfileEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventDescriptorDto.ResponseRequiredEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventDescriptorDto.StateEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventFilter;
import com.avob.server.oadrvtn20b.model.DemandResponseEventFilter.TypeEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventReadDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventSignalDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventSignalDto.SignalNameEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventSignalDto.SignalTypeEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventSignalIntervalDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventTargetDto;
import com.avob.server.oadrvtn20b.model.VenMarketContextDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventTargetDto.TargetTypeEnum;

@Service
public class DummyEventManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventManager.class);

	private static final String X_TOTAL_COUNT = "X-total-count";

	@Resource
	private MarketContextControllerApi marketContextControllerApi;

	@Resource
	private DemandResponseControllerApi demandResponseControllerApi;

	@PostConstruct
	public void init() {
		Long marketcontextId;
		try {
			VenMarketContextDto findMarketContextByNameUsingGET = marketContextControllerApi.findMarketContextByNameUsingGET(DummyVTN20bControllerConfig.MARKET_CONTEXT);
			marketcontextId = findMarketContextByNameUsingGET.getId();
			LOGGER.warn("Ven market context: "+DummyVTN20bControllerConfig.MARKET_CONTEXT+ " is already provisioned");
		} catch (ApiException e) {
			if(e.getCode() != HttpStatus.NOT_FOUND_404) {
				LOGGER.error("Ven market context: "+DummyVTN20bControllerConfig.MARKET_CONTEXT+ " can't be provisioned", e);
				return;
			} else {
				VenMarketContextDto dto = new VenMarketContextDto();
				dto.setName(DummyVTN20bControllerConfig.MARKET_CONTEXT);
				dto.setDescription(DummyVTN20bControllerConfig.MARKET_CONTEXT_DESCRIPTION);
				try {
					VenMarketContextDto createMarketContextUsingPOST = marketContextControllerApi.createMarketContextUsingPOST(dto );
					marketcontextId = createMarketContextUsingPOST.getId();
				} catch (ApiException e1) {
					LOGGER.error("Ven market context: "+DummyVTN20bControllerConfig.MARKET_CONTEXT+ " can't be provisioned", e1);
					return;
				}
			}
		}

		if(marketcontextId == null) {
			LOGGER.error("Ven market context: "+DummyVTN20bControllerConfig.MARKET_CONTEXT+ " can't be provisioned");
			return;
		}
		
		List<DemandResponseEventReadDto> events = new ArrayList<>();
		DemandResponseEventFilter filter = new DemandResponseEventFilter();
		filter.setType(TypeEnum.MARKET_CONTEXT);
		filter.setValue(DummyVTN20bControllerConfig.MARKET_CONTEXT);

		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime truncatedTo = now.truncatedTo(ChronoUnit.HOURS);
		Long start = truncatedTo.toEpochSecond() * 1000;
		Long end = truncatedTo.plusHours(6).toEpochSecond() * 1000;
		Integer totalCount;
		int page= 0;
		try {

			do {

				ApiResponse<List<DemandResponseEventReadDto>> response = demandResponseControllerApi
						.searchUsingPOSTWithHttpInfo(Arrays.asList(filter), end, page, null, start);
				totalCount = Integer.valueOf(response.getHeaders().get(X_TOTAL_COUNT).get(0));
				page++;
			} while(events.size() < totalCount);

			events.forEach(event -> {
				LOGGER.info(String.format("%s", String.valueOf(event.getId())));
			});


		} catch (ApiException e) {
			LOGGER.error("Market context: "+DummyVTN20bControllerConfig.MARKET_CONTEXT+ " events can't be retrieved", e);
			return;
		}



		this.ensureEventAreCreatedForNext6Hours(truncatedTo, events);


	}

	private void ensureEventAreCreatedForNext6Hours(OffsetDateTime start, List<DemandResponseEventReadDto> existingEvents) {


		OffsetDateTime temp = start;
		List<Long> existingStart = new ArrayList<>();
		for(DemandResponseEventReadDto event : existingEvents) {
			existingStart.add(event.getActivePeriod().getStart());
		}

		for(int i=0; i<6; i++) {
			if(!existingStart.contains(temp.toEpochSecond()*1000)) {
				DemandResponseEventCreateDto createEvent = this.createEvent(temp.toEpochSecond()*1000);
				try {
					DemandResponseEventReadDto createUsingPOST = demandResponseControllerApi.createUsingPOST(createEvent );
					
					demandResponseControllerApi.publishUsingPOST(createUsingPOST.getId());

				} catch (ApiException e) {
					LOGGER.error("Event can't be created", e);
				};
			}
			temp = temp.plusHours(1);
		}


	}

	private DemandResponseEventCreateDto createEvent(Long start) {
		DemandResponseEventCreateDto event = new DemandResponseEventCreateDto();

		String duration = "PT1H";
		String notificationDuration = "P1D";
		String toleranceDuration = "PT5M";
		DemandResponseEventActivePeriodDto activePeriod = new DemandResponseEventActivePeriodDto()
				.start(start)
				.notificationDuration(notificationDuration )
				.toleranceDuration(toleranceDuration )
				.duration(duration);
		
		event.activePeriod(activePeriod);

		DemandResponseEventDescriptorDto descriptor = new DemandResponseEventDescriptorDto()
				.state(StateEnum.ACTIVE)
				.marketContext(DummyVTN20bControllerConfig.MARKET_CONTEXT)
				.oadrProfile(OadrProfileEnum.OADR20B)
				.responseRequired(ResponseRequiredEnum.ALWAYS);
		event.descriptor(descriptor );

		DemandResponseEventSignalDto signalsItem = new DemandResponseEventSignalDto()
				.signalName(SignalNameEnum.SIMPLE)
				.signalType(SignalTypeEnum.LEVEL);

		String intervalDuration = "PT15M";
		for(int i=0; i < 4; i++) {
			Float value = Float.valueOf(i);
			DemandResponseEventSignalIntervalDto intervalsItem = new DemandResponseEventSignalIntervalDto()
					.duration(intervalDuration )
					.value(value );
			signalsItem.addIntervalsItem(intervalsItem );
		}
		event.addSignalsItem(signalsItem );


		DemandResponseEventTargetDto targetsItem = new DemandResponseEventTargetDto()
				.targetType(TargetTypeEnum.MARKET_CONTEXT)
				.targetId(DummyVTN20bControllerConfig.MARKET_CONTEXT);
		event.addTargetsItem(targetsItem );

		return event;
	}

}
