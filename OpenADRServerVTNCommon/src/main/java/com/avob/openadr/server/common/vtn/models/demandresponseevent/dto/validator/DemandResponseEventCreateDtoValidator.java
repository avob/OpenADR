package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.validator;

import javax.annotation.Resource;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;

@Component
public class DemandResponseEventCreateDtoValidator extends DemandResponseEventDtoValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemandResponseEventCreateDto.class);

	@Autowired
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenMarketContextService venMarketContextService;

	public DemandResponseEventCreateDtoValidator() throws DatatypeConfigurationException {
		super();
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return DemandResponseEventCreateDto.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		DemandResponseEventCreateDto dto = (DemandResponseEventCreateDto) arg0;

		validateDescriptor(dto, errors);
		validateActivePeriod(dto, errors);
		validateContent(dto, errors);

		if (errors.hasErrors()) {
			LOGGER.warn(errors.getAllErrors().toString());
			return;
		}

		DemandResponseEvent event = demandResponseEventService.findByEventId(dto.getDescriptor().getEventId());
		if (event != null) {
			errors.rejectValue("descriptor.eventId", "field.must_be_unique", "EventId must be unique");
		}

		VenMarketContext marketContext = venMarketContextService.findOneByName(dto.getDescriptor().getMarketContext());
		if (marketContext == null) {
			errors.rejectValue("descriptor.marketContext", "field.must_exists", "MarketContext must exists");
		}

	}

}
