package com.avob.openadr.server.common.vtn.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;

@Service
public class VenMarketContextService {

    @Resource
    private VenMarketContextDao venMarketcontextDao;

    public VenMarketContext prepare(VenMarketContextDto dto) {
    	VenMarketContext context =  new VenMarketContext(dto.getName(), dto.getDescription()); 
        context.setColor(dto.getColor());
    	return context;
    }

    public VenMarketContext save(VenMarketContext entity) {
        return venMarketcontextDao.save(entity);
    }

    public Iterable<VenMarketContext> findAll() {
        return venMarketcontextDao.findAll();
    }

    public VenMarketContext findOneByName(String name) {
        return venMarketcontextDao.findOneByName(name);
    }
    
    public Optional<VenMarketContext> findById(Long id) {
        return venMarketcontextDao.findById(id);
    }
    
    public List<VenMarketContext> findByNameIn(List<String> name) {
    	return venMarketcontextDao.findByNameIn(name);
    }

    public void delete(VenMarketContext venMarketContext) {
        venMarketcontextDao.delete(venMarketContext);
    }

    public long count() {
        return venMarketcontextDao.count();
    }

}
