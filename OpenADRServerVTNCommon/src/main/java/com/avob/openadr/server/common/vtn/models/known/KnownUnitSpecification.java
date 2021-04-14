package com.avob.openadr.server.common.vtn.models.known;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class KnownUnitSpecification {

	private static final String FILTER_KNOWN_UNIT_ID = "knownUnitId";
	private static final String FILTER_ITEM_DESCRIPTION = "itemDescription";
	private static final String FILTER_ITEM_UNITS = "itemUnits";
	private static final String FILTER_XMLTYPE = "xmlType";

	private static final Sort DEFAULT_SORT = Sort.by(Direction.ASC,
			FILTER_KNOWN_UNIT_ID + "." + FILTER_ITEM_DESCRIPTION, FILTER_KNOWN_UNIT_ID + "." + FILTER_ITEM_UNITS,
			FILTER_KNOWN_UNIT_ID + "." + FILTER_XMLTYPE);

	public static Specification<KnownUnit> hasItemDescriptor(String itemDescriptor) {
		if (itemDescriptor == null) {
			return null;
		}
		return (event, cq, cb) -> cb.equal(event.get(FILTER_KNOWN_UNIT_ID).get(FILTER_ITEM_DESCRIPTION),
				itemDescriptor);
	}

	public static Specification<KnownUnit> hasItemUnits(String itemUnits) {
		if (itemUnits == null) {
			return null;
		}
		return (event, cq, cb) -> cb.equal(event.get(FILTER_KNOWN_UNIT_ID).get(FILTER_ITEM_UNITS), itemUnits);
	}

	public static Specification<KnownUnit> hasXmlType(String xmlType) {
		if (xmlType == null) {
			return null;
		}
		return (event, cq, cb) -> cb.equal(event.get(FILTER_KNOWN_UNIT_ID).get(FILTER_XMLTYPE), xmlType);
	}

	public static Specification<KnownUnit> search(KnownUnitDto dto) {
		return Specification.where(KnownUnitSpecification.hasItemDescriptor(dto.getItemDescription()))
				.and(KnownUnitSpecification.hasItemUnits(dto.getItemUnits()))
				.and(KnownUnitSpecification.hasXmlType(dto.getXmlType()));

	}

	public static Sort defaultSort() {
		return DEFAULT_SORT;
	}

}
