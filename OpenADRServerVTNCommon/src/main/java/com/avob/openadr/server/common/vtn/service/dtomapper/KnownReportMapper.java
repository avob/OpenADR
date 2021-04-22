package com.avob.openadr.server.common.vtn.service.dtomapper;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportDto;
import com.avob.openadr.server.common.vtn.models.known.KnownReportId;

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

			destination = new KnownReportDto();
		}
		destination.setReportName(source.getKnownReportId().getReportName());
		destination.setReportType(source.getKnownReportId().getReportType());
		destination.setReadingType(source.getKnownReportId().getReadingType());
		destination.setPayloadBase(source.getKnownReportId().getPayloadBase());
		destination.setUnit(knownUnitMapper.convertTo(source.getKnownReportId().getUnit()));
		return destination;
	}

	@Override
	public KnownReport convertFrom(KnownReportDto source, KnownReport destination) {

		if (destination == null) {
			destination = new KnownReport();
			destination.setKnownReportId(new KnownReportId());
		}
		destination.getKnownReportId().setReportName(source.getReportName());
		destination.getKnownReportId().setReportType(source.getReportType());
		destination.getKnownReportId().setReadingType(source.getReadingType());
		destination.getKnownReportId().setPayloadBase(source.getPayloadBase());
		destination.getKnownReportId().setUnit(knownUnitMapper.convertFrom(source.getUnit()));
		return destination;
	}

}
