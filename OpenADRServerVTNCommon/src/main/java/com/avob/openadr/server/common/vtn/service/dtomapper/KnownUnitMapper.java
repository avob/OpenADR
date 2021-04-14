package com.avob.openadr.server.common.vtn.service.dtomapper;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDto;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitId;

@Service
public class KnownUnitMapper extends DozerConverter<KnownUnit, KnownUnitDto> {

	public KnownUnitMapper() {
		super(KnownUnit.class, KnownUnitDto.class);
	}

	@Override
	public KnownUnitDto convertTo(KnownUnit source, KnownUnitDto destination) {
		if (destination == null) {
			destination = new KnownUnitDto();
		}
		destination.setAttributes(source.getAttributes());
		destination.setItemDescription(source.getKnownUnitId().getItemDescription());
		destination.setItemUnits(source.getKnownUnitId().getItemUnits());
		destination.setXmlType(source.getKnownUnitId().getXmlType());
		return destination;
	}

	@Override
	public KnownUnit convertFrom(KnownUnitDto source, KnownUnit destination) {
		if (destination == null) {
			destination = new KnownUnit();
			destination.setKnownUnitId(new KnownUnitId());
		}
		destination.getKnownUnitId().setItemDescription(source.getItemDescription());
		destination.getKnownUnitId().setItemUnits(source.getItemUnits());
		destination.getKnownUnitId().setXmlType(source.getXmlType());
		return destination;
	}

}
