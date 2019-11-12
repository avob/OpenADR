package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.validator;

import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventUpdateDto;

@Component
public class DemandResponseEventUpdateDtoValidator extends DemandResponseEventDtoValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemandResponseEventUpdateDtoValidator.class);

	public DemandResponseEventUpdateDtoValidator() throws DatatypeConfigurationException {
		super();
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return DemandResponseEventUpdateDto.class.equals(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		DemandResponseEventUpdateDto dto = (DemandResponseEventUpdateDto) arg0;

		validateContent(dto, errors);

		if (dto.getPublished() == null) {
			errors.rejectValue("published", "field.required", "published must be configured");
		}

		if (errors.hasErrors()) {
			LOGGER.warn(errors.getAllErrors().toString());
		}

	}

}
