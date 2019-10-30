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

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDescriptor;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventDescriptorDto;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDto;
import com.google.common.collect.Lists;

@Service

public class DtoMapper {
	protected static final String MARKET_CONTEXT_CONVERTER_ID = "marketContextConverter";
	protected static final String VEN_CONVERTER_ID = "venConverter";
	protected static final String DEMAND_RESPONSE_CONVERTER_ID = "demandResponseEventConverter";
	protected static final String VEN_RESOURCE_CONVERTER_ID = "venResourceConverter";

	protected static final String ABSTRACT_USER_CONVERTER_ID = "abstractUserConverter";

	@Resource
	protected MarketContextMapper marketContextConverter;

	@Resource
	private VenMapper venConverter;

	@Resource
	private DemandResponseEventMapper demandResponseEventConverter;

	@Resource
	private VenResourceMapper venResourceMapper;

	@Resource
	private AbstractUserMapper abstractUserMapper;

	protected DozerBeanMapper mapper;

	@PostConstruct
	public void init() {
		mapper = new DozerBeanMapper();
		Map<String, CustomConverter> customConvertersWithId = new HashMap<>();
		customConvertersWithId.put(MARKET_CONTEXT_CONVERTER_ID, marketContextConverter);
		customConvertersWithId.put(VEN_CONVERTER_ID, venConverter);
		customConvertersWithId.put(DEMAND_RESPONSE_CONVERTER_ID, demandResponseEventConverter);
		customConvertersWithId.put(VEN_RESOURCE_CONVERTER_ID, venResourceMapper);
		customConvertersWithId.put(ABSTRACT_USER_CONVERTER_ID, abstractUserMapper);
		mapper.setCustomConvertersWithId(customConvertersWithId);
		mapper.addMapping(demandResponseEventMappingConfiguration());
		mapper.addMapping(venDemandResponseEventMappingConfiguration());
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
				mapping(DemandResponseEventDescriptor.class, DemandResponseEventDescriptorDto.class).fields(
						"marketContext", "marketContext", customConverter(MarketContextMapper.class),
						customConverterId(MARKET_CONTEXT_CONVERTER_ID));

			}
		};
	}

	private BeanMappingBuilder venDemandResponseEventMappingConfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				mapping(VenDemandResponseEvent.class, VenDemandResponseEventDto.class)
						.fields("ven", "venId", customConverter(VenMapper.class), customConverterId(VEN_CONVERTER_ID))
						.fields("event", "eventId", customConverter(DemandResponseEventMapper.class),
								customConverterId(DEMAND_RESPONSE_CONVERTER_ID));

			}
		};
	}

}
