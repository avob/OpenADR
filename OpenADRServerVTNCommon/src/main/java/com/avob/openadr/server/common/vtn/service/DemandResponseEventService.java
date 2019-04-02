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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignal;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSpecification;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventTarget;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventTargetInterface;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventUpdateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenDao;
import com.avob.openadr.server.common.vtn.models.ven.VenSpecification;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDao;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.google.common.collect.Lists;

@Service
@Transactional
public class DemandResponseEventService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemandResponseEventService.class);

	private static final Integer DEFAULT_SEARCH_SIZE = 20;

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
			events = demandResponseEventDao.findAll(DemandResponseEventSpecification.hasVenUsername(venUsername));

		} else if (venUsername != null && state == null && limit != null) {
			events = demandResponseEventDao.findAll(DemandResponseEventSpecification.hasVenUsername(venUsername),
					limit);
		} else if (venUsername == null && state != null && limit == null) {
			events = demandResponseEventDao.findAll(DemandResponseEventSpecification.hasDescriptorState(state));

		} else if (venUsername == null && state != null && limit != null) {
			events = demandResponseEventDao.findAll(DemandResponseEventSpecification.hasDescriptorState(state), limit);

		} else if (venUsername != null && state != null && limit == null) {
			events = demandResponseEventDao.findAll(DemandResponseEventSpecification.hasDescriptorState(state)
					.and(DemandResponseEventSpecification.hasVenUsername(venUsername)));

		} else if (venUsername != null && state != null && limit != null) {
			events = demandResponseEventDao.findAll(DemandResponseEventSpecification.hasDescriptorState(state)
					.and(DemandResponseEventSpecification.hasVenUsername(venUsername)), limit);
		}

		return Lists.newArrayList(events);
	}

	private List<Ven> findVenForTarget(DemandResponseEvent event,
			List<? extends DemandResponseEventTargetInterface> targets) {
		Specification<Ven> targetedSpecification = null;
		if (targets != null) {

			for (DemandResponseEventTargetInterface target : targets) {
				if ("ven".equals(target.getTargetType())) {
					if (targetedSpecification == null) {
						targetedSpecification = VenSpecification.hasVenIdEquals(target.getTargetId());
					} else {
						targetedSpecification = targetedSpecification
								.or(VenSpecification.hasVenIdEquals(target.getTargetId()));
					}

				} else if ("group".equals(target.getTargetType())) {
					if (targetedSpecification == null) {
						targetedSpecification = VenSpecification.hasGroup(target.getTargetId());
					} else {
						targetedSpecification = targetedSpecification
								.or(VenSpecification.hasGroup(target.getTargetId()));
					}
				}
			}

		}
		if (targetedSpecification == null) {
			targetedSpecification = VenSpecification
					.hasMarketContext(event.getDescriptor().getMarketContext().getName());
		} else {
			targetedSpecification = targetedSpecification
					.and(VenSpecification.hasMarketContext(event.getDescriptor().getMarketContext().getName()));
		}
		return venDao.findAll(targetedSpecification);
	}

	/**
	 * create a DR Event
	 * 
	 * @param event
	 * @return
	 */
	@Transactional(readOnly = false)
	public DemandResponseEvent create(DemandResponseEventCreateDto dto) {
		DemandResponseEvent event = dtoMapper.map(dto, DemandResponseEvent.class);
		event.setCreatedTimestamp(System.currentTimeMillis());
		event.setLastUpdateTimestamp(System.currentTimeMillis());
		if (dto.isPublished()) {
			event.getDescriptor().setModificationNumber(0);
		} else {
			event.getDescriptor().setModificationNumber(-1);
		}

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

		// save event
		DemandResponseEvent save = demandResponseEventDao.save(event);

		// link targets
		List<Ven> findByUsernameIn = findVenForTarget(event, dto.getTargets());

		List<VenDemandResponseEvent> list = new ArrayList<VenDemandResponseEvent>();
		for (Ven ven : findByUsernameIn) {
//			if (supportPush && ven.getPushUrl() != null && demandResponseEventPublisher != null) {
			if (demandResponseEventPublisher != null) {
				if (event.isPublished()) {
					pushAsync(Arrays.asList(ven), dto.getDescriptor().getOadrProfile());
				}
			}
			list.add(new VenDemandResponseEvent(event, ven));
		}
		venDemandResponseEventDao.saveAll(list);

		// link signals
		event.getSignals().forEach(sig -> {
			sig.setEvent(save);
		});
		demandResponseEventSignalDao.saveAll(event.getSignals());

		return save;
	}

	@Transactional(readOnly = false)
	public DemandResponseEvent update(DemandResponseEvent event, DemandResponseEventUpdateDto dto) {

		List<DemandResponseEventTargetDto> toAdd = new ArrayList<>();
		List<DemandResponseEventTargetDto> unchanged = new ArrayList<>();

		for (DemandResponseEventTargetDto updateTarget : dto.getTargets()) {
			boolean found = false;
			for (DemandResponseEventTarget target : event.getTargets()) {
				if (target.getTargetType().equals(updateTarget.getTargetType())
						&& target.getTargetId().equals(updateTarget.getTargetId())) {
					found = true;
					unchanged.add(updateTarget);
				}
			}
			if (!found) {
				toAdd.add(updateTarget);
			}

		}

		List<DemandResponseEventTarget> toRemove = new ArrayList<>();
		if (dto.getTargets().size() < unchanged.size() + toAdd.size()) {
			for (DemandResponseEventTarget target : event.getTargets()) {
				boolean found = false;
				for (DemandResponseEventTargetDto updateTarget : dto.getTargets()) {
					if (target.getTargetType().equals(updateTarget.getTargetType())
							&& target.getTargetId().equals(updateTarget.getTargetId())) {
						found = true;
					}
				}

				if (!found) {
					toRemove.add(target);
				}
			}
		}

		// update modification number if event is published
		if (dto.isPublished()) {
			event.getDescriptor().setModificationNumber(event.getDescriptor().getModificationNumber() + 1);
		}

		DemandResponseEvent partialUpdate = dtoMapper.map(dto, DemandResponseEvent.class);
		// link signals
		demandResponseEventSignalDao.deleteAll(event.getSignals());
		partialUpdate.getSignals().forEach(sig -> {
			sig.setEvent(event);
		});
		demandResponseEventSignalDao.saveAll(partialUpdate.getSignals());
		// update event
		event.setSignals(partialUpdate.getSignals());
		event.setTargets(partialUpdate.getTargets());
		event.setLastUpdateTimestamp(System.currentTimeMillis());
		event.setPublished(partialUpdate.isPublished());
		DemandResponseEvent save = demandResponseEventDao.save(event);

		// link added targets
		if (toAdd.size() > 0) {
			List<Ven> vens = findVenForTarget(event, toAdd);
			List<VenDemandResponseEvent> list = new ArrayList<VenDemandResponseEvent>();
			for (Ven ven : vens) {
				if (supportPush && ven.getPushUrl() != null && demandResponseEventPublisher != null) {
					if (dto.isPublished()) {
						pushAsync(Arrays.asList(ven), event.getDescriptor().getOadrProfile());
					}
				}
				list.add(new VenDemandResponseEvent(event, ven));
			}
			venDemandResponseEventDao.saveAll(list);
		}

		// unlink removed target
		if (toRemove.size() > 0) {
			List<Ven> vens = findVenForTarget(event, toRemove);
			venDemandResponseEventDao.deleteByEventIdAndVenIn(event.getId(), vens);
		}

		if (dto.isPublished()) {
			this.distributeEventToPushVen(event);
		}

		return save;
	}

	public void distributeEventToPushVen(DemandResponseEvent event) {
		List<Ven> vens = findVenForTarget(event, event.getTargets());
		for (Ven ven : vens) {
			if (supportPush && ven.getPushUrl() != null && demandResponseEventPublisher != null) {
				pushAsync(Arrays.asList(ven), event.getDescriptor().getOadrProfile());
			}
		}
	}

	@Transactional(readOnly = false)
	public DemandResponseEvent publish(DemandResponseEvent event) {
		if (!event.isPublished()) {
			event.getDescriptor().setModificationNumber(event.getDescriptor().getModificationNumber() + 1);
			event.setPublished(true);
			demandResponseEventDao.save(event);
		}

		distributeEventToPushVen(event);
		return event;
	}

	@Transactional(readOnly = false)
	public DemandResponseEvent active(DemandResponseEvent event) {
		if (!event.getDescriptor().getState().equals(DemandResponseEventStateEnum.ACTIVE)) {
			event.getDescriptor().setState(DemandResponseEventStateEnum.ACTIVE);
			event.getDescriptor().setModificationNumber(event.getDescriptor().getModificationNumber() + 1);
			event.setPublished(true);

			demandResponseEventDao.save(event);
			distributeEventToPushVen(event);

		}
		return event;
	}

	@Transactional(readOnly = false)
	public DemandResponseEvent cancel(DemandResponseEvent event) {
		if (!event.getDescriptor().getState().equals(DemandResponseEventStateEnum.CANCELLED)) {
			event.getDescriptor().setState(DemandResponseEventStateEnum.CANCELLED);
			event.getDescriptor().setModificationNumber(event.getDescriptor().getModificationNumber() + 1);
			event.setPublished(true);

			demandResponseEventDao.save(event);
			distributeEventToPushVen(event);
		}
		return event;
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
//					if (DemandResponseEventOadrProfileEnum.OADR20B.equals(profile)
//							&& DemandResponseEventOadrProfileEnum.OADR20B.getCode().equals(ven.getOadrProfil())) {
//						
//					} else 
					if (DemandResponseEventOadrProfileEnum.OADR20A.equals(profile)) {
						demandResponseEventPublisher.publish20a(ven);
					} else {
						demandResponseEventPublisher.publish20b(ven);
					}
				}
			}

		};

		executor.execute(run);

	}

	public Optional<DemandResponseEvent> findById(Long id) {
		return demandResponseEventDao.findById(id);
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

	public void deleteById(Iterable<Long> entities) {
		for (Long id : entities) {
			venDemandResponseEventDao.deleteByEventId(id);

		}

		demandResponseEventDao.deleteByIdIn(entities);
	}

	public boolean exists(Long id) {
		return demandResponseEventDao.existsById(id);
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
			findOneByEventIdAndVenId.setLastUpdateDatetime(System.currentTimeMillis());
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
		if (size != null && size > 0) {
			Pageable limit = null;
			int i = (int) (long) size;
			limit = PageRequest.of(0, i, Sort.by(Sort.Direction.ASC, "activePeriod.start"));

			Page<DemandResponseEvent> findAll = demandResponseEventDao
					.findAll(DemandResponseEventSpecification.toSentByVenUsername(username), limit);
			return findAll.getContent();

		} else {
			return demandResponseEventDao.findAll(DemandResponseEventSpecification.toSentByVenUsername(username),
					Sort.by(Sort.Direction.ASC, "activePeriod.start"));
		}
	}

	public List<VenDemandResponseEvent> getVenDemandResponseEvent(Long demandResponseEventId)
			throws OadrElementNotFoundException {
		Optional<DemandResponseEvent> findById = demandResponseEventDao.findById(demandResponseEventId);
		if (!findById.isPresent()) {
			throw new OadrElementNotFoundException("Event not found");
		}
		DemandResponseEvent event = findById.get();
		return venDemandResponseEventDao.findByEvent(event);
	}

	public Page<DemandResponseEvent> search(List<DemandResponseEventFilter> filters, Long start, Long end) {
		return search(filters, start, end, null, null);
	}

	public Page<DemandResponseEvent> search(List<DemandResponseEventFilter> filters, Long start, Long end, Integer page,
			Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = DEFAULT_SEARCH_SIZE;
		}
		Specification<DemandResponseEvent> spec = Specification.where(DemandResponseEventSpecification.search(filters));

		if (start != null) {
			spec = spec.and(DemandResponseEventSpecification.hasActivePeriodStartAfter(start));
		}

		if (end != null) {
			spec = spec.and(DemandResponseEventSpecification.hasActivePeriodEndNullOrBefore(end));
		}

		return demandResponseEventDao.findAll(spec,
				PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "activePeriod.start")));
	}

}
