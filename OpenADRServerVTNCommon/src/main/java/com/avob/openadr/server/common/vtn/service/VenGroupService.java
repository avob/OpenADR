package com.avob.openadr.server.common.vtn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDao;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;

@Service
public class VenGroupService {

    @Resource
    private VenGroupDao venGroupDao;

    public VenGroup prepare(VenGroupDto dto) {
        return new VenGroup(dto.getName());
    }

    public VenGroup save(VenGroup entity) {
        return venGroupDao.save(entity);
    }

    public Iterable<VenGroup> findAll() {
        return venGroupDao.findAll();
    }

    public VenGroup findByName(String name) {
        return venGroupDao.findOneByName(name);
    }

    public List<VenGroup> findByName(List<String> name) {
        return venGroupDao.findByNameIn(name);
    }

    public void delete(VenGroup entity) {
        venGroupDao.delete(entity);
    }

    public long count() {
        return venGroupDao.count();
    }
}
