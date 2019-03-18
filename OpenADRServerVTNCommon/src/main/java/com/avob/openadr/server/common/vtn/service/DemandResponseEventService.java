package com.avob.openadr.server.common.vtn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignal;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventTarget;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventTargetDto;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenDao;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDao;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
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
	private DemandResponseEventSignalDao demandResponseEventSignalDao;

	@Resource
	private Executor executor;

	@Resource
	private DtoMapper dtoMapper;

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
			limit = PageRequest.of(0, i);
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
	public DemandResponseEvent create(DemandResponseEventDto dto) {
		DemandResponseEvent event = dtoMapper.map(dto, DemandResponseEvent.class);
		event.setCreatedTimestamp(System.currentTimeMillis());
		event.setModificationNumber(0);

//		event.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20A);

		Date dateStart = new Date();
		dateStart.setTime(event.getActivePeriod().getStart());

		// compute end from start and duration
		Duration duration = datatypeFactory.newDuration(event.getActivePeriod().getDuration());
		long durationInMillis = duration.getTimeInMillis(dateStart);
		event.getActivePeriod().setEnd(dateStart.getTime() + durationInMillis);

		// compute startNotification from start and notificationDuration
		Duration notificationDuration = datatypeFactory.newDuration(event.getActivePeriod().getNotificationDuration());
		long notificationDurationInMillis = notificationDuration.getTimeInMillis(dateStart);
		event.getActivePeriod().setStartNotification(dateStart.getTime() - notificationDurationInMillis);
		List<Ven> findByUsernameIn = null;

		if (dto.getTargets() != null) {
			List<String> targetedVenUsername = new ArrayList<>();

			List<DemandResponseEventTargetDto> targets = dto.getTargets();
			for (DemandResponseEventTargetDto target : targets) {
				if ("ven".equals(target.getTargetType())) {
					targetedVenUsername.add(target.getTargetId());
				}
			}
			findByUsernameIn = venDao.findByUsernameInAndVenMarketContextsContains(targetedVenUsername,
					event.getDescriptor().getMarketContext());
		} else {
			findByUsernameIn = venDao.findByVenMarketContextsName(event.getDescriptor().getMarketContext().getName());
		}

		List<VenDemandResponseEvent> list = new ArrayList<VenDemandResponseEvent>();
		for (Ven ven : findByUsernameIn) {
			if (supportPush && ven.getPushUrl() != null && demandResponseEventPublisher != null) {
				if (DemandResponseEventStateEnum.ACTIVE.equals(dto.getState())) {
					pushAsync(Arrays.asList(ven), dto.getOadrProfile());
				}
			}
			list.add(new VenDemandResponseEvent(event, ven));
		}
		venDemandResponseEventDao.saveAll(list);
		DemandResponseEvent save = demandResponseEventDao.save(event);
		event.getSignals().forEach(sig -> {
			sig.setEvent(save);
		});
		demandResponseEventSignalDao.saveAll(event.getSignals());
		return save;
	}

	public void publishEvent(DemandResponseEvent event) {
		if (!DemandResponseEventStateEnum.ACTIVE.equals(event.getState())) {
			// only ACTIVE event shoudl be published
			return;
		}
		List<Ven> findByUsernameIn = null;
		if (event.getTargets() != null) {
			List<String> targetedVenUsername = new ArrayList<>();

			List<DemandResponseEventTarget> targets = event.getTargets();
			for (DemandResponseEventTarget target : targets) {
				if ("ven".equals(target.getTargetType())) {
					targetedVenUsername.add(target.getTargetId());
				}
			}
			findByUsernameIn = venDao.findByUsernameInAndVenMarketContextsContains(targetedVenUsername,
					event.getDescriptor().getMarketContext());
		} else {
			findByUsernameIn = venDao.findByVenMarketContextsName(event.getDescriptor().getMarketContext().getName());
		}
		for (Ven ven : findByUsernameIn) {
			if (supportPush && ven.getPushUrl() != null && demandResponseEventPublisher != null) {

				pushAsync(Arrays.asList(ven), event.getOadrProfile());

			}
		}
	}

	/**
	 * create a DR Event
	 * 
	 * @param event
	 * @return
	 */
	@Transactional(readOnly = false)
	private DemandResponseEvent persist(DemandResponseEvent event, List<VenDemandResponseEvent> list) {
		venDemandResponseEventDao.saveAll(list);
		return demandResponseEventDao.save(event);
	}

	private void pushAsync(List<Ven> vens, DemandResponseEventOadrProfileEnum profile) {
		Runnable run = new Runnable() {

			@Override
			public void run() {
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
			}

		};

		executor.execute(run);

	}

	public Optional<DemandResponseEvent> findById(Long id) {
		return demandResponseEventDao.findById(id);
	}

	public DemandResponseEvent findByEventId(String eventId) {
		return demandResponseEventDao.findOneByEventId(eventId);
	}

	public List<DemandResponseEventSignal> getSignals(DemandResponseEvent event) {
		return demandResponseEventSignalDao.findByEvent(event);
	}

	public boolean delete(Long id) {
		boolean exists = demandResponseEventDao.existsById(id);
		if (exists) {
			venDemandResponseEventDao.deleteByEventId(id);
			demandResponseEventDao.deleteById(id);
		}
		return exists;
	}

	public void delete(Iterable<DemandResponseEvent> entities) {
		venDemandResponseEventDao.deleteByEventIn(Lists.newArrayList(entities));
		demandResponseEventDao.deleteAll(entities);
	}

	private DemandResponseEvent updateState(DemandResponseEvent event, DemandResponseEventStateEnum state) {

		if (!event.getState().equals(state)) {
			event.setState(state);
			event.setModificationNumber(event.getModificationNumber() + 1);
			event.setLastUpdateTimestamp(System.currentTimeMillis());
			return demandResponseEventDao.save(event);
		}

		return event;
	}

	public DemandResponseEvent cancel(Long id) {
		Optional<DemandResponseEvent> op = demandResponseEventDao.findById(id);
		if (!op.isPresent()) {
			return null;
		}
		DemandResponseEvent event = op.get();
		return updateState(event, DemandResponseEventStateEnum.CANCELED);
	}

	public DemandResponseEvent active(Long id) {
		Optional<DemandResponseEvent> op = demandResponseEventDao.findById(id);
		if (!op.isPresent()) {
			return null;
		}
		DemandResponseEvent event = op.get();
		this.publishEvent(event);
		if(DemandResponseEventStateEnum.ACTIVE.equals(event.getState())) {
			return event;
		}
		return updateState(event, DemandResponseEventStateEnum.ACTIVE);
	}

	public DemandResponseEvent unpublish(Long id) {
		Optional<DemandResponseEvent> op = demandResponseEventDao.findById(id);
		if (!op.isPresent()) {
			return null;
		}
		DemandResponseEvent event = op.get();
		return updateState(event, DemandResponseEventStateEnum.UNPUBLISHED);
	}

	public DemandResponseEvent updateValue(Long id, Float value) {
		Optional<DemandResponseEvent> op = demandResponseEventDao.findById(id);
		if (!op.isPresent()) {
			return null;
		}
		DemandResponseEvent event = op.get();

		if (!value.equals(event.getSignals().toArray(new DemandResponseEventSignal[0])[0].getCurrentValue())) {
			event.getSignals().toArray(new DemandResponseEventSignal[0])[0].setCurrentValue(value);
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
			limit = PageRequest.of(0, i);
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
			limit = PageRequest.of(0, i);
			return demandResponseEventDao.findToSentEventByVenUsername(username, currentTimeMillis, limit);
		} else {
			return demandResponseEventDao.findToSentEventByVenUsername(username, currentTimeMillis);
		}
	}

}
