package com.avob.openadr.server.common.vtn.service.dtomapper;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalDto;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalId;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;

@Service
public class KnownSignalMapper extends DozerConverter<KnownSignal, KnownSignalDto> {

	@Resource
	private KnownUnitMapper knownUnitMapper;

	public KnownSignalMapper() {
		super(KnownSignal.class, KnownSignalDto.class);
	}

	@Override
	public KnownSignalDto convertTo(KnownSignal source, KnownSignalDto destination) {
		if (destination == null) {

			destination = new KnownSignalDto();
		}

		destination.setSignalName(source.getKnownSignalId().getSignalName());
		destination.setSignalType(source.getKnownSignalId().getSignalType());
		destination.setItemBase(knownUnitMapper.convertTo(new KnownUnit(source.getKnownSignalId().getItemBase())));

		return destination;
	}

	@Override
	public KnownSignal convertFrom(KnownSignalDto source, KnownSignal destination) {
		if (destination == null) {
			destination = new KnownSignal();
			destination.setKnownSignalId(new KnownSignalId());
		}

		destination.getKnownSignalId().setSignalName(source.getSignalName());
		destination.getKnownSignalId().setSignalType(source.getSignalType());
		destination.getKnownSignalId().setItemBase(knownUnitMapper.convertFrom(source.getItemBase()).getKnownUnitId());

		return destination;
	}

}
