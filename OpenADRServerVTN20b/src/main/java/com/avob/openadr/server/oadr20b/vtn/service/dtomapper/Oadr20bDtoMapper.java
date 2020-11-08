package com.avob.openadr.server.oadr20b.vtn.service.dtomapper;

import static org.dozer.loader.api.FieldsMappingOptions.customConverter;
import static org.dozer.loader.api.FieldsMappingOptions.customConverterId;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.dozer.CustomConverter;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingBuilder;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.service.dtomapper.AbstractUserMapper;
import com.avob.openadr.server.common.vtn.service.dtomapper.DemandResponseEventMapper;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
import com.avob.openadr.server.common.vtn.service.dtomapper.MarketContextMapper;
import com.avob.openadr.server.common.vtn.service.dtomapper.VenMapper;
import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOpt;
import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOptDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifier;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDto;

@Service
public class Oadr20bDtoMapper extends DtoMapper {

	protected static String OTHER_REPORT_CAPABILITY_MAPPER_ID = "otherReportCapabilityMapper";

	protected static String OTHER_REPORT_CAPABILITY_DESCRIPTION_MAPPER_ID = "otherReportCapabilityDescriptionMapper";

	protected static String OTHER_REPORT_REQUEST_MAPPER_ID = "otherReportRequestMapper";

	@Resource
	private VenMapper venMapper;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
		customConvertersWithId.putAll(this.mapper.getCustomConvertersWithId());
		customConvertersWithId.put(OTHER_REPORT_CAPABILITY_MAPPER_ID, new OtherReportCapabilityMapper());
		customConvertersWithId.put(OTHER_REPORT_CAPABILITY_DESCRIPTION_MAPPER_ID,
				new OtherReportCapabilityDescriptionMapper());
		customConvertersWithId.put(OTHER_REPORT_REQUEST_MAPPER_ID, new OtherReportRequestMapper());

		this.mapper.setCustomConvertersWithId(customConvertersWithId);
		this.mapper.addMapping(venOptMappingConfiguration());
		this.mapper.addMapping(reportRequestMappingconfiguration());
		this.mapper.addMapping(venReportRequestCapabilityMappingConfiguration());
		this.mapper.addMapping(venReportRequestCapabilityDescriptionMappingConfiguration());
		this.mapper.addMapping(reportRequestSpecifierMappingconfiguration());

	}

	private BeanMappingBuilder venOptMappingConfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				TypeMappingBuilder fields = mapping(VenOpt.class, VenOptDto.class);
				fields.fields("ven", "venId", customConverter(VenMapper.class),
						customConverterId(Oadr20bDtoMapper.VEN_CONVERTER_ID));
				fields.fields("marketContext", "marketContext", customConverter(MarketContextMapper.class),
						customConverterId(MARKET_CONTEXT_CONVERTER_ID)).fields("event", "eventId",
								customConverter(DemandResponseEventMapper.class),
								customConverterId(DEMAND_RESPONSE_CONVERTER_ID));
			}
		};
	}

	private BeanMappingBuilder venReportRequestCapabilityMappingConfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				TypeMappingBuilder fields = mapping(OtherReportCapability.class, OtherReportCapabilityDto.class);
				fields.fields("source", "venId", customConverter(VenMapper.class),
						customConverterId(Oadr20bDtoMapper.VEN_CONVERTER_ID));

			}
		};
	}

	private BeanMappingBuilder venReportRequestCapabilityDescriptionMappingConfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				TypeMappingBuilder fields = mapping(OtherReportCapabilityDescription.class,
						OtherReportCapabilityDescriptionDto.class);
				fields.fields("otherReportCapability.source", "venId", customConverter(VenMapper.class),
						customConverterId(Oadr20bDtoMapper.VEN_CONVERTER_ID));
				fields.fields("otherReportCapability", "reportSpecifierId",
						customConverter(OtherReportCapabilityMapper.class),
						customConverterId(Oadr20bDtoMapper.OTHER_REPORT_CAPABILITY_MAPPER_ID));

			}
		};
	}

	private BeanMappingBuilder reportRequestMappingconfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				TypeMappingBuilder fields = mapping(OtherReportRequest.class, OtherReportRequestDto.class);
				fields.fields("otherReportCapability", "reportSpecifierId",
						customConverter(OtherReportCapabilityMapper.class),
						customConverterId(Oadr20bDtoMapper.OTHER_REPORT_CAPABILITY_MAPPER_ID));

				fields.fields("requestor", "requestorUsername", customConverter(AbstractUserMapper.class),
						customConverterId(Oadr20bDtoMapper.ABSTRACT_USER_CONVERTER_ID));
			}
		};
	}

	private BeanMappingBuilder reportRequestSpecifierMappingconfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				TypeMappingBuilder fields = mapping(OtherReportRequestSpecifier.class,
						OtherReportRequestSpecifierDto.class);

				fields.fields("request", "reportRequestId", customConverter(OtherReportRequestMapper.class),
						customConverterId(Oadr20bDtoMapper.OTHER_REPORT_REQUEST_MAPPER_ID));
				
//				fields.fields("request", "reportSpecifierId", customConverter(OtherReportRequestMapper.class),
//						customConverterId(Oadr20bDtoMapper.OTHER_REPORT_CAPABILITY_MAPPER_ID));

				fields.fields("otherReportCapabilityDescription", "rid",
						customConverter(OtherReportCapabilityDescriptionMapper.class),
						customConverterId(Oadr20bDtoMapper.OTHER_REPORT_CAPABILITY_DESCRIPTION_MAPPER_ID));

			}
		};
	}
}
