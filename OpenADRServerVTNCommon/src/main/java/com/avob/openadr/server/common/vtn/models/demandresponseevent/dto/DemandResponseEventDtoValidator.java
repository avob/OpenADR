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

	private void checkXmlDuration(String duration, Errors errors, String field) {
		if (duration != null) {
			try {
				datatypeFactory.newDuration(duration);
			} catch (Exception e) {

				errors.rejectValue(field, "field." + field + ".invalid", "Invalid XML duration format");
			}
		}
	}

	private void checkNullOrEmpty(Object obj, Errors errors, String field) {
		if (obj == null || (obj instanceof String && "".equals(((String) obj).trim()))) {
			errors.rejectValue(field, "field.required", "Missing mandatory field");
		}
	}

	private void checkNotNull(Object obj, Errors errors, String field) {
		if (obj != null) {
			errors.rejectValue(field, "field.must_not_be_set", "Field must not be set");
		}
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		DemandResponseEventDto dto = (DemandResponseEventDto) arg0;

		checkNullOrEmpty(dto.getEventId(), errors, "eventId");
		checkNullOrEmpty(dto.getActivePeriod().getStart(), errors, "activePeriod.start");
		if (dto.getSignals().isEmpty()) {
			errors.rejectValue("signals", "field.required", "At least one signals must be configured");
			return;
		}

		checkNullOrEmpty(dto.getTargets(), errors, "targets");
		checkNullOrEmpty(dto.getActivePeriod().getDuration(), errors, "activePeriod.duration");
		checkNullOrEmpty(dto.getActivePeriod().getNotificationDuration(), errors, "activePeriod.notificationDuration");
		checkNullOrEmpty(dto.getActivePeriod().getToleranceDuration(), errors, "activePeriod.toleranceDuration");
		checkNullOrEmpty(dto.getDescriptor().getMarketContext(), errors, "descriptor.marketContext");

		if (!"0".equals(dto.getActivePeriod().getDuration())) {
			checkXmlDuration(dto.getActivePeriod().getDuration(), errors, "activePeriod.duration");
		}

		checkXmlDuration(dto.getActivePeriod().getNotificationDuration(), errors, "activePeriod.notificationDuration");
		checkXmlDuration(dto.getActivePeriod().getToleranceDuration(), errors, "activePeriod.toleranceDuration");
		checkXmlDuration(dto.getActivePeriod().getRampUpDuration(), errors, "activePeriod.rampUpDuration");
		checkXmlDuration(dto.getActivePeriod().getRecoveryDuration(), errors, "activePeriod.recoveryDuration");

		checkNotNull(dto.getModificationNumber(), errors, "modificationNumber");
		checkNotNull(dto.getCreatedTimestamp(), errors, "createdTimestamp");
		checkNotNull(dto.getLastUpdateTimestamp(), errors, "lastUpdateTimestamp");

		if (dto.getDescriptor().getPriority() != null && dto.getDescriptor().getPriority() < 0) {
			errors.rejectValue("priority", "field.priority.invalid", "Priority MUST be greater than 0");
		}

		if (errors.hasErrors()) {
			LOGGER.warn(errors.getAllErrors().toString());
		}
	}

}
