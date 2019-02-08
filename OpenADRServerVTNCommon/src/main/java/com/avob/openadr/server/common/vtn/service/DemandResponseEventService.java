package com.avob.openadr.server.common.vtn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Resource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenDao;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.google.common.collect.Lists;

@Service
@Transactional
public class DemandResponseEventService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemandResponseEventService.class);

	@Value("${oadr.supportPush:#{false}}")
	private Boolean supportPush;

	@Resource
	private VenDao venDao;

	@Resource
	private DemandResponseEventDao demandResponseEventDao;

	@Resource
	private VenDemandResponseEventDao venDemandResponseEventDao;

	@Resource
	private DemandResponseEventPublisher demandResponseEventPublisher;

	@Resource
	private Executor executor;

	private static DatatypeFactory datatypeFactory;
	static {
		try {
			datatypeFactory = DatatypeFactory.newInstance();

		} catch (DatatypeConfigurationException e) {
			LOGGER.error("", e);
			throw new OadrVTNInitializationException(e);
		}
	}

	public Iterable<DemandResponseEvent> findAll() {
		return demandResponseEventDao.findAll();
	}

	public List<DemandResponseEvent> find(String venUsername, DemandResponseEventStateEnum state) {
		return this.find(venUsername, state, null);
	}

	public List<DemandResponseEvent> find(String venUsername, DemandResponseEventStateEnum state, Long size) {
		Pageable limit = null;
		if (size != null) {
			int i = (int) (long) size;
			limit = new PageRequest(0, i);
		}

		Iterable<DemandResponseEvent> events = new ArrayList<DemandResponseEvent>();

		if (venUsername == null && state == null && limit == null) {
			events = demandResponseEventDao.findAll();

		} else if (venUsername == null && state == null && limit != null) {
			events = demandResponseEventDao.findAll(limit);

		} else if (venUsername != null && state == null && limit == null) {
			events = demandResponseEventDao.findByVenUsername(venUsername);

		} else if (venUsername != null && state == null && limit != null) {
			events = demandResponseEventDao.findByVenUsername(venUsername, limit);

		} else if (venUsername == null && state != null && limit == null) {
			events = demandResponseEventDao.findByState(state);

		} else if (venUsername == null && state != null && limit != null) {
			events = demandResponseEventDao.findByState(state, limit);

		} else if (venUsername != null && state != null && limit == null) {
			events = demandResponseEventDao.findByVenUsernameAndState(venUsername, state);

		} else if (venUsername != null && state != null && limit != null) {
			events = demandResponseEventDao.findByVenUsernameAndState(venUsername, state, limit);
		}

		return Lists.newArrayList(events);
	}

	/**
	 * create a DR Event
	 * 
	 * @param event
	 * @return
	 */
	@Transactional
	public DemandResponseEvent create(DemandResponseEvent event) {

		event.setCreatedTimestamp(System.currentTimeMillis());
		event.setModificationNumber(0);

		event.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		Date dateStart = new Date();
		dateStart.setTime(event.getStart());

		// compute end from start and duration
		Duration duration = datatypeFactory.newDuration(event.getDuration());
		long durationInMillis = duration.getTimeInMillis(dateStart);
		event.setEnd(dateStart.getTime() + durationInMillis);

		// compute startNotification from start and notificationDuration
		Duration notificationDuration = datatypeFactory.newDuration(event.getNotificationDuration());
		long notificationDurationInMillis = notificationDuration.getTimeInMillis(dateStart);
		event.setStartNotification(dateStart.getTime() - notificationDurationInMillis);

		List<String> targetedVenUsername = Arrays.asList(event.getComaSeparatedTargetedVenUsername().split(","));

		List<Ven> findByUsernameIn = venDao.findByUsernameInAndVenMarketContextsContains(targetedVenUsername,
				event.getMarketContext());
		List<VenDemandResponseEvent> list = new ArrayList<VenDemandResponseEvent>();
		for (Ven ven : findByUsernameIn) {
			if (supportPush && ven.getPushUrl() != null && demandResponseEventPublisher != null) {
				demandResponseEventPublisher.publish20a(ven);
			}
			list.add(new VenDemandResponseEvent(event, ven));
		}
		venDemandResponseEventDao.save(list);
		return demandResponseEventDao.save(event);
	}

	/**
	 * create a DR Event
	 * 
	 * @param event
	 * @return
	 */
	@Transactional(readOnly = false)
	private DemandResponseEvent persist(DemandResponseEvent event, List<VenDemandResponseEvent> list) {
		venDemandResponseEventDao.save(list);
		return demandResponseEventDao.save(event);
	}

	private void publish(List<Ven> vens, DemandResponseEventOadrProfileEnum profile) {
		Runnable run = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					for (Ven ven : vens) {
						// the receiver check if ven is a push ven and how it
						// does
						// make
						// event available if not
						if (DemandResponseEventOadrProfileEnum.OADR20B.equals(profile)
								&& DemandResponseEventOadrProfileEnum.OADR20B.getCode().equals(ven.getOadrProfil())) {
							demandResponseEventPublisher.publish20b(ven);
						} else if (DemandResponseEventOadrProfileEnum.OADR20A.equals(profile)) {
							demandResponseEventPublisher.publish20a(ven);
						}
					}
				} catch (InterruptedException e) {
					LOGGER.error("", e);
				}
			}

		};

		executor.execute(run);

	}

	private DemandResponseEvent createOrUpdate(DemandResponseEvent event, String eventId, long modificationNumber,
			Long start, String durationXml, String notificationDurationXml, List<String> targetedVenUsername,
			VenMarketContext marketContext, DemandResponseEventStateEnum state, String payload,
			DemandResponseEventOadrProfileEnum profile) {
		event.setEventId(eventId);
		event.setCreatedTimestamp(System.currentTimeMillis());
		event.setModificationNumber(modificationNumber);
		event.setComaSeparatedTargetedVenUsername(String.join(",", targetedVenUsername));
		event.setStart(start);
		event.setMarketContext(marketContext);
		event.setDuration(durationXml);
		event.setNotificationDuration(notificationDurationXml);
		event.setState(state);
		event.setEvent(payload);
		Date dateStart = new Date();
		dateStart.setTime(start);
		event.setOadrProfile(profile);
		// compute end from start and duration
		Duration duration = datatypeFactory.newDuration(durationXml);
		long durationInMillis = duration.getTimeInMillis(dateStart);
		event.setEnd(dateStart.getTime() + durationInMillis);

		// compute startNotification from start and notificationDuration
		Duration notificationDuration = datatypeFactory.newDuration(notificationDurationXml);
		long notificationDurationInMillis = notificationDuration.getTimeInMillis(dateStart);
		event.setStartNotification(dateStart.getTime() - notificationDurationInMillis);

		List<Ven> findByUsernameIn = venDao.findByUsernameInAndVenMarketContextsContains(targetedVenUsername,
				marketContext);
		List<VenDemandResponseEvent> list = new ArrayList<VenDemandResponseEvent>();

		for (Ven ven : findByUsernameIn) {
			list.add(new VenDemandResponseEvent(event, ven));
		}

		DemandResponseEvent save = this.persist(event, list);
		this.publish(findByUsernameIn, profile);

		return save;

	}

	public DemandResponseEvent update(DemandResponseEvent event, String eventId, long modificationNumber, Long start,
			String durationXml, String notificationDurationXml, List<String> targetedVenUsername,
			VenMarketContext marketContext, DemandResponseEventStateEnum state, String payload,
			DemandResponseEventOadrProfileEnum profile) {
		venDemandResponseEventDao.deleteByEventId(event.getId());
		return createOrUpdate(event, eventId, modificationNumber, start, durationXml, notificationDurationXml,
				targetedVenUsername, marketContext, state, payload, profile);
	}

	public DemandResponseEvent create(String eventId, Long start, String durationXml, String notificationDurationXml,
			List<String> targetedVenUsername, VenMarketContext marketContext, DemandResponseEventStateEnum state,
			String payload, DemandResponseEventOadrProfileEnum profile) {
		return createOrUpdate(new DemandResponseEvent(), eventId, 0L, start, durationXml, notificationDurationXml,
				targetedVenUsername, marketContext, state, payload, profile);
	}

	public DemandResponseEvent findById(Long id) {
		return demandResponseEventDao.findOne(id);
	}

	public DemandResponseEvent findByEventId(String eventId) {
		return demandResponseEventDao.findOneByEventId(eventId);
	}

	public boolean delete(Long id) {
		boolean exists = demandResponseEventDao.exists(id);
		if (exists) {
			venDemandResponseEventDao.deleteByEventId(id);
			demandResponseEventDao.delete(id);
		}
		return exists;
	}

	public void delete(Iterable<DemandResponseEvent> entities) {
		venDemandResponseEventDao.deleteByEventIn(Lists.newArrayList(entities));
		demandResponseEventDao.delete(entities);
	}

	private DemandResponseEvent updateState(Long id, DemandResponseEventStateEnum state) {
		DemandResponseEvent event = demandResponseEventDao.findOne(id);
		if (event == null) {
			return null;
		}
		if (!event.getState().equals(state)) {
			event.setState(state);
			event.setModificationNumber(event.getModificationNumber() + 1);
			event.setLastUpdateTimestamp(System.currentTimeMillis());
			return demandResponseEventDao.save(event);
		}

		return event;
	}

	public DemandResponseEvent cancel(Long id) {
		return updateState(id, DemandResponseEventStateEnum.CANCELED);
	}

	public DemandResponseEvent active(Long id) {
		return updateState(id, DemandResponseEventStateEnum.ACTIVE);
	}

	public DemandResponseEvent updateValue(Long id, DemandResponseEventSimpleValueEnum value) {
		DemandResponseEvent event = demandResponseEventDao.findOne(id);
		if (event == null) {
			return null;
		}

		if (!value.equals(event.getValue())) {
			event.setValue(value);
			event.setModificationNumber(event.getModificationNumber() + 1);
			event.setLastUpdateTimestamp(System.currentTimeMillis());
			return demandResponseEventDao.save(event);
		}

		return event;
	}

	public List<DemandResponseEvent> findToSentEventByVen(Ven ven) {
		return this.findToSentEventByVen(ven, null);
	}

	public List<DemandResponseEvent> findToSentEventByVen(Ven ven, Long size) {
		long currentTimeMillis = System.currentTimeMillis();

		if (size != null && size > 0) {
			Pageable limit = null;
			int i = (int) (long) size;
			limit = new PageRequest(0, i);
			return demandResponseEventDao.findToSentEventByVen(ven, currentTimeMillis, limit);
		} else {
			return demandResponseEventDao.findToSentEventByVen(ven, currentTimeMillis);
		}
	}

	public boolean hasResponded(String venId, DemandResponseEvent event) {
		VenDemandResponseEvent findOneByEventAndVen = venDemandResponseEventDao.findOneByEventAndVenId(event, venId);
		return findOneByEventAndVen.getVenOpt() != null;
	}

	/**
	 * update ven optin for a specific event
	 * 
	 * @param demandResponseEvent
	 * @param ven
	 * @param venOpt
	 */
	public void updateVenOpt(Long demandResponseEventId, Long modificationNumber, String venUsername,
			DemandResponseEventOptEnum venOpt) {

		VenDemandResponseEvent findOneByEventIdAndVenId = venDemandResponseEventDao
				.findOneByEventIdAndVenUsername(demandResponseEventId, venUsername);

		if (findOneByEventIdAndVenId != null) {
			findOneByEventIdAndVenId.setVenOpt(venOpt);
			findOneByEventIdAndVenId.setLastSentModificationNumber(modificationNumber);
			venDemandResponseEventDao.save(findOneByEventIdAndVenId);
		}
	}

	/**
	 * get ven optin for a specific event
	 * 
	 * @param demandResponseEvent
	 * @param ven
	 * @param venOpt
	 */
	public DemandResponseEventOptEnum getVenOpt(Long demandResponseEventId, String venUsername) {

		VenDemandResponseEvent findOneByEventIdAndVenId = venDemandResponseEventDao
				.findOneByEventIdAndVenUsername(demandResponseEventId, venUsername);

		if (findOneByEventIdAndVenId != null) {
			return findOneByEventIdAndVenId.getVenOpt();
		}
		return null;
	}

	public List<DemandResponseEvent> findToSentEventByVenUsername(String username) {
		return this.findToSentEventByVenUsername(username, null);
	}

	public List<DemandResponseEvent> findToSentEventByVenUsername(String username, Long size) {
		long currentTimeMillis = System.currentTimeMillis() - 60 * 1000;
		if (size != null && size > 0) {
			Pageable limit = null;
			int i = (int) (long) size;
			limit = new PageRequest(0, i);
			return demandResponseEventDao.findToSentEventByVenUsername(username, currentTimeMillis, limit);
		} else {
			return demandResponseEventDao.findToSentEventByVenUsername(username, currentTimeMillis);
		}
	}

}
