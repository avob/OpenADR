package com.avob.openadr.server.common.vtn.service.dtomapper;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDao;

@Service
public class VenResourceMapper extends DozerConverter<VenResource, Long> {

	@Resource
	private VenResourceDao venResourceDao;

	public VenResourceMapper() {
		super(VenResource.class, Long.class);
	}

	@Override
	public Long convertTo(VenResource source, Long destination) {
		return source.getId();
	}

	@Override
	public VenResource convertFrom(Long source, VenResource destination) {
		return venResourceDao.findById(source).get();
	}

}
