package com.avob.openadr.server.common.vtn.models.ven;

import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;

import org.springframework.data.jpa.domain.Specification;

import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilter;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;

public class VenSpecification {

	static public Specification<Ven> hasMarketContext(String marketContextName) {
		return (ven, cq, cb) -> {
			Join<Ven, VenGroup> join = ven.join("venMarketContexts", JoinType.INNER);
			return cb.equal(join.get("name"), marketContextName);
		};
	}

	static public Specification<Ven> hasGroup(String groupName) {
		return (ven, cq, cb) -> {
			Join<Ven, VenGroup> join = ven.join("venGroups", JoinType.INNER);
			return cb.equal(join.get("name"), groupName);
		};
	}

	static public Specification<Ven> isRegistered() {
		return (ven, cq, cb) -> cb.isNotNull(ven.get("registrationId"));
	}

	static public Specification<Ven> isRegistered(Boolean reg) {
		if (reg) {
			return VenSpecification.isRegistered();
		} else {
			return VenSpecification.isNotRegistered();
		}
	}

	static public Specification<Ven> isNotRegistered() {
		return (ven, cq, cb) -> Specification.not(VenSpecification.isRegistered()).toPredicate(ven, cq, cb);
	}

	static public Specification<Ven> hasEventId(String eventId) {
		return (ven, cq, cb) -> {
			ListJoin<Ven, VenDemandResponseEvent> joinList = ven.joinList("venDemandResponseEvent", JoinType.INNER);
			return cb.equal(joinList.get("event").get("descriptor").get("eventId"), eventId);
		};
	}

	static public Specification<Ven> hasVenIdContains(String str) {
		return (event, cq, cb) -> cb.like(event.get("username"), "%" + str + "%");
	}

	static public Specification<Ven> hasVenCommonNameContains(String str) {
		return (event, cq, cb) -> cb.like(event.get("commonName"), "%" + str + "%");
	}

	static public Specification<Ven> hasVenOadrNameContains(String str) {
		return (event, cq, cb) -> cb.like(event.get("oadrName"), "%" + str + "%");
	}

	static public Specification<Ven> search(List<VenFilter> filters) {
		Specification<Ven> eventIdPredicates = null;
		Specification<Ven> groupPredicates = null;
		Specification<Ven> marketContextPredicates = null;
		Specification<Ven> registeredPredicates = null;
		Specification<Ven> venIdPredicates = null;

		for (VenFilter venFilter : filters) {
			switch (venFilter.getType()) {

			case EVENT:
				if (eventIdPredicates != null) {
					eventIdPredicates = eventIdPredicates.or(VenSpecification.hasEventId(venFilter.getValue()));
				} else {
					eventIdPredicates = VenSpecification.hasEventId(venFilter.getValue());
				}
				break;
			case GROUP:
				if (groupPredicates != null) {
					groupPredicates = groupPredicates.or(VenSpecification.hasGroup(venFilter.getValue()));
				} else {
					groupPredicates = VenSpecification.hasGroup(venFilter.getValue());
				}
				break;
			case IS_REGISTERED:
				boolean bool = Boolean.parseBoolean(venFilter.getValue());

				if (registeredPredicates != null) {
					registeredPredicates = registeredPredicates.or(VenSpecification.isRegistered(bool));
				} else {
					registeredPredicates = VenSpecification.isRegistered(bool);
				}

				break;
			case MARKET_CONTEXT:
				if (marketContextPredicates != null) {
					marketContextPredicates = marketContextPredicates
							.or(VenSpecification.hasMarketContext(venFilter.getValue()));
				} else {
					marketContextPredicates = VenSpecification.hasMarketContext(venFilter.getValue());
				}
				break;
			case VEN_ID:

				if (venIdPredicates != null) {
					venIdPredicates = venIdPredicates.or(VenSpecification.hasVenIdContains(venFilter.getValue()))
							.or(VenSpecification.hasVenCommonNameContains(venFilter.getValue()))
							.or(VenSpecification.hasVenOadrNameContains(venFilter.getValue()));
				} else {
					venIdPredicates = VenSpecification.hasVenIdContains(venFilter.getValue())
							.or(VenSpecification.hasVenCommonNameContains(venFilter.getValue()))
							.or(VenSpecification.hasVenOadrNameContains(venFilter.getValue()));
				}
				break;
			default:
				break;

			}
		}
		final Specification<Ven> finalRes = Specification.where(eventIdPredicates).and(marketContextPredicates)
				.and(groupPredicates).and(registeredPredicates).and(venIdPredicates);

		return (ven, cq, cb) -> {
			if (finalRes != null) {
				cq.distinct(true);
				return finalRes.toPredicate(ven, cq, cb);
			}
			return null;

		};
	}

}
