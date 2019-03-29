package com.avob.openadr.server.common.vtn.service.dtomapper;

import javax.annotation.Resource;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;

@Service
public class DemandResponseEventMapper extends DozerConverter<DemandResponseEvent, String> {

	@Resource
	private DemandResponseEventService demandResponseEventService;

	public DemandResponseEventMapper() {
		super(DemandResponseEvent.class, String.class);
	}

	@Override
	public String convertTo(DemandResponseEvent source, String destination) {
		if (source == null || source.getDescriptor() == null) {
			return null;
		}
		return String.valueOf(source.getId());
	}

	@Override
	public DemandResponseEvent convertFrom(String source, DemandResponseEvent destination) {
		return demandResponseEventService.findById(Long.valueOf(source)).get();
	}

}
