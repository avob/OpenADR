package com.avob.openadr.server.common.vtn.models.demandresponseevent;

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
        checkNullOrEmpty(dto.getStart(), errors, "start");
        checkNullOrEmpty(dto.getValue(), errors, "value");
        checkNullOrEmpty(dto.getComaSeparatedTargetedVenUsername(), errors, "comaSeparatedTargetedVenUsername");
        checkNullOrEmpty(dto.getDuration(), errors, "duration");
        checkNullOrEmpty(dto.getNotificationDuration(), errors, "notificationDuration");
        checkNullOrEmpty(dto.getToleranceDuration(), errors, "toleranceDuration");
        checkNullOrEmpty(dto.getMarketContext(), errors, "marketContext");

        if (!"0".equals(dto.getDuration())) {
            checkXmlDuration(dto.getDuration(), errors, "duration");
        }

        checkXmlDuration(dto.getNotificationDuration(), errors, "notificationDuration");
        checkXmlDuration(dto.getToleranceDuration(), errors, "toleranceDuration");
        checkXmlDuration(dto.getRampUpDuration(), errors, "rampUpDuration");
        checkXmlDuration(dto.getRecoveryDuration(), errors, "recoveryDuration");

        checkNotNull(dto.getModificationNumber(), errors, "modificationNumber");
        checkNotNull(dto.getCreatedTimestamp(), errors, "createdTimestamp");
        checkNotNull(dto.getLastUpdateTimestamp(), errors, "lastUpdateTimestamp");

        if (dto.getPriority() != null && dto.getPriority() < 0) {
            errors.rejectValue("priority", "field.priority.invalid", "Priority MUST be greater than 0");
        }

        if (errors.hasErrors()) {
            LOGGER.warn(errors.getAllErrors().toString());
        }
    }

}
