package com.avob.openadr.server.oadr20b.vtn.converter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class ZonedDateTimeConverter {

	static public class From implements Converter<String, ZonedDateTime> {
		@Override
		public ZonedDateTime convert(String source) {
			return ZonedDateTime.parse(source);
		}
	}

	static public class To implements Converter<ZonedDateTime, String> {
		@Override
		public String convert(ZonedDateTime source) {
			return source.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		}
	}

}