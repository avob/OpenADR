package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DemandResponseEventDtoValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemandResponseEventDtoValidator.class);

	private DatatypeFactory datatypeFactory = null;

	public DemandResponseEventDtoValidator() throws DatatypeConfigurationException {
		datatypeFactory = DatatypeFactory.newInstance();
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return DemandResponseEventDto.class.equals(arg0);
	}

	private void failOnInvalidXmlDuration(String duration, Errors errors, String field) {
		if (duration != null) {
			try {
				datatypeFactory.newDuration(duration);
			} catch (Exception e) {

				errors.rejectValue(field, "field." + field + ".invalid", "Invalid XML duration format");
			}
		}
	}

	private void failOnMissingOrEmpty(Object obj, Errors errors, String field) {
		if (obj == null || (obj instanceof String && "".equals(((String) obj).trim()))) {
			errors.rejectValue(field, "field.required", "Missing mandatory field");
		}
	}

	private void failOnPresent(Object obj, Errors errors, String field) {
		if (obj != null) {
			errors.rejectValue(field, "field.must_not_be_set", "Field must not be set");
		}
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		DemandResponseEventDto dto = (DemandResponseEventDto) arg0;

		failOnMissingOrEmpty(dto.getEventId(), errors, "eventId");
		failOnMissingOrEmpty(dto.getActivePeriod().getStart(), errors, "activePeriod.start");
		if (dto.getSignals().isEmpty()) {
			errors.rejectValue("signals", "field.required", "At least one signals must be configured");
			return;
		}

		failOnMissingOrEmpty(dto.getTargets(), errors, "targets");
		failOnMissingOrEmpty(dto.getActivePeriod().getDuration(), errors, "activePeriod.duration");
		failOnMissingOrEmpty(dto.getActivePeriod().getNotificationDuration(), errors, "activePeriod.notificationDuration");
		failOnMissingOrEmpty(dto.getActivePeriod().getToleranceDuration(), errors, "activePeriod.toleranceDuration");
		failOnMissingOrEmpty(dto.getDescriptor().getMarketContext(), errors, "descriptor.marketContext");

		if (!"0".equals(dto.getActivePeriod().getDuration())) {
			failOnInvalidXmlDuration(dto.getActivePeriod().getDuration(), errors, "activePeriod.duration");
		}
		
		failOnMissingOrEmpty(dto.getOadrProfile(), errors, "oadrProfile");
		failOnMissingOrEmpty(dto.getDescriptor().getResponseRequired(), errors, "descriptor.responseRequired");

		failOnInvalidXmlDuration(dto.getActivePeriod().getNotificationDuration(), errors, "activePeriod.notificationDuration");
		failOnInvalidXmlDuration(dto.getActivePeriod().getToleranceDuration(), errors, "activePeriod.toleranceDuration");
		failOnInvalidXmlDuration(dto.getActivePeriod().getRampUpDuration(), errors, "activePeriod.rampUpDuration");
		failOnInvalidXmlDuration(dto.getActivePeriod().getRecoveryDuration(), errors, "activePeriod.recoveryDuration");

		failOnPresent(dto.getModificationNumber(), errors, "modificationNumber");
		failOnPresent(dto.getCreatedTimestamp(), errors, "createdTimestamp");
		failOnPresent(dto.getLastUpdateTimestamp(), errors, "lastUpdateTimestamp");

		

		if (dto.getDescriptor().getPriority() != null && dto.getDescriptor().getPriority() < 0) {
			errors.rejectValue("priority", "field.priority.invalid", "Priority MUST be greater than 0");
		}

		if (errors.hasErrors()) {
			LOGGER.warn(errors.getAllErrors().toString());
		}
	}

}
