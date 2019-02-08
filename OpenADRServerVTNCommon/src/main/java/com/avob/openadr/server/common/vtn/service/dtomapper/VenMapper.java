package com.avob.openadr.server.common.vtn.service.dtomapper;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;

@Service
public class VenMapper extends DozerConverter<Ven, String> {

    @Resource
    private VenService venService;

    public VenMapper() {
        super(Ven.class, String.class);
    }

    @Override
    public String convertTo(Ven source, String destination) {
        if (source == null) {
            return null;
        }
        return source.getUsername();
    }

    @Override
    public Ven convertFrom(String source, Ven destination) {
        if (source == null) {
            return null;
        }
        return venService.findOneByUsername(source);
    }

}
