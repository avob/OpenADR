package com.avob.openadr.server.common.vtn.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenSearchDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDao;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceSpecification;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceType;

@Service
public class VenResourceService {

	@Resource
	private VenResourceDao venResourceDao;

	public VenResource save(VenResource entity) {
		return venResourceDao.save(entity);
	}

	public Iterable<VenResource> save(List<VenResource> entities) {
		return entities.stream().map(this::save).collect(Collectors.toList());
	}

	public Iterable<VenResource> findAll() {
		return venResourceDao.findAll();
	}

	public List<VenResource> findByVen(Ven ven) {
		return venResourceDao.findByVenId(ven.getId());
	}

	public void deleteByVen(Ven ven) {
		venResourceDao.deleteByVenId(ven.getId());
	}

	public VenResource findByVenAndName(Ven ven, String name) {
		return venResourceDao.findOneByVenAndName(ven, name);
	}

	public List<VenResource> findByVenIdAndName(List<String> venId, List<String> name) {
		return venResourceDao.findByVenIdAndName(venId, name);
	}

	public List<VenResource> findByNameIn(List<String> name) {
		return venResourceDao.findByNameIn(name);
	}

	public void delete(VenResource resource) {
		venResourceDao.delete(resource);
	}

	public long count() {
		return venResourceDao.count();
	}

	@Transactional
	public List<VenResource> search(VenSearchDto venSearchDto) {

		Specification<VenResource> search = VenResourceSpecification.search(venSearchDto.getFilters());
		Specification<VenResource> typeIn = VenResourceSpecification.typeIn(Arrays.asList(VenResourceType.VEN,
				VenResourceType.REPORT, VenResourceType.ENDDEVICEASSET, VenResourceType.RESOURCE));

		Specification<VenResource> and = Specification.where(search).and(typeIn);
		return venResourceDao.findAll(and);
	}

}
