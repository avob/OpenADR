package com.avob.openadr.server.common.vtn.service;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportDao;
import com.avob.openadr.server.common.vtn.models.known.KnownReportDto;
import com.avob.openadr.server.common.vtn.models.known.KnownReportSpecification;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@Service
public class KnownReportService {

	@Resource
	private KnownReportDao knownReportDao;

	@Resource
	private DtoMapper dtoMapper;

	public KnownReport prepare(KnownReportDto dto) {
		KnownReport id = dtoMapper.map(dto, KnownReport.class);
		return id;
	}

	public KnownReport save(KnownReport entity) {
		return knownReportDao.save(entity);
	}

	public Iterable<KnownReport> save(Collection<KnownReport> entities) {
		return knownReportDao.saveAll(entities);
	}

	public Iterable<KnownReport> search(KnownReportDto dto) {

		return knownReportDao.findAll(KnownReportSpecification.search(dto), KnownReportSpecification.defaultSort());
	}

}
