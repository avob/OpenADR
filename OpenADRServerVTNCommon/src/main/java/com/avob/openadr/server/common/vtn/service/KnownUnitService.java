package com.avob.openadr.server.common.vtn.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDao;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDto;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitSpecification;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;

@Service
public class KnownUnitService {

	@Resource
	private KnownUnitDao knownUnitDao;

	@Resource
	private DtoMapper dtoMapper;

	public KnownUnit prepare(KnownUnitDto dto) {
		KnownUnit id = dtoMapper.map(dto, KnownUnit.class);
		return id;
	}

	public KnownUnit save(KnownUnit entity) {
		return knownUnitDao.save(entity);
	}

	public Iterable<KnownUnit> save(Collection<KnownUnit> entities) {
		return knownUnitDao.saveAll(entities);
	}

	public Iterable<KnownUnit> search(KnownUnitDto dto) {
		return knownUnitDao.findAll(KnownUnitSpecification.search(dto), KnownUnitSpecification.defaultSort());
	}

	public List<String> findItemDescription() {
		return knownUnitDao.findItemDescription();
	}

	public List<String> findItemUnits(String description) {
		if (description == null) {
			return knownUnitDao.findItemUnits();
		} else {
			return knownUnitDao.findItemUnitsByItemDescription(description);
		}

	}

}
