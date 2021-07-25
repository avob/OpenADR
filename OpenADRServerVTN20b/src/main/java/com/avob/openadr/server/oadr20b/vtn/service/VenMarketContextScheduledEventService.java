package com.avob.openadr.server.oadr20b.vtn.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.server.common.vtn.models.ItemBaseDto;
import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventActivePeriodDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventBaselineDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventDescriptorDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalIntervalDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDemandResponseEventScheduleStrategy;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@Service
public class VenMarketContextScheduledEventService {

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private DtoMapper dtoMapper;

	public void scheduleEvent(VenMarketContext marketContext) {

		VenMarketContextDemandResponseEventScheduleStrategy scheduleStategy = marketContext
				.getDemandResponseEventScheduleStrategy();

		String scheduledCronDate = scheduleStategy.getScheduledCronDate();
		String scheduledCronTimezone = scheduleStategy.getScheduledCronTimezone();

		CronSequenceGenerator generator = new CronSequenceGenerator(scheduledCronDate,
				TimeZone.getTimeZone(scheduledCronTimezone));
		String notificationDuration = marketContext.getActivePeriod().getNotificationDuration();

		Long xmlDurationToMillisecond = Oadr20bFactory.xmlDurationToMillisecond(notificationDuration);

		Instant now = Instant.now();
		Instant temp = now;
		List<Instant> toCreateEventStart = new ArrayList<>();
		do {

			Date next = generator.next(Date.from(temp));

			temp = next.toInstant();

			toCreateEventStart.add(temp);

		} while (now.plus(xmlDurationToMillisecond, ChronoUnit.MILLIS).isAfter(temp));

		Set<Long> existingEventStart = new HashSet<>(demandResponseEventService
				.findAllActivePeriodStartByDescriptorMarketContextName(marketContext.getName()));

		Set<Long> generated = toCreateEventStart.stream().map(Instant::toEpochMilli).collect(Collectors.toSet());

		generated.removeAll(existingEventStart);

		List<Long> eventStarts = new ArrayList<>();
		eventStarts.addAll(generated);
		eventStarts.addAll(scheduleStategy.getScheduledDate().stream().map(d -> d.toInstant().toEpochMilli())
				.collect(Collectors.toList()));

		Collections.sort(eventStarts);

		for (Long start : eventStarts) {
			DemandResponseEventCreateDto createDto = createDto(marketContext, start);

			demandResponseEventService.create(createDto);
		}

	}

	private DemandResponseEventCreateDto createDto(VenMarketContext marketContext, Long start) {

		DemandResponseEventCreateDto eventRead = new DemandResponseEventCreateDto();
		DemandResponseEventActivePeriodDto activePeriod = new DemandResponseEventActivePeriodDto();
		activePeriod.setDuration(marketContext.getActivePeriod().getDuration());
		activePeriod.setNotificationDuration(marketContext.getActivePeriod().getNotificationDuration());
		activePeriod.setRampUpDuration(marketContext.getActivePeriod().getRampUpDuration());
		activePeriod.setRecoveryDuration(marketContext.getActivePeriod().getRecoveryDuration());
		activePeriod.setToleranceDuration(marketContext.getActivePeriod().getToleranceDuration());
		activePeriod.setStart(start);
		eventRead.setActivePeriod(activePeriod);

		if (marketContext.getBaseline() != null) {
			DemandResponseEventBaselineDto baseline = new DemandResponseEventBaselineDto();
			baseline.setBaselineId(marketContext.getBaseline().getBaselineId());
			baseline.setBaselineName(marketContext.getBaseline().getBaselineName());
			baseline.setDuration(marketContext.getBaseline().getDuration());
			baseline.setIntervals(marketContext.getBaseline().getIntervals().stream().map(i -> {
				DemandResponseEventSignalIntervalDto eventInterval = new DemandResponseEventSignalIntervalDto();
				eventInterval.setDuration(i.getDuration());
				eventInterval.setValue(i.getValue());
				return eventInterval;
			}).collect(Collectors.toList()));
			eventRead.setBaseline(baseline);
		}

		DemandResponseEventDescriptorDto descriptor = new DemandResponseEventDescriptorDto();
		descriptor.setMarketContext(marketContext.getName());
		descriptor.setOadrProfile(marketContext.getDescriptor().getOadrProfile());
		descriptor.setPriority(marketContext.getDescriptor().getPriority());
		descriptor.setResponseRequired(marketContext.getDescriptor().getResponseRequired());
		descriptor.setTestEvent(false);
		descriptor.setVtnComment(marketContext.getDescriptor().getVtnComment());
		eventRead.setDescriptor(descriptor);

		eventRead.setPublished(true);

		if (marketContext.getBaseline() != null) {
			DemandResponseEventBaselineDto baseline = dtoMapper.map(marketContext.getBaseline(),
					DemandResponseEventBaselineDto.class);

			baseline.setStart(start);
			eventRead.setBaseline(baseline);
		}

		eventRead.setSignals(marketContext.getSignals().stream().map(s -> {
			
			DemandResponseEventSignalDto eventSignal = new DemandResponseEventSignalDto();
			if(s.getItemBase() != null) {
				ItemBaseDto itemBase = dtoMapper.map(s.getItemBase(), ItemBaseDto.class);
				eventSignal.setItemBase(itemBase);
			}
			eventSignal.setIntervals(s.getIntervals().stream().map(i -> {
				DemandResponseEventSignalIntervalDto eventInterval = new DemandResponseEventSignalIntervalDto();
				eventInterval.setDuration(i.getDuration());
				eventInterval.setValue(i.getValue());
				return eventInterval;
			}).collect(Collectors.toList()));
			eventSignal.setSignalName(s.getSignalName());
			eventSignal.setSignalType(s.getSignalType());
			List<TargetDto> target = dtoMapper.mapList(s.getTargets(), TargetDto.class);
			eventSignal.setTargets(target);

			return eventSignal;
		}).collect(Collectors.toList()));
		List<TargetDto> target = dtoMapper.mapList(marketContext.getTargets(), TargetDto.class);
		eventRead.setTargets(target);

		return eventRead;
	}

}
