package com.avob.openadr.server.common.vtn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDao;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;

@Service
public class VenResourceService {

    @Resource
    private VenResourceDao venResourceDao;

    public VenResource prepare(Ven ven, VenResourceDto dto) {
        return new VenResource(dto.getName(), ven);
    }

    public VenResource save(VenResource entity) {
        return venResourceDao.save(entity);
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

}
