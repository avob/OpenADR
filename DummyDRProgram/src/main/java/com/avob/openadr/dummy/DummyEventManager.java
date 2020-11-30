package com.avob.openadr.dummy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.avob.server.oadrvtn20b.model.DemandResponseEventCreateDto;
import com.avob.server.oadrvtn20b.model.DemandResponseEventFilter;
import com.avob.server.oadrvtn20b.model.DemandResponseEventFilter.TypeEnum;
import com.avob.server.oadrvtn20b.model.DemandResponseEventReadDto;
import com.avob.server.oadrvtn20b.model.VenMarketContextDto;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

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

	private List<DemandResponseEventCreateDto> eventTemplate = new ArrayList<>();;

	@PostConstruct
	public void init() {
		Long marketcontextId;
		try {
			VenMarketContextDto findMarketContextByNameUsingGET = marketContextControllerApi
					.findMarketContextByNameUsingGET(DummyVTN20bControllerConfig.MARKET_CONTEXT);
			marketcontextId = findMarketContextByNameUsingGET.getId();
			LOGGER.warn(
					"Ven market context: " + DummyVTN20bControllerConfig.MARKET_CONTEXT + " is already provisioned");
		} catch (ApiException e) {
			if (e.getCode() != HttpStatus.SC_NOT_FOUND) {
				LOGGER.error(
						"Ven market context: " + DummyVTN20bControllerConfig.MARKET_CONTEXT + " can't be provisioned",
						e);
				return;
			} else {
				VenMarketContextDto dto = new VenMarketContextDto();
				dto.setName(DummyVTN20bControllerConfig.MARKET_CONTEXT);
				dto.setDescription(DummyVTN20bControllerConfig.MARKET_CONTEXT_DESCRIPTION);
				try {
					VenMarketContextDto createMarketContextUsingPOST = marketContextControllerApi
							.createMarketContextUsingPOST(dto);
					marketcontextId = createMarketContextUsingPOST.getId();
				} catch (ApiException e1) {
					LOGGER.error("Ven market context: " + DummyVTN20bControllerConfig.MARKET_CONTEXT
							+ " can't be provisioned", e1);
					return;
				}
			}
		}

		if (marketcontextId == null) {
			LOGGER.error("Ven market context: " + DummyVTN20bControllerConfig.MARKET_CONTEXT + " can't be provisioned");
			return;
		}

		Gson gson = new Gson();

		for (String filePath : dummyVTN20bControllerConfig.getEventTemplate()) {
			JsonReader reader;
			try {
				reader = new JsonReader(new FileReader(filePath));
				DemandResponseEventCreateDto fromJson = gson.fromJson(reader, DemandResponseEventCreateDto.class);
				eventTemplate.add(fromJson);
			} catch (FileNotFoundException e) {
				LOGGER.error("Event template: " + filePath + " cannot be parsed", e);
				return;
			}

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
		int page = 0;
		try {

			do {

				ApiResponse<List<DemandResponseEventReadDto>> response = demandResponseControllerApi
						.searchUsingPOSTWithHttpInfo(Arrays.asList(filter), end, page, null, start);
				totalCount = Integer.valueOf(response.getHeaders().get(X_TOTAL_COUNT).get(0));
				page++;
			} while (events.size() < totalCount);

			events.forEach(event -> {
				LOGGER.info(String.format("%s", String.valueOf(event.getId())));
			});

		} catch (ApiException e) {
			LOGGER.error("Market context: " + DummyVTN20bControllerConfig.MARKET_CONTEXT + " events can't be retrieved",
					e);
			return;
		}

		this.ensureEventAreCreatedForNextHour(truncatedTo, events, 24);

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
