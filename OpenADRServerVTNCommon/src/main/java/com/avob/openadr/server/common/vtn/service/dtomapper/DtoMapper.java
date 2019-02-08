package com.avob.openadr.server.common.vtn.service.dtomapper;

import static org.dozer.loader.api.FieldsMappingOptions.customConverter;
import static org.dozer.loader.api.FieldsMappingOptions.customConverterId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDto;
import com.google.common.collect.Lists;

@Service

public class DtoMapper {
    protected static final String MARKET_CONTEXT_CONVERTER_ID = "marketContextConverter";

    @Resource
    protected MarketContextMapper marketContextConverter;

    protected DozerBeanMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
        Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
        customConvertersWithId.put(MARKET_CONTEXT_CONVERTER_ID, marketContextConverter);
        mapper.setCustomConvertersWithId(customConvertersWithId);
        mapper.addMapping(demandResponseEventMappingConfiguration());
    }

    public <T> T map(Object src, Class<T> klass) {
        return mapper.map(src, klass);
    }

    public <T> List<T> mapList(Iterable<?> src, Class<T> klass) {
        List<T> list = Lists.newArrayList();
        for (Object obj : src) {
            list.add(mapper.map(obj, klass));
        }
        return list;
    }

    private BeanMappingBuilder demandResponseEventMappingConfiguration() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(DemandResponseEvent.class, DemandResponseEventDto.class).fields("marketContext",
                        "marketContext", customConverter(MarketContextMapper.class),
                        customConverterId(MARKET_CONTEXT_CONVERTER_ID));
            }
        };
    }

}
