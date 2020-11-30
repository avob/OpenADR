package com.avob.openadr.server.oadr20b.ven.timeline;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiEventType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.strm.Intervals;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.model.oadr20b.xcal.Properties;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bDistributeEventApplicationLayerException;

public class Timeline {
	private static final Logger LOGGER = LoggerFactory.getLogger(Timeline.class);

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

	private TreeMap<Long, Map<String, List<TimelineEvent>>> timeline = new TreeMap<>();

	private Map<String, Map<String, OadrEvent>> events = new HashMap<>();
	private Map<String, List<ActiveSignal>> activeSignals = new HashMap<>();
	private Map<String, List<ActiveBaselineSignal>> activeBaselineSignals = new HashMap<>();
	private Map<String, VtnSessionConfiguration> vtnConfigurations = new HashMap<>();

	private ScheduledFuture<?> scheduledNextTick;

	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	private EventTimelineListener listener;

	public Timeline(EventTimelineListener listener) {
		this.listener = listener;
	}

	public synchronized void synchronizeOadrDistributeEvent(VtnSessionConfiguration vtnConfiguration,
			OadrDistributeEventType event) throws Oadr20bDistributeEventApplicationLayerException {
		this.cancelNextTick();
		events.put(vtnConfiguration.getSessionKey(), new HashMap<>());
		String requestID = event.getRequestID();
		List<String> knownEventIdList = new ArrayList<>(getEvents(vtnConfiguration).keySet());
		Iterator<OadrEvent> iterator = event.getOadrEvent().iterator();
		List<String> eventIdList = new ArrayList<>();
		while (iterator.hasNext()) {
			OadrEvent next = iterator.next();
			if (!isKnownEvent(vtnConfiguration, next)) {
				vtnConfigurations.put(vtnConfiguration.getVtnId(), vtnConfiguration);
				putEvent(vtnConfiguration, next);
				addEventTotimeline(vtnConfiguration, next);

				listener.onCreatedEvent(vtnConfiguration, next);

			} else {
				eventIdList.add(next.getEiEvent().getEventDescriptor().getEventID());
				if (isUpdatedEvent(vtnConfiguration, requestID, next)) {
					putEvent(vtnConfiguration, next);
					removeEventFromtimeline(vtnConfiguration, next);
					addEventTotimeline(vtnConfiguration, next);

					listener.onUpdatedEvent(vtnConfiguration, next);
				}
			}

		}

		knownEventIdList.removeAll(eventIdList);
		knownEventIdList.forEach(deletedOadrEventId -> {
			OadrEvent oadrEvent = getEvents(vtnConfiguration).get(deletedOadrEventId);
			removeEvent(vtnConfiguration, oadrEvent);
			removeEventFromtimeline(vtnConfiguration, oadrEvent);

			listener.onDeletedEvent(vtnConfiguration, oadrEvent);
		});

		this.synchronizeActiveSignals(vtnConfiguration);
		this.scheduleNextTick();
	}

	private synchronized void synchronizeActiveSignals(VtnSessionConfiguration vtnConfiguration) {
		Iterator<OadrEvent> iterator = this.getEvents(vtnConfiguration).values().iterator();
		List<ActiveSignal> activeSignalsList = new ArrayList<>();
		List<ActiveBaselineSignal> activeBaselineSignalsList = new ArrayList<>();
		while (iterator.hasNext()) {
			OadrEvent next = iterator.next();
			Long now = System.currentTimeMillis();
			Long eventStart = Oadr20bFactory.xmlCalendarToTimestamp(
					next.getEiEvent().getEiActivePeriod().getProperties().getDtstart().getDateTime());
			Long eventEnd = Oadr20bFactory.addXMLDurationToTimestamp(eventStart,
					next.getEiEvent().getEiActivePeriod().getProperties().getDuration().getDuration());
			if (now >= eventStart && now < eventEnd) {
				next.getEiEvent().getEiEventSignals().getEiEventSignal().forEach(signal -> {
					signal.getIntervals().getInterval().forEach(interval -> {
						Long intervalStart = Oadr20bFactory.xmlCalendarToTimestamp(interval.getDtstart().getDateTime());
						Long intervalEnd = Oadr20bFactory.addXMLDurationToTimestamp(intervalStart,
								interval.getDuration().getDuration());

						if (now >= intervalStart && now < intervalEnd) {
							ActiveSignal activeSignal = new ActiveSignal(next, signal, interval);
							activeSignalsList.add(activeSignal);
						}

					});
				});
				if (next.getEiEvent().getEiEventSignals().getEiEventBaseline() != null) {
					Long baselineStart = Oadr20bFactory.xmlCalendarToTimestamp(
							next.getEiEvent().getEiActivePeriod().getProperties().getDtstart().getDateTime());
					Long baselineEnd = Oadr20bFactory.addXMLDurationToTimestamp(eventStart,
							next.getEiEvent().getEiActivePeriod().getProperties().getDuration().getDuration());
					if (now >= baselineStart && now < baselineEnd) {
						next.getEiEvent().getEiEventSignals().getEiEventBaseline().getIntervals().getInterval()
								.forEach(interval -> {
									Long intervalStart = Oadr20bFactory
											.xmlCalendarToTimestamp(interval.getDtstart().getDateTime());
									Long intervalEnd = Oadr20bFactory.addXMLDurationToTimestamp(intervalStart,
											interval.getDuration().getDuration());

									if (now >= intervalStart && now < intervalEnd) {
										ActiveBaselineSignal activeSignal = new ActiveBaselineSignal(next, interval);
										activeBaselineSignalsList.add(activeSignal);
									}
								});
					}

				}
			}

		}

		activeSignals.put(vtnConfiguration.getSessionKey(), activeSignalsList);
		activeBaselineSignals.put(vtnConfiguration.getSessionKey(), activeBaselineSignalsList);

	}

	/**
	 * true if the event is already known, based on it's eventID, without
	 * considering it's modification number
	 * 
	 * @param event
	 * @return
	 */
	private boolean isKnownEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		return getEvents(vtnConfiguration).containsKey(event.getEiEvent().getEventDescriptor().getEventID());
	}

	/**
	 * true if the event have an higher modification number than an already known
	 * event with the same eventID
	 * 
	 * @param event
	 * @return
	 */
	private boolean isUpdatedEvent(VtnSessionConfiguration vtnConfiguration, String requestId, OadrEvent event)
			throws Oadr20bDistributeEventApplicationLayerException {
		String eventID = event.getEiEvent().getEventDescriptor().getEventID();
		long modificationNumber = event.getEiEvent().getEventDescriptor().getModificationNumber();
		OadrEvent oadrEvent = getEvents(vtnConfiguration).get(eventID);
		if (oadrEvent == null) {
			return false;
		}
		long knownModificationNumber = oadrEvent.getEiEvent().getEventDescriptor().getModificationNumber();
		if (knownModificationNumber > modificationNumber) {
			throw new Oadr20bDistributeEventApplicationLayerException(
					"registred event " + eventID + " has a modification number higher than retrieved event",
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestId, HttpStatus.NOT_ACCEPTABLE_406, "")
							.build());
		}

		return modificationNumber > knownModificationNumber;
	}

	/**
	 * @return the oadrEvents
	 */
	public Map<String, OadrEvent> getEvents(VtnSessionConfiguration vtnConfiguration) {
		Map<String, OadrEvent> map = events.get(vtnConfiguration.getVtnId());
		if (map == null) {
			map = new ConcurrentHashMap<>();
		}
		return map;
	}

	public List<ActiveSignal> getActiveSignals(VtnSessionConfiguration vtnConfiguration) {
		return activeSignals.get(vtnConfiguration.getSessionKey());
	}

	public List<ActiveBaselineSignal> getActiveBaselineSignals(VtnSessionConfiguration vtnConfiguration) {
		return activeBaselineSignals.get(vtnConfiguration.getSessionKey());
	}

	private void putEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		Map<String, OadrEvent> map = events.get(vtnConfiguration.getVtnId());
		if (map == null) {
			map = new HashMap<>();
		}
		map.put(event.getEiEvent().getEventDescriptor().getEventID(), event);
		events.put(vtnConfiguration.getVtnId(), map);
	}

	private void removeEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		Map<String, OadrEvent> map = events.get(vtnConfiguration.getVtnId());
		if (map != null) {
			map.remove(event.getEiEvent().getEventDescriptor().getEventID());
			events.put(vtnConfiguration.getVtnId(), map);
		}
	}

	private void addEventTotimeline(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {

		putActivePeriodOnTimeline(vtnConfiguration, event);

		if (event.getEiEvent().getEiEventSignals().getEiEventBaseline() != null) {
			event.getEiEvent().getEiEventSignals().getEiEventBaseline().getIntervals().getInterval()
					.forEach(interval -> {
						putBaselineIntervalOnTimeline(vtnConfiguration, event, interval);
					});
		}

		event.getEiEvent().getEiEventSignals().getEiEventSignal().forEach(signal -> {

			Intervals intervals = signal.getIntervals();
			intervals.getInterval().forEach(interval -> {

				putIntervalOnTimeline(vtnConfiguration, event, signal, interval);

			});
		});

	}

	private void removeEventFromtimeline(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		timeline.forEach((date, timelineEventMap) -> {
			timelineEventMap.remove(getInnerTimelineKey(vtnConfiguration, event));
		});
	}

	private void putActivePeriodOnTimeline(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		EiEventType eiEvent = event.getEiEvent();
		Properties properties = eiEvent.getEiActivePeriod().getProperties();
		Long activePeriodStart = Oadr20bFactory.xmlCalendarToTimestamp(properties.getDtstart().getDateTime());
		Long activePeriodEnd = Oadr20bFactory.addXMLDurationToTimestamp(activePeriodStart,
				properties.getDuration().getDuration());

		TimelineEvent startTimelineEvent = new TimelineEvent(TimelineEventType.EVENT_ACTIVE_PERIOD_STARTED,
				vtnConfiguration.getVtnId(), event.getEiEvent().getEventDescriptor().getEventID(), null, null);
		putOnTimeline(activePeriodStart, startTimelineEvent);

		TimelineEvent endTimelineEvent = new TimelineEvent(TimelineEventType.EVENT_ACTIVE_PERIOD_ENDED,
				vtnConfiguration.getVtnId(), event.getEiEvent().getEventDescriptor().getEventID(), null, null);
		putOnTimeline(activePeriodEnd, endTimelineEvent);

	}

	private void putBaselineIntervalOnTimeline(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			IntervalType interval) {
		Long intervalStart = Oadr20bFactory.xmlCalendarToTimestamp(interval.getDtstart().getDateTime());
		Long intervalEnd = Oadr20bFactory.addXMLDurationToTimestamp(intervalStart,
				interval.getDuration().getDuration());

		TimelineEvent startTimelineEvent = new TimelineEvent(TimelineEventType.BASELINE_INTERVAL_STARTED,
				vtnConfiguration.getVtnId(), event.getEiEvent().getEventDescriptor().getEventID(), null,
				interval.getUid().getText());
		putOnTimeline(intervalStart, startTimelineEvent);

		TimelineEvent endTimelineEvent = new TimelineEvent(TimelineEventType.BASELINE_INTERVAL_ENDED,
				vtnConfiguration.getVtnId(), event.getEiEvent().getEventDescriptor().getEventID(), null,
				interval.getUid().getText());
		putOnTimeline(intervalEnd, endTimelineEvent);

	}

	private void putIntervalOnTimeline(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType signal, IntervalType interval) {
		Long intervalStart = Oadr20bFactory.xmlCalendarToTimestamp(interval.getDtstart().getDateTime());
		Long intervalEnd = Oadr20bFactory.addXMLDurationToTimestamp(intervalStart,
				interval.getDuration().getDuration());

		TimelineEvent startTimelineEvent = new TimelineEvent(TimelineEventType.INTERVAL_STARTED,
				vtnConfiguration.getVtnId(), event.getEiEvent().getEventDescriptor().getEventID(), signal.getSignalID(),
				interval.getUid().getText());
		putOnTimeline(intervalStart, startTimelineEvent);

		TimelineEvent endTimelineEvent = new TimelineEvent(TimelineEventType.INTERVAL_ENDED,
				vtnConfiguration.getVtnId(), event.getEiEvent().getEventDescriptor().getEventID(), signal.getSignalID(),
				interval.getUid().getText());
		putOnTimeline(intervalEnd, endTimelineEvent);

	}

	private void putOnTimeline(Long date, TimelineEvent timelineEvent) {
		Long now = System.currentTimeMillis();
		if (date < now) {
			LOGGER.warn(String.format("%s can't be added to timeline because it already happened (date in past)",
					timelineEvent.toString()));
			return;
		}

		Map<String, List<TimelineEvent>> map = timeline.get(date);
		if (map == null) {
			map = new HashMap<>();
		}
		String key = getInnerTimelineKey(timelineEvent);
		List<TimelineEvent> list = map.get(key);
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(timelineEvent);
		map.put(key, list);
		timeline.put(date, map);
	}

	private String getInnerTimelineKey(TimelineEvent timelineEvent) {
		return timelineEvent.vtnId + ":" + timelineEvent.eventId;
	}

	private String getInnerTimelineKey(VtnSessionConfiguration vtnConfiguration, OadrEvent event) {
		return vtnConfiguration.getVtnId() + ":" + event.getEiEvent().getEventDescriptor().getEventID();
	}

	private void cancelNextTick() {
		if (scheduledNextTick == null) {
			LOGGER.warn("No schedule du cancel");
			return;
		}
		scheduledNextTick.cancel(true);
	}

	private void scheduleNextTick() {

		if (timeline.isEmpty()) {
			LOGGER.warn("Timeline empty, no tick to schedule");
			return;
		}
		Long date = timeline.firstKey();
		Long now = System.currentTimeMillis();
		if (date < now) {
			timeline.pollFirstEntry();
			scheduleNextTick();
			return;
		}

		long delay = date - now;
		OffsetDateTime ofInstant = OffsetDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
		LOGGER.info("Schedule next tick at :" + DATE_FORMATTER.format(ofInstant));
		scheduledNextTick = scheduledExecutorService.schedule(new TimelineEventTask(date), delay,
				TimeUnit.MILLISECONDS);
	}

	private class TimelineEvent implements Comparable<TimelineEvent> {

		private String vtnId;
		private String eventId;
		private String signalId;
		private String intervalId;
		private TimelineEventType type;

		public TimelineEvent(TimelineEventType type, String vtnId, String eventId, String signalId, String intervalId) {
			this.type = type;
			this.vtnId = vtnId;
			this.eventId = eventId;
			this.signalId = signalId;
			this.intervalId = intervalId;

		}

		@Override
		public String toString() {
			return "TimelineEvent [vtnId=" + vtnId + ", eventId=" + eventId + ", signalId=" + signalId + ", intervalId="
					+ intervalId + ", type=" + type + "]";
		}

		@Override
		public int compareTo(TimelineEvent o) {
			return Integer.compare(this.type.getEmitPriority(), o.type.getEmitPriority());
		}

	}

	private enum TimelineEventType {

		EVENT_ACTIVE_PERIOD_STARTED(3), INTERVAL_STARTED(5), INTERVAL_ENDED(0), EVENT_ACTIVE_PERIOD_ENDED(2),
		BASELINE_INTERVAL_STARTED(4), BASELINE_INTERVAL_ENDED(1);

		private int emitPriority;

		private TimelineEventType(int emitPriority) {
			this.emitPriority = emitPriority;
		}

		public int getEmitPriority() {
			return emitPriority;
		}

	}

	private class TimelineEventTask implements Runnable {

		private Long tick;

		public TimelineEventTask(Long tick) {
			this.tick = tick;
		}

		@Override
		public void run() {
			try {
				Map<String, List<TimelineEvent>> map = timeline.get(tick);

				List<TimelineEvent> collect = map.values().stream().flatMap(Collection::stream)
						.collect(Collectors.toList());

				List<VtnSessionConfiguration> toSync = new ArrayList<>();
				Collections.sort(collect);

				for (TimelineEvent timelineEvent : collect) {
					String vtnId = timelineEvent.vtnId;
					String eventId = timelineEvent.eventId;

					VtnSessionConfiguration vtnSessionConfiguration = vtnConfigurations.get(vtnId);
					toSync.add(vtnSessionConfiguration);
					OadrEvent oadrEvent = getEvents(vtnSessionConfiguration).get(eventId);

					switch (timelineEvent.type) {
					case EVENT_ACTIVE_PERIOD_ENDED:
						listener.onActivePeriodEnd(vtnSessionConfiguration, oadrEvent);
						break;
					case EVENT_ACTIVE_PERIOD_STARTED:
						listener.onActivePeriodStart(vtnSessionConfiguration, oadrEvent);
						break;
					case INTERVAL_ENDED:
					case INTERVAL_STARTED:
						List<EiEventSignalType> signals = oadrEvent.getEiEvent().getEiEventSignals().getEiEventSignal()
								.stream().filter(signal -> signal.getSignalID().equals(timelineEvent.signalId))
								.collect(Collectors.toList());
						EiEventSignalType eiEventSignalType = null;
						IntervalType intervalType = null;
						if (signals != null && !signals.isEmpty()) {

							if (signals.size() == 1) {

								eiEventSignalType = signals.get(0);

								List<IntervalType> intervals = eiEventSignalType
										.getIntervals().getInterval().stream().filter(interval -> interval.getUid()
												.getText().equals(timelineEvent.intervalId))
										.collect(Collectors.toList());

								if (intervals != null && !intervals.isEmpty()) {

									if (intervals.size() == 1) {

										intervalType = intervals.get(0);

									} else {
										LOGGER.warn("Multiple intervals with intervalId for timeline event: "
												+ timelineEvent.toString());
									}

								} else {
									LOGGER.warn("Unknown intervalId for timeline event: " + timelineEvent.toString());
								}

							} else {
								LOGGER.warn("Multiple signals with signalId for timeline event: "
										+ timelineEvent.toString());
							}

						} else {
							LOGGER.warn("Unknown signalId for timeline event: " + timelineEvent.toString());
						}

						if (eiEventSignalType != null && intervalType != null) {

							if (timelineEvent.type.equals(TimelineEventType.INTERVAL_STARTED)) {
								listener.onIntervalStart(vtnSessionConfiguration, oadrEvent, eiEventSignalType,
										intervalType);
							} else if (timelineEvent.type.equals(TimelineEventType.INTERVAL_ENDED)) {
								listener.onIntervalEnd(vtnSessionConfiguration, oadrEvent, eiEventSignalType,
										intervalType);
							}
						}

						break;
					case BASELINE_INTERVAL_ENDED:
					case BASELINE_INTERVAL_STARTED:
						List<IntervalType> baselineIntervals = oadrEvent.getEiEvent().getEiEventSignals()
								.getEiEventBaseline().getIntervals().getInterval().stream()
								.filter(interval -> interval.getUid().getText().equals(timelineEvent.intervalId))
								.collect(Collectors.toList());

						if (baselineIntervals != null && !baselineIntervals.isEmpty()) {

							if (baselineIntervals.size() == 1) {

								IntervalType activeBaselineInterval = baselineIntervals.get(0);

								if (timelineEvent.type.equals(TimelineEventType.BASELINE_INTERVAL_STARTED)) {
									listener.onBaselineIntervalStart(vtnSessionConfiguration, oadrEvent,
											activeBaselineInterval);
								} else if (timelineEvent.type.equals(TimelineEventType.BASELINE_INTERVAL_ENDED)) {
									listener.onBaselineIntervalEnd(vtnSessionConfiguration, oadrEvent,
											activeBaselineInterval);
								}

							} else {
								LOGGER.warn("Multiple baseline intervals with IntervalId for timeline event: "
										+ timelineEvent.toString());
							}

						} else {
							LOGGER.warn("Unknown baseline IntervalId for timeline event: " + timelineEvent.toString());
						}

						break;
					default:
						break;

					}
				}

				timeline.remove(tick);

				toSync.forEach(vtnConfig -> {
					synchronizeActiveSignals(vtnConfig);
				});

				scheduleNextTick();
			} catch (Exception e) {
				LOGGER.error("Can't process tick", e);
			}

		}

	}

	public interface EventTimelineListener {

		void onActivePeriodStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

		void onActivePeriodEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

		void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				EiEventSignalType eiEventSignalType, IntervalType intervalType);

		void onIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				EiEventSignalType eiEventSignalType, IntervalType intervalType);

		void onBaselineIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				IntervalType intervalType);

		void onBaselineIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
				IntervalType intervalType);

		void onCreatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

		void onDeletedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

		void onUpdatedEvent(VtnSessionConfiguration vtnConfiguration, OadrEvent event);

	}

	private Float getIntervalFloatValue(IntervalType interval) {
		Float value = null;
		for (JAXBElement<? extends StreamPayloadBaseType> payload : interval.getStreamPayloadBase()) {
			if (payload.getDeclaredType().equals(SignalPayloadType.class)) {

				SignalPayloadType reportPayload = (SignalPayloadType) payload.getValue();

				JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
				if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {

					PayloadFloatType payloadFloat = (PayloadFloatType) payloadBase.getValue();

					value = payloadFloat.getValue();

				}
			}
		}
		return value;
	}

	public class ActiveSignal implements Comparable<ActiveSignal> {
		private OadrEvent event;
		private EiEventSignalType signal;
		private IntervalType interval;

		public ActiveSignal(OadrEvent event, EiEventSignalType signal, IntervalType interval) {
			this.event = event;
			this.signal = signal;
			this.interval = interval;
		}

		@Override
		public int compareTo(ActiveSignal o) {
			return this.getEvent().getEiEvent().getEventDescriptor().getPriority()
					.compareTo(o.getEvent().getEiEvent().getEventDescriptor().getPriority());
		}

		public OadrEvent getEvent() {
			return event;
		}

		public EiEventSignalType getSignal() {
			return signal;
		}

		public IntervalType getInterval() {
			return interval;
		}

		public Float getValue() {
			return getIntervalFloatValue(this.interval);
		}

	}

	public class ActiveBaselineSignal {
		private OadrEvent event;
		private IntervalType interval;

		public ActiveBaselineSignal(OadrEvent event, IntervalType interval) {
			this.event = event;
			this.interval = interval;
		}

		public OadrEvent getEvent() {
			return event;
		}

		public IntervalType getInterval() {
			return interval;
		}

		public Float getValue() {
			return getIntervalFloatValue(this.interval);
		}
	}

	public void clear() {
		this.cancelNextTick();
		events.clear();
		vtnConfigurations.clear();
		timeline.clear();

	}

}
