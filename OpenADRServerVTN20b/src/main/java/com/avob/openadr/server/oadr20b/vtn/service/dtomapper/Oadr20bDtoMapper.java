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

import com.avob.openadr.model.oadr20b.dto.ReportRequestDto;
import com.avob.openadr.model.oadr20b.dto.VenOptDto;
import com.avob.openadr.server.common.vtn.service.dtomapper.DtoMapper;
import com.avob.openadr.server.common.vtn.service.dtomapper.MarketContextMapper;
import com.avob.openadr.server.common.vtn.service.dtomapper.VenMapper;
import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOpt;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;

@Service
public class Oadr20bDtoMapper extends DtoMapper {

	protected static String VEN_MAPPER_ID = "venMapper";

	protected static String OTHER_REPORT_CAPABILITY_MAPPER_ID = "otherReportCapabilityMapper";

	protected static String OTHER_REPORT_CAPABILITY_DESCRIPTION_MAPPER_ID = "otherReportCapabilityDescriptionMapper";

	@Resource
	private VenMapper venMapper;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
		customConvertersWithId.putAll(this.mapper.getCustomConvertersWithId());
		customConvertersWithId.put(VEN_MAPPER_ID, venMapper);
		customConvertersWithId.put(OTHER_REPORT_CAPABILITY_MAPPER_ID, new OtherReportCapabilityMapper());
		customConvertersWithId.put(OTHER_REPORT_CAPABILITY_DESCRIPTION_MAPPER_ID,
				new OtherReportCapabilityDescriptionMapper());
		this.mapper.setCustomConvertersWithId(customConvertersWithId);
		this.mapper.addMapping(venOptMappingConfiguration());
		this.mapper.addMapping(reportRequestMappingconfiguration());

	}

	private BeanMappingBuilder venOptMappingConfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				TypeMappingBuilder fields = mapping(VenOpt.class, VenOptDto.class);
				fields.fields("ven", "venId", customConverter(VenMapper.class), customConverterId(VEN_MAPPER_ID));
				fields.fields("marketContext", "marketContextName", customConverter(MarketContextMapper.class),
						customConverterId(MARKET_CONTEXT_CONVERTER_ID));
			}
		};
	}

	private BeanMappingBuilder reportRequestMappingconfiguration() {
		return new BeanMappingBuilder() {
			@Override
			protected void configure() {
				TypeMappingBuilder fields = mapping(OtherReportRequest.class, ReportRequestDto.class);
				fields.fields("otherReportCapability", "reportSpecifierId",
						customConverter(OtherReportCapabilityMapper.class),
						customConverterId(OTHER_REPORT_CAPABILITY_MAPPER_ID));
				fields.fields("otherReportCapabilityDescription", "rid",
						customConverter(OtherReportCapabilityDescriptionMapper.class),
						customConverterId(OTHER_REPORT_CAPABILITY_DESCRIPTION_MAPPER_ID));
			}
		};
	}
}
