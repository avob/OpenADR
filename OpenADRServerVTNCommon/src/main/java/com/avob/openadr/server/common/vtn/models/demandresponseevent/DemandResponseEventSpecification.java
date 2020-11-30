package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import java.util.List;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;

import org.springframework.data.jpa.domain.Specification;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;

public class DemandResponseEventSpecification {

	private static final String FILTER_NOT_SENDABLE = "NOT_SENDABLE";
	private static final String FILTER_SENDABLE = "SENDABLE";
	private static final String FILTER_EVENT_NOT_PUBLISHED = "NOT_PUBLISHED";
	private static final String FILTER_EVENT_PUBLISHED = "PUBLISHED";
	private static final String FIELD_END = "end";
	private static final String FIELD_VEN_DEMAND_RESPONSE_EVENT = "venDemandResponseEvent";
	private static final String FIELD_START = "start";
	private static final String FIELD_ACTIVE_PERIOD = "activePeriod";
	private static final String FIELD_USERNAME = "username";
	private static final String FIELD_VEN = "ven";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_MARKET_CONTEXT = "marketContext";
	private static final String FIELD_STATE = "state";
	private static final String FIELD_DESCRIPTOR = "descriptor";
	private static final String FIELD_PUBLISHED = "published";

	private DemandResponseEventSpecification() {
	}

	public static Specification<DemandResponseEvent> isPublished() {
		return (event, cq, cb) -> cb.isTrue(event.get(FIELD_PUBLISHED));
	}

	public static Specification<DemandResponseEvent> isPublished(Boolean published) {
		return (event, cq, cb) -> cb.equal(event.get(FIELD_PUBLISHED), published);
	}

	public static Specification<DemandResponseEvent> hasDescriptorState(DemandResponseEventStateEnum state) {
		return (event, cq, cb) -> cb.equal(event.get(FIELD_DESCRIPTOR).get(FIELD_STATE), state);
	}

	public static Specification<DemandResponseEvent> hasDescriptorMarketContext(String marketContextName) {
		return (event, cq, cb) -> cb.equal(event.get(FIELD_DESCRIPTOR).get(FIELD_MARKET_CONTEXT).get(FIELD_NAME),
				marketContextName);
	}

	public static Specification<DemandResponseEvent> hasVenUsername(String username) {
		return (event, cq, cb) -> {
			ListJoin<DemandResponseEvent, VenDemandResponseEvent> joinList = event
					.joinList(FIELD_VEN_DEMAND_RESPONSE_EVENT, JoinType.INNER);
			return cb.equal(joinList.get(FIELD_VEN).get(FIELD_USERNAME), username);
		};
	}

	public static Specification<DemandResponseEvent> hasActivePeriodStartAfter(Long timestamp) {
		return (event, cq, cb) -> cb.gt(event.get(FIELD_ACTIVE_PERIOD).get(FIELD_START), timestamp);
	}

	public static Specification<DemandResponseEvent> hasActivePeriodStartBefore(Long timestamp) {
		return (event, cq, cb) -> cb.le(event.get(FIELD_ACTIVE_PERIOD).get(FIELD_START), timestamp);
	}

	public static Specification<DemandResponseEvent> hasActivePeriodEndNullOrBefore(Long timestamp) {
		return (event, cq, cb) -> {
			return cb.or(cb.isNull(event.get(FIELD_ACTIVE_PERIOD).get(FIELD_END)),
					cb.lt(event.get(FIELD_ACTIVE_PERIOD).get(FIELD_END), timestamp));
		};
	}

	public static Specification<DemandResponseEvent> hasActivePeriodNotificationStartBefore(Long timestamp) {
		return (event, cq, cb) -> cb.le(event.get(FIELD_ACTIVE_PERIOD).get("startNotification"), timestamp);
	}
	
	public static Specification<DemandResponseEvent> hasActivePeriodNotificationStartAfter(Long timestamp) {
		return (event, cq, cb) -> cb.gt(event.get(FIELD_ACTIVE_PERIOD).get("startNotification"), timestamp);
	}

	public static Specification<DemandResponseEvent> hasActivePeriodEndNullOrAfter(Long timestamp) {
		return (event, cq, cb) -> {
			return cb.or(cb.isNull(event.get(FIELD_ACTIVE_PERIOD).get(FIELD_END)),
					cb.gt(event.get(FIELD_ACTIVE_PERIOD).get(FIELD_END), timestamp));
		};
	}

	public static Specification<DemandResponseEvent> isSendable(boolean sendable) {
		return (event, cq, cb) -> {
			long now = System.currentTimeMillis();
			Specification<DemandResponseEvent> and = DemandResponseEventSpecification.isPublished()
					.and(DemandResponseEventSpecification.hasActivePeriodEndNullOrAfter(now))
					.and(DemandResponseEventSpecification.hasActivePeriodNotificationStartBefore(now));

			if (!sendable) {
				and = Specification.not(and);
			}

			return and.toPredicate(event, cq, cb);

		};
	}

	public static Specification<DemandResponseEvent> toSentByVenUsername(String venUsername) {
		return (event, cq, cb) -> {
			long now = System.currentTimeMillis();
			Specification<DemandResponseEvent> and = DemandResponseEventSpecification.isPublished()
					.and(DemandResponseEventSpecification.hasVenUsername(venUsername))
					.and(DemandResponseEventSpecification.hasActivePeriodEndNullOrAfter(now))
					.and(DemandResponseEventSpecification.hasActivePeriodNotificationStartBefore(now))
					;
			cq.distinct(true);
			return and.toPredicate(event, cq, cb);

		};
	}

	public static Specification<DemandResponseEvent> search(List<DemandResponseEventFilter> filters) {
		Specification<DemandResponseEvent> marketContextPredicates = null;
		Specification<DemandResponseEvent> venPredicates = null;
		Specification<DemandResponseEvent> statePredicates = null;
		Specification<DemandResponseEvent> isPublishedPredicates = null;
		Specification<DemandResponseEvent> isSendablePredicates = null;
		for (DemandResponseEventFilter demandResponseEventFilter : filters) {
			switch (demandResponseEventFilter.getType()) {

			case MARKET_CONTEXT:
				if (marketContextPredicates != null) {
					marketContextPredicates = marketContextPredicates.or(DemandResponseEventSpecification
							.hasDescriptorMarketContext(demandResponseEventFilter.getValue()));
				} else {
					marketContextPredicates = DemandResponseEventSpecification
							.hasDescriptorMarketContext(demandResponseEventFilter.getValue());
				}
				break;
			case VEN:
				if (venPredicates != null) {
					venPredicates = venPredicates
							.or(DemandResponseEventSpecification.hasVenUsername(demandResponseEventFilter.getValue()));
				} else {
					venPredicates = DemandResponseEventSpecification
							.hasVenUsername(demandResponseEventFilter.getValue());
				}
				break;
			case EVENT_STATE:
				DemandResponseEventStateEnum valueOf = DemandResponseEventStateEnum
						.valueOf(demandResponseEventFilter.getValue());
				if (statePredicates != null) {
					statePredicates = statePredicates.or(DemandResponseEventSpecification.hasDescriptorState(valueOf));
				} else {
					statePredicates = DemandResponseEventSpecification.hasDescriptorState(valueOf);
				}
				break;
			case EVENT_PUBLISHED:
				if (FILTER_EVENT_PUBLISHED.equalsIgnoreCase(demandResponseEventFilter.getValue())) {
					if (isPublishedPredicates != null) {
						isPublishedPredicates = isPublishedPredicates
								.or(DemandResponseEventSpecification.isPublished(true));
					} else {
						isPublishedPredicates = DemandResponseEventSpecification.isPublished(true);
					}
				} else if (FILTER_EVENT_NOT_PUBLISHED.equalsIgnoreCase(demandResponseEventFilter.getValue())) {
					if (isPublishedPredicates != null) {
						isPublishedPredicates = isPublishedPredicates
								.or(DemandResponseEventSpecification.isPublished(false));
					} else {
						isPublishedPredicates = DemandResponseEventSpecification.isPublished(false);
					}
				}

				break;

			case EVENT_SENDABLE:
				if (FILTER_SENDABLE.equalsIgnoreCase(demandResponseEventFilter.getValue())) {
					if (isSendablePredicates != null) {
						isSendablePredicates = isSendablePredicates
								.or(DemandResponseEventSpecification.isSendable(true));
					} else {
						isSendablePredicates = DemandResponseEventSpecification.isSendable(true);
					}
				} else if (FILTER_NOT_SENDABLE.equalsIgnoreCase(demandResponseEventFilter.getValue())) {
					if (isSendablePredicates != null) {
						isSendablePredicates = isSendablePredicates
								.or(DemandResponseEventSpecification.isSendable(false));
					} else {
						isSendablePredicates = DemandResponseEventSpecification.isSendable(false);
					}
				}

				break;

			default:
				break;

			}
		}

		final Specification<DemandResponseEvent> finalRes = Specification.where(marketContextPredicates)
				.and(venPredicates).and(statePredicates).and(isPublishedPredicates).and(isSendablePredicates);

		return (event, cq, cb) -> {
			cq.distinct(true);
			return finalRes.toPredicate(event, cq, cb);
		};
	}

}
