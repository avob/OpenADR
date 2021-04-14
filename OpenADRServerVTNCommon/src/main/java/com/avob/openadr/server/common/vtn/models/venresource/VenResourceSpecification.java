package com.avob.openadr.server.common.vtn.models.venresource;

import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;

import org.springframework.data.jpa.domain.Specification;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilter;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;

public class VenResourceSpecification {

	public static Specification<VenResource> typeIn(List<VenResourceType> types) {
		return (event, cq, cb) -> event.get("type").in(types);
	}

	public static Specification<VenResource> hasMarketContext(String marketContextName) {
		return (ven, cq, cb) -> {
			Join<Ven, VenGroup> join = ven.join("ven").join("venMarketContexts", JoinType.INNER);
			return cb.equal(join.get("name"), marketContextName);
		};
	}

	public static Specification<VenResource> hasGroup(String groupName) {
		return (ven, cq, cb) -> {
			Join<Ven, VenGroup> join = ven.join("ven").join("venGroups", JoinType.INNER);
			return cb.equal(join.get("name"), groupName);
		};
	}

	public static Specification<VenResource> isRegistered() {
		return (ven, cq, cb) -> {
			Join<Ven, VenGroup> join = ven.join("ven", JoinType.INNER);
			return cb.isNotNull(join.get("registrationId"));
		};
	}

	public static Specification<VenResource> isRegistered(Boolean reg) {
		if (reg) {
			return VenResourceSpecification.isRegistered();
		} else {
			return VenResourceSpecification.isNotRegistered();
		}
	}

	public static Specification<VenResource> isNotRegistered() {
		return (ven, cq, cb) -> Specification.not(VenResourceSpecification.isRegistered()).toPredicate(ven, cq, cb);
	}

	public static Specification<VenResource> hasEventId(String eventId) {
		return (ven, cq, cb) -> {
			ListJoin<Ven, VenDemandResponseEvent> joinList = ven.join("ven").joinList("venDemandResponseEvent", JoinType.INNER);
			return cb.equal(joinList.get("event").get("id"), eventId);
		};
	}

	public static Specification<VenResource> hasVenIdEquals(String str) {
		return (event, cq, cb) -> cb.equal(event.join("ven").get("username"), str);
	}

	public static Specification<VenResource> hasVenIdContains(String str) {
		return (event, cq, cb) -> cb.like(event.join("ven").get("username"), "%" + str + "%");
	}

	public static Specification<VenResource> hasVenCommonNameContains(String str) {
		return (event, cq, cb) -> cb.like(event.join("ven").get("commonName"), "%" + str + "%");
	}

	public static Specification<VenResource> hasVenOadrNameContains(String str) {
		return (event, cq, cb) -> cb.like(event.join("ven").get("oadrName"), "%" + str + "%");
	}

	public static Specification<VenResource> search(List<VenFilter> filters) {
		Specification<VenResource> eventIdPredicates = null;
		Specification<VenResource> groupPredicates = null;
		Specification<VenResource> marketContextPredicates = null;
		Specification<VenResource> registeredPredicates = null;
		Specification<VenResource> venIdPredicates = null;

		for (VenFilter venFilter : filters) {
			switch (venFilter.getType()) {

			case EVENT:
				if (eventIdPredicates != null) {
					eventIdPredicates = eventIdPredicates.or(VenResourceSpecification.hasEventId(venFilter.getValue()));
				} else {
					eventIdPredicates = VenResourceSpecification.hasEventId(venFilter.getValue());
				}
				break;
			case GROUP:
				if (groupPredicates != null) {
					groupPredicates = groupPredicates.or(VenResourceSpecification.hasGroup(venFilter.getValue()));
				} else {
					groupPredicates = VenResourceSpecification.hasGroup(venFilter.getValue());
				}
				break;
			case IS_REGISTERED:
				boolean bool = Boolean.parseBoolean(venFilter.getValue());

				if (registeredPredicates != null) {
					registeredPredicates = registeredPredicates.or(VenResourceSpecification.isRegistered(bool));
				} else {
					registeredPredicates = VenResourceSpecification.isRegistered(bool);
				}

				break;
			case MARKET_CONTEXT:
				if (marketContextPredicates != null) {
					marketContextPredicates = marketContextPredicates
							.or(VenResourceSpecification.hasMarketContext(venFilter.getValue()));
				} else {
					marketContextPredicates = VenResourceSpecification.hasMarketContext(venFilter.getValue());
				}
				break;
			case VEN:

				if (venIdPredicates != null) {
					venIdPredicates = venIdPredicates
							.or(VenResourceSpecification.hasVenIdContains(venFilter.getValue()))
							.or(VenResourceSpecification.hasVenCommonNameContains(venFilter.getValue()))
							.or(VenResourceSpecification.hasVenOadrNameContains(venFilter.getValue()));
				} else {
					venIdPredicates = VenResourceSpecification.hasVenIdContains(venFilter.getValue())
							.or(VenResourceSpecification.hasVenCommonNameContains(venFilter.getValue()))
							.or(VenResourceSpecification.hasVenOadrNameContains(venFilter.getValue()));
				}
				break;
			default:
				break;

			}
		}
		final Specification<VenResource> finalRes = Specification.where(eventIdPredicates).and(marketContextPredicates)
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
