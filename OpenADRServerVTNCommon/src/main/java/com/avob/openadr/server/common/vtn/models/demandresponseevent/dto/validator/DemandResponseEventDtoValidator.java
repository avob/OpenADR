package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.validator;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.springframework.validation.Errors;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventContentDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventDto;

public class DemandResponseEventDtoValidator {

	private DatatypeFactory datatypeFactory = null;

	public DemandResponseEventDtoValidator() throws DatatypeConfigurationException {
		datatypeFactory = DatatypeFactory.newInstance();
	}

	protected void failOnInvalidXmlDuration(String duration, Errors errors, String field) {
		if (duration != null) {
			try {
				datatypeFactory.newDuration(duration);
			} catch (Exception e) {

				errors.rejectValue(field, "field." + field + ".invalid", "Invalid XML duration format");
			}
		}
	}

	protected void failOnMissingOrEmpty(Object obj, Errors errors, String field) {
		if (obj == null || (obj instanceof String && "".equals(((String) obj).trim()))) {
			errors.rejectValue(field, "field.required", "Missing mandatory field");
		}
	}

	protected void failOnPresent(Object obj, Errors errors, String field) {
		if (obj != null) {
			errors.rejectValue(field, "field.must_not_be_set", "Field must not be set");
		}
	}

	protected void validateContent(DemandResponseEventContentDto dto, Errors errors) {

		if (dto.getSignals().isEmpty()) {
			errors.rejectValue("signals", "field.required", "At least one signal must be configured");
		}

	}

	protected void validateDescriptor(DemandResponseEventDto dto, Errors errors) {
		failOnMissingOrEmpty(dto.getDescriptor().getMarketContext(), errors, "descriptor.marketContext");
		failOnMissingOrEmpty(dto.getDescriptor().getOadrProfile(), errors, "descriptor.oadrProfile");
		failOnMissingOrEmpty(dto.getDescriptor().getResponseRequired(), errors, "descriptor.responseRequired");
		failOnPresent(dto.getDescriptor().getModificationNumber(), errors, "descriptor.modificationNumber");
		if (dto.getDescriptor().getPriority() != null && dto.getDescriptor().getPriority() < 0) {
			errors.rejectValue("descriptor.priority", "field.priority.invalid", "Priority MUST be greater than 0");
		}

	}

	protected void validateActivePeriod(DemandResponseEventDto dto, Errors errors) {

		failOnMissingOrEmpty(dto.getActivePeriod().getStart(), errors, "activePeriod.start");
		failOnMissingOrEmpty(dto.getActivePeriod().getDuration(), errors, "activePeriod.duration");
		failOnMissingOrEmpty(dto.getActivePeriod().getNotificationDuration(), errors,
				"activePeriod.notificationDuration");
		failOnMissingOrEmpty(dto.getActivePeriod().getToleranceDuration(), errors, "activePeriod.toleranceDuration");
		if (!"0".equals(dto.getActivePeriod().getDuration())) {
			failOnInvalidXmlDuration(dto.getActivePeriod().getDuration(), errors, "activePeriod.duration");
		}
		failOnInvalidXmlDuration(dto.getActivePeriod().getNotificationDuration(), errors,
				"activePeriod.notificationDuration");
		failOnInvalidXmlDuration(dto.getActivePeriod().getToleranceDuration(), errors,
				"activePeriod.toleranceDuration");
		failOnInvalidXmlDuration(dto.getActivePeriod().getRampUpDuration(), errors, "activePeriod.rampUpDuration");
		failOnInvalidXmlDuration(dto.getActivePeriod().getRecoveryDuration(), errors, "activePeriod.recoveryDuration");

	}

}
