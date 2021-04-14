package com.avob.openadr.server.common.vtn.service.dtomapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalDto;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalId;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDto;

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
		List<KnownUnitDto> collect = source.getUnits().stream().map(knownUnitMapper::convertTo)
				.collect(Collectors.toList());

		destination.setUnits(collect);
		destination.setSignalName(source.getKnownSignalId().getSignalName());
		destination.setSignalType(source.getKnownSignalId().getSignalType());
		return destination;
	}

	@Override
	public KnownSignal convertFrom(KnownSignalDto source, KnownSignal destination) {
		if (destination == null) {
			destination = new KnownSignal();
			destination.setKnownSignalId(new KnownSignalId());
		}
		List<KnownUnit> collect = source.getUnits().stream().map(knownUnitMapper::convertFrom)
				.collect(Collectors.toList());
		destination.setUnits(collect);
		destination.getKnownSignalId().setSignalName(source.getSignalName());
		destination.getKnownSignalId().setSignalType(source.getSignalType());

		return destination;
	}

}
