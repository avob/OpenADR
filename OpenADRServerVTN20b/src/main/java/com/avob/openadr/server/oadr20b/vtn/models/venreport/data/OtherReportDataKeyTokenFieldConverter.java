package com.avob.openadr.server.oadr20b.vtn.models.venreport.data;

import java.io.IOException;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.model.oadr20b.avob.KeyTokenType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OtherReportDataKeyTokenFieldConverter implements AttributeConverter<List<KeyTokenType>, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OtherReportDataKeyTokenFieldConverter.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<KeyTokenType> customerInfo) {

		String customerInfoJson = null;
		try {
			customerInfoJson = objectMapper.writeValueAsString(customerInfo);
		} catch (final JsonProcessingException e) {
			LOGGER.error("JSON writing error", e);
		}

		return customerInfoJson;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KeyTokenType> convertToEntityAttribute(String customerInfoJSON) {

		List<KeyTokenType> customerInfo = null;
		try {
			customerInfo = objectMapper.readValue(customerInfoJSON, List.class);
		} catch (final IOException e) {
			LOGGER.error("JSON reading error", e);
		}

		return customerInfo;
	}

}
