package com.avob.openadr.server.common.vtn.service.dtomapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportDto;
import com.avob.openadr.server.common.vtn.models.known.KnownReportId;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDto;

@Service
public class KnownReportMapper extends DozerConverter<KnownReport, KnownReportDto> {

	@Resource
	private KnownUnitMapper knownUnitMapper;

	public KnownReportMapper() {
		super(KnownReport.class, KnownReportDto.class);
	}

	@Override
	public KnownReportDto convertTo(KnownReport source, KnownReportDto destination) {

		
		if (destination == null) {
			List<KnownUnitDto> units = source.getUnits().stream().map(knownUnitMapper::convertTo)
					.collect(Collectors.toList());
			destination = new KnownReportDto();
			destination.setUnits(units);
		}
		destination.setReportName(source.getKnownReportId().getReportName());
		destination.setReportType(source.getKnownReportId().getReportType());
		destination.setReadingType(source.getReadingType());
		destination.setPayloadBase(source.getPayloadBase());
		
		return destination;
	}

	@Override
	public KnownReport convertFrom(KnownReportDto source, KnownReport destination) {
		
		if (destination == null) {
			List<KnownUnit> units = source.getUnits().stream().map(knownUnitMapper::convertFrom)
					.collect(Collectors.toList());
			destination = new KnownReport();
			destination.setKnownReportId(new KnownReportId());
			destination.setUnits(units);
		}
		destination.getKnownReportId().setReportName(source.getReportName());
		destination.getKnownReportId().setReportType(source.getReportType());
		destination.setReadingType(source.getReadingType());
		destination.setPayloadBase(source.getPayloadBase());
		
		return destination;
	}

}
