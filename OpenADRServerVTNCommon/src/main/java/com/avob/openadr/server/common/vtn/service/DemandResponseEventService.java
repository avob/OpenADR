package com.avob.openadr.server.common.vtn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
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
import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.TargetDto;
import com.avob.openadr.server.common.vtn.models.TargetInterface;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventBaselineDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignal;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalDao;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSpecification;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventUpdateDto;
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

	private static final Sort SORT_ASC_ACTIVEPERIOD = Sort.by(Sort.Direction.ASC, "activePeriod.start");

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

	@Resource
	private DemandResponseEventBaselineDao demandResponseEventBaselineDao;

	private DatatypeFactory datatypeFactory;

	@PostConstruct
	public void init() throws DatatypeConfigurationException {
		datatypeFactory = DatatypeFactory.newInstance();
	}

	private List<Ven> findVenForTarget(DemandResponseEvent event, List<? extends TargetInterface> targets) {
		Specification<Ven> targetedSpecification = null;
		if (targets != null) {

			for (TargetInterface target : targets) {
				if (TargetTypeEnum.VEN.equals(target.getTargetType())) {
					if (targetedSpecification == null) {
						targetedSpecification = VenSpecification.hasVenIdEquals(target.getTargetId());
					} else {
						targetedSpecification = targetedSpecification
								.or(VenSpecification.hasVenIdEquals(target.getTargetId()));
					}

				} else if (TargetTypeEnum.GROUP.equals(target.getTargetType())) {
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

		if (event.getBaseline() != null) {
			demandResponseEventBaselineDao.save(event.getBaseline());
		}
		// link targets
		List<Ven> findByUsernameIn = findVenForTarget(event, dto.getTargets());

		List<VenDemandResponseEvent> list = new ArrayList<>();
		for (Ven ven : findByUsernameIn) {
			list.add(new VenDemandResponseEvent(event, ven));
		}
		venDemandResponseEventDao.saveAll(list);

		// link signals
		event.getSignals().forEach(sig -> {
			sig.setEvent(save);
		});
		demandResponseEventSignalDao.saveAll(event.getSignals());

		for (Ven ven : findByUsernameIn) {
			if (demandResponseEventPublisher != null && event.isPublished()) {
				pushAsync(ven, event.getDescriptor().getOadrProfile());
			}
		}

		return save;
	}

	@Transactional(readOnly = false)
	public DemandResponseEvent update(DemandResponseEvent event, DemandResponseEventUpdateDto dto) {

		List<Target> toKeep = new ArrayList<>();
		List<TargetDto> toAdd = new ArrayList<>();
		for (TargetDto updateTarget : dto.getTargets()) {
			boolean found = false;
			for (Target target : event.getTargets()) {
				if (target.getTargetType().equals(updateTarget.getTargetType())
						&& target.getTargetId().equals(updateTarget.getTargetId())) {
					found = true;
					toKeep.add(target);
				}
			}
			if (!found) {
				toAdd.add(updateTarget);
			}

		}

		if (!toAdd.isEmpty()) {
			List<VenDemandResponseEvent> list = new ArrayList<>();
			List<Ven> findVenForTarget = findVenForTarget(event, toAdd);
			for (Ven ven : findVenForTarget) {
				list.add(new VenDemandResponseEvent(event, ven));
			}
			if (!list.isEmpty()) {
				venDemandResponseEventDao.saveAll(list);
			}

		}

		Set<Target> actualSet = new HashSet<Target>(event.getTargets());
		actualSet.removeAll(toKeep);
		// unlink removed target
		if (!actualSet.isEmpty()) {
			List<Ven> vens = findVenForTarget(event, new ArrayList<>(actualSet));
			venDemandResponseEventDao.deleteByEventIdAndVenIn(event.getId(), vens);
		}

		// update modification number if event is published
		if (dto.getPublished()) {
			event.getDescriptor().setModificationNumber(event.getDescriptor().getModificationNumber() + 1);
		}

		DemandResponseEvent partialUpdate = dtoMapper.map(dto, DemandResponseEvent.class);
		// link signals
		demandResponseEventSignalDao.deleteAll(event.getSignals());
		partialUpdate.getSignals().forEach(sig -> sig.setEvent(event));
		demandResponseEventSignalDao.saveAll(partialUpdate.getSignals());

		// update event
		event.getSignals().clear();
		event.getTargets().clear();
		event.getSignals().addAll(partialUpdate.getSignals());
		event.getTargets().addAll(partialUpdate.getTargets());
		event.setLastUpdateTimestamp(System.currentTimeMillis());
		event.setPublished(partialUpdate.isPublished());
		DemandResponseEvent save = demandResponseEventDao.save(event);

		if (dto.getPublished()) {
			this.distributeEventToPushVen(event);
		}

		return save;
	}

	public void distributeEventToPushVen(DemandResponseEvent event) {
		List<Ven> vens = findVenForTarget(event, event.getTargets());
		for (Ven ven : vens) {
			pushAsync(ven, event.getDescriptor().getOadrProfile());
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

	private void pushAsync(Ven ven, DemandResponseEventOadrProfileEnum profile) {

		if (DemandResponseEventOadrProfileEnum.OADR20A.equals(profile)) {
			demandResponseEventPublisher.publish20a(ven);
		} else {
			demandResponseEventPublisher.publish20b(ven);
		}

	}

	public Optional<DemandResponseEvent> findById(Long id) {
		return demandResponseEventDao.findById(id);
	}

	public List<DemandResponseEventSignal> getSignals(DemandResponseEvent event) {
		return demandResponseEventSignalDao.findByEvent(event);
	}

	@Transactional(readOnly = false)
	public boolean delete(Long id) {
		boolean exists = demandResponseEventDao.existsById(id);
		if (exists) {
			demandResponseEventSignalDao.deleteByEventId(id);
			venDemandResponseEventDao.deleteByEventId(id);
			demandResponseEventDao.deleteById(id);
		}
		return exists;
	}

	@Transactional(readOnly = false)
	public void delete(Iterable<DemandResponseEvent> entities) {
		List<Long> ids = new ArrayList<>();
		for (DemandResponseEvent event : entities) {
			ids.add(event.getId());
		}
		demandResponseEventSignalDao.deleteByEventIn(Lists.newArrayList(entities));
		venDemandResponseEventDao.deleteByEventIn(Lists.newArrayList(entities));
		demandResponseEventDao.deleteByIdIn(ids);
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
	 * @throws OadrElementNotFoundException
	 */
	public void updateVenDemandResponseEvent(Long demandResponseEventId, Long modificationNumber, String venUsername,
			DemandResponseEventOptEnum venOpt) {

		VenDemandResponseEvent findOneByEventIdAndVenId = venDemandResponseEventDao
				.findOneByEventIdAndVenUsername(demandResponseEventId, venUsername);

		if (findOneByEventIdAndVenId == null) {
			LOGGER.warn("Missing VenDemandResponseEvent for ven: " + venUsername + " and eventId: "
					+ demandResponseEventId);
			return;
		}

		findOneByEventIdAndVenId.setVenOpt(venOpt);
		findOneByEventIdAndVenId.setLastSentModificationNumber(modificationNumber);
		findOneByEventIdAndVenId.setLastUpdateDatetime(System.currentTimeMillis());
		venDemandResponseEventDao.save(findOneByEventIdAndVenId);
	}

	/**
	 * get ven response for a specific event
	 * 
	 * @param demandResponseEvent
	 * @param ven
	 * @param venOpt
	 * @throws OadrElementNotFoundException
	 */
	public VenDemandResponseEvent getVenDemandResponseEvent(Long demandResponseEventId, String venUsername)
			throws OadrElementNotFoundException {
		VenDemandResponseEvent findOneByEventIdAndVenUsername = venDemandResponseEventDao
				.findOneByEventIdAndVenUsername(demandResponseEventId, venUsername);
		if (findOneByEventIdAndVenUsername == null) {
			throw new OadrElementNotFoundException("Missing VenDemandResponseEvent for event: " + demandResponseEventId
					+ " and venID: " + venUsername);
		}
		return findOneByEventIdAndVenUsername;

	}

	/**
	 * get ven optin for a specific event
	 * 
	 * @param demandResponseEvent
	 * @param ven
	 * @param venOpt
	 * @throws OadrElementNotFoundException
	 */
	public DemandResponseEventOptEnum getVenDemandResponseEventOpt(Long demandResponseEventId, String venUsername)
			throws OadrElementNotFoundException {
		return this.getVenDemandResponseEvent(demandResponseEventId, venUsername).getVenOpt();
	}

	public List<DemandResponseEvent> findToSentEventByVenUsername(String username) {
		return this.findToSentEventByVenUsername(username, null);
	}

	public List<DemandResponseEvent> findToSentEventByVenUsername(String username, Long size) {
		if (size != null && size > 0) {
			Pageable limit = null;
			int i = (int) (long) size;
			limit = PageRequest.of(0, i, SORT_ASC_ACTIVEPERIOD);

			Page<DemandResponseEvent> findAll = demandResponseEventDao
					.findAll(DemandResponseEventSpecification.toSentByVenUsername(username), limit);
			return findAll.getContent();

		} else {
			return demandResponseEventDao.findAll(DemandResponseEventSpecification.toSentByVenUsername(username),
					SORT_ASC_ACTIVEPERIOD);
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

		if (start != null && end != null) {
			spec = spec.and(DemandResponseEventSpecification.hasActivePeriodEndNullOrAfter(start)
					.and(DemandResponseEventSpecification.hasActivePeriodStartBefore(end)));
		} else if (start != null) {
			spec = spec.and(DemandResponseEventSpecification.hasActivePeriodStartAfter(start));
		} else if (end != null) {
			spec = spec.and(DemandResponseEventSpecification.hasActivePeriodEndNullOrBefore(end));
		}

		return demandResponseEventDao.findAll(spec, PageRequest.of(page, size, SORT_ASC_ACTIVEPERIOD));
	}

	public Page<DemandResponseEvent> searchSendable(List<DemandResponseEventFilter> filters, Long start, Integer page,
			Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = DEFAULT_SEARCH_SIZE;
		}
		Specification<DemandResponseEvent> spec = Specification.where(DemandResponseEventSpecification.search(filters));

		if (start != null) {

			spec = spec.and(DemandResponseEventSpecification.hasActivePeriodEndNullOrAfter(start)
					.and(DemandResponseEventSpecification.hasActivePeriodNotificationStartBefore(start)));

//			spec = spec.and(DemandResponseEventSpecification.hasActivePeriodNotificationStartAfter(start)
//					.or(DemandResponseEventSpecification.hasActivePeriodStartAfter(start)));
		}

		return demandResponseEventDao.findAll(spec, PageRequest.of(page, size, SORT_ASC_ACTIVEPERIOD));
	}

}
