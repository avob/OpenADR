package com.avob.openadr.server.oadr20b.vtn.converter;

import java.time.Instant;

import org.springframework.core.convert.converter.Converter;

import com.avob.openadr.server.common.vtn.exception.UnparsableDateFormatException;
import com.avob.openadr.server.common.vtn.utils.DateUtils;

public class InstantConverter implements Converter<String, Instant> {

	@Override
	public Instant convert(String source) {
		try {
			return Instant.ofEpochMilli(DateUtils.parseDateAsTimestamp(source));
		} catch (UnparsableDateFormatException e) {
			return null;
		}
	}

}
