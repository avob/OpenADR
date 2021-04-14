package com.avob.openadr.server.common.vtn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalDao;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalDto;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalSpecification;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@Service
public class KnownSignalService {

	@Resource
	private KnownSignalDao knownSignalDao;

	@Resource
	private DtoMapper dtoMapper;

	public KnownSignal prepare(KnownSignalDto dto) {
		KnownSignal id = dtoMapper.map(dto, KnownSignal.class);
		return id;
	}

	public KnownSignal save(KnownSignal entity) {
		return knownSignalDao.save(entity);
	}
	
	public Iterable<KnownSignal> save(List<KnownSignal> entities) {
		return knownSignalDao.saveAll(entities);
	}

	public Iterable<KnownSignal> search(KnownSignalDto dto) {

		return knownSignalDao.findAll(KnownSignalSpecification.search(dto), KnownSignalSpecification.defaultSort());
	}

}
