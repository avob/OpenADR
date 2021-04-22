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

import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventDescriptorDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventDescriptor;
import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportDto;
import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalDto;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitDto;
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

	protected static final String KNOWN_UNIT_CONVERTER_ID = "knownUnitConverter";
	protected static final String KNOWN_SIGNAL_CONVERTER_ID = "knownSignalConverter";

	protected static final String KNOWN_REPORT_CONVERTER_ID = "knownReportConverter";

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

	@Resource
	private KnownUnitMapper knownUnitMapper;

	@Resource
	private KnownSignalMapper knownSignalMapper;

	@Resource
	private KnownReportMapper knownReportMapper;

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
		customConvertersWithId.put(KNOWN_UNIT_CONVERTER_ID, knownUnitMapper);
		customConvertersWithId.put(KNOWN_SIGNAL_CONVERTER_ID, knownSignalMapper);
		customConvertersWithId.put(KNOWN_REPORT_CONVERTER_ID, knownReportMapper);

		mapper.setCustomConvertersWithId(customConvertersWithId);
		mapper.addMapping(demandResponseEventMappingConfiguration());
		mapper.addMapping(venDemandResponseEventMappingConfiguration());
		mapper.addMapping(knownUnitMappingConfiguration());
		mapper.addMapping(knownSignalMappingConfiguration());
		mapper.addMapping(knownReportMappingConfiguration());
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

	private BeanMappingBuilder knownUnitMappingConfiguration() {

		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				mapping(KnownUnit.class, KnownUnitDto.class).fields(this_(), this_(),
						customConverter(KnownUnitMapper.class), customConverterId(KNOWN_UNIT_CONVERTER_ID));

			}
		};
	}

	private BeanMappingBuilder knownSignalMappingConfiguration() {

		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				mapping(KnownSignal.class, KnownSignalDto.class).fields(this_(), this_(),
						customConverter(KnownSignalMapper.class), customConverterId(KNOWN_SIGNAL_CONVERTER_ID));

			}
		};
	}

	private BeanMappingBuilder knownReportMappingConfiguration() {

		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				mapping(KnownReport.class, KnownReportDto.class).fields(this_(), this_(),
						customConverter(KnownReportMapper.class), customConverterId(KNOWN_REPORT_CONVERTER_ID));

			}
		};
	}

}
