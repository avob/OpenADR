package com.avob.openadr.server.common.vtn.models.known;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;

public class KnownSignalSpecification {

private static final String FILTER_KNOWN_SIGNAL_ID = "knownSignalId";
	private static final String FILTER_SIGNAL_NAME = "signalName";
	private static final String FILTER_SIGNAL_TYPE = "signalType";

	private static final Sort DEFAULT_SORT = Sort.by(Direction.ASC, FILTER_KNOWN_SIGNAL_ID + "." + FILTER_SIGNAL_NAME,
			FILTER_KNOWN_SIGNAL_ID + "." + FILTER_SIGNAL_TYPE);
	public static Specification<KnownSignal> hasSignalName(DemandResponseEventSignalNameEnum signalName) {
		if (signalName == null) {
			return null;
		}
		return (event, cq, cb) -> cb.equal(event.get(FILTER_KNOWN_SIGNAL_ID).get(FILTER_SIGNAL_NAME), signalName);
	}

	public static Specification<KnownSignal> hasSignalType(DemandResponseEventSignalTypeEnum signalType) {
		if (signalType == null) {
			return null;
		}
		return (event, cq, cb) -> cb.equal(event.get(FILTER_KNOWN_SIGNAL_ID).get(FILTER_SIGNAL_TYPE), signalType);
	}

	public static Specification<KnownSignal> search(KnownSignalDto dto) {

		return Specification.where(KnownSignalSpecification.hasSignalName(dto.getSignalName()))
				.and(KnownSignalSpecification.hasSignalType(dto.getSignalType()));

	}

	public static Sort defaultSort() {
		return DEFAULT_SORT;
	}

}
