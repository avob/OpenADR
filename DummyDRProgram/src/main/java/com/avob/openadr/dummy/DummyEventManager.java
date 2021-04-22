package com.avob.openadr.dummy;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.core.appender.rolling.action.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.server.oadrvtn20b.api.DemandResponseControllerApi;
import com.avob.server.oadrvtn20b.api.MarketContextControllerApi;
import com.avob.server.oadrvtn20b.handler.ApiException;
import com.avob.server.oadrvtn20b.handler.ApiResponse;
import com.avob.server.oadrvtn20b.model.DemandResponseEventActivePeriodDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventBaselineDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventCreateDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventDescriptorDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventDescriptorDto.OadrProfileEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventDescriptorDto.ResponseRequiredEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventFilter;
import com.avob.server.oadrvtn20b.model.DemandResponseEventFilter.TypeEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventReadDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventSearchDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventSignalDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventSignalIntervalDto;
import com.avob.server.oadrvtn20b.model.VenMarketContextDto;

@Service
public class DummyEventManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventManager.class);

	private static final String X_TOTAL_COUNT = "X-total-count";

	@Resource
	private MarketContextControllerApi marketContextControllerApi;

	@Resource
	private DemandResponseControllerApi demandResponseControllerApi;

	@Resource
	private DummyVTN20bControllerConfig dummyVTN20bControllerConfig;

	@Resource
	private List<VenMarketContextDto> marketContexts;

	private List<DemandResponseEventCreateDto> eventTemplate = new ArrayList<>();;

	@PostConstruct
	public void init() {
		marketContexts.forEach(marketContext -> {
			Long marketcontextId;
			try {
				VenMarketContextDto findMarketContextByNameUsingGET = marketContextControllerApi
						.findMarketContextByNameUsingGET(marketContext.getName());
				marketcontextId = findMarketContextByNameUsingGET.getId();
				LOGGER.warn("Ven market context: " + marketContext.getName() + " is already provisioned");
			} catch (ApiException e) {
				if (e.getCode() != HttpStatus.SC_NOT_FOUND) {
					LOGGER.error("Ven market context: " + marketContext.getName() + " can't be provisioned", e);
					return;
				} else {

					try {
						VenMarketContextDto createMarketContextUsingPOST = marketContextControllerApi
								.createMarketContextUsingPOST(marketContext);
						marketcontextId = createMarketContextUsingPOST.getId();
					} catch (ApiException e1) {
						LOGGER.error("Ven market context: " + marketContext.getName() + " can't be provisioned", e1);
						return;
					}
				}
			}

			if (marketcontextId == null) {
				LOGGER.error("Ven market context: " + marketContext.getName() + " can't be provisioned");
				return;
			}

			List<DemandResponseEventReadDto> events = marketContexts.stream().map(m -> {

				DemandResponseEventReadDto eventRead = new DemandResponseEventReadDto();
				DemandResponseEventActivePeriodDto activePeriod = new DemandResponseEventActivePeriodDto();
				activePeriod.setDuration(m.getActivePeriod().getDuration());
				activePeriod.setNotificationDuration(m.getActivePeriod().getNotificationDuration());
				activePeriod.setRampUpDuration(m.getActivePeriod().getRampUpDuration());
				activePeriod.setRecoveryDuration(m.getActivePeriod().getRecoveryDuration());
				activePeriod.setToleranceDuration(m.getActivePeriod().getToleranceDuration());
				eventRead.setActivePeriod(activePeriod);

				if (m.getBaseline() != null) {
					DemandResponseEventBaselineDto baseline = new DemandResponseEventBaselineDto();
					baseline.setBaselineId(m.getBaseline().getBaselineId());
					baseline.setBaselineName(m.getBaseline().getBaselineName());
					baseline.setDuration(m.getBaseline().getDuration());
					baseline.setIntervals(m.getBaseline().getIntervals().stream().map(i -> {
						DemandResponseEventSignalIntervalDto eventInterval = new DemandResponseEventSignalIntervalDto();
						eventInterval.setDuration(i.getDuration());
						eventInterval.setValue(i.getValue());
						return eventInterval;
					}).collect(Collectors.toList()));
					eventRead.setBaseline(baseline);
				}

				DemandResponseEventDescriptorDto descriptor = new DemandResponseEventDescriptorDto();
				descriptor.setMarketContext(m.getName());
				descriptor.setOadrProfile(OadrProfileEnum.fromValue(m.getDescriptor().getOadrProfile().getValue()));
				descriptor.setPriority(m.getDescriptor().getPriority());
				descriptor.setResponseRequired(ResponseRequiredEnum.fromValue(m.getDescriptor().getResponseRequired().getValue()));
				descriptor.setTestEvent(false);
				descriptor.setVtnComment(m.getDescriptor().getVtnComment());
				eventRead.setDescriptor(descriptor);

				eventRead.setPublished(true);

				eventRead.setSignals(m.getSignals().stream().map(s -> {
					DemandResponseEventSignalDto eventSignal = new DemandResponseEventSignalDto();
					eventSignal.setItemBase(s.getItemBase());
					eventSignal.setIntervals(s.getIntervals().stream().map(i -> {
						DemandResponseEventSignalIntervalDto eventInterval = new DemandResponseEventSignalIntervalDto();
						eventInterval.setDuration(i.getDuration());
						eventInterval.setValue(i.getValue());
						return eventInterval;
					}).collect(Collectors.toList()));
					eventSignal.setSignalName(s.getSignalName());
					eventSignal.setSignalType(
							DemandResponseEventSignalDto.SignalTypeEnum.fromValue(s.getSignalType().getValue()));
					eventSignal.setTargets(s.getTargets());

					return eventSignal;
				}).collect(Collectors.toList()));

				eventRead.setTargets(m.getTargets());

				return eventRead;
			}).collect(Collectors.toList());

			DemandResponseEventFilter filter = new DemandResponseEventFilter();
			filter.setType(TypeEnum.MARKET_CONTEXT);
			filter.setValue(marketContext.getName());

			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime truncatedTo = now.truncatedTo(ChronoUnit.HOURS);
			Long start = truncatedTo.toEpochSecond() * 1000;
			Long end = truncatedTo.plusHours(6).toEpochSecond() * 1000;
			Integer totalCount;
			int page = 0;
			try {

				do {

					DemandResponseEventSearchDto demandResponseEventSearch = new DemandResponseEventSearchDto();
					demandResponseEventSearch.filters(Arrays.asList(filter));
					ApiResponse<List<DemandResponseEventReadDto>> response = demandResponseControllerApi
							.searchUsingPOSTWithHttpInfo(demandResponseEventSearch, end, page, null, start);
					totalCount = Integer.valueOf(response.getHeaders().get(X_TOTAL_COUNT).get(0));
					page++;
				} while (events.size() < totalCount);

				events.forEach(event -> {
					LOGGER.info(String.format("%s", String.valueOf(event.getId())));
				});

			} catch (ApiException e) {
				LOGGER.error("Market context: " + marketContext.getName() + " events can't be retrieved", e);
				return;
			}

			this.ensureEventAreCreatedForNextHour(truncatedTo, events, 24);
		});

	}

	private void ensureEventAreCreatedForNextHour(OffsetDateTime start, List<DemandResponseEventReadDto> existingEvents,
			int nextXHours) {

		List<Long> existingStart = new ArrayList<>();
		for (DemandResponseEventReadDto event : existingEvents) {
			existingStart.add(event.getActivePeriod().getStart());
		}

		Long end = start.toInstant().toEpochMilli() + nextXHours * 60 * 60 * 1000;

		eventTemplate.forEach(template -> {

			OffsetDateTime temp = start;

			String duration = template.getActivePeriod().getDuration();

			Duration parse = Duration.parse(duration);

			long durationMillis = parse.toMillis();

			LOGGER.info(String.format("%s %s %s", temp.toInstant().toEpochMilli(), durationMillis, end));

			while (temp.toInstant().toEpochMilli() + durationMillis < end) {

				template.getActivePeriod().setStart(temp.toInstant().toEpochMilli());
				if (template.getBaseline() != null) {
					template.getBaseline().setStart(temp.toInstant().toEpochMilli());
				}

				try {
					template.setPublished(true);
					demandResponseControllerApi.createUsingPOST(template);

//					demandResponseControllerApi.publishUsingPOST(createUsingPOST.getId());

				} catch (ApiException e) {
					LOGGER.error("Event can't be created", e);
				}

				temp = temp.plus(durationMillis, ChronoUnit.MILLIS);

			}

		});

	}

}
