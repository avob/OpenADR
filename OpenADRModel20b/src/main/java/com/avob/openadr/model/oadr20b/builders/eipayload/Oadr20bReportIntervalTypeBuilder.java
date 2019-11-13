package com.avob.openadr.model.oadr20b.builders.eipayload;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.avob.PayloadKeyTokenType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;

public class Oadr20bReportIntervalTypeBuilder {

	private IntervalType interval;

	public Oadr20bReportIntervalTypeBuilder(String intervalId, Long start, String xmlDuration, String rid,
			Long confidence, Float accuracy, Float value) {

		interval = Oadr20bFactory.createReportIntervalType(intervalId, start, xmlDuration, rid, confidence, accuracy,
				value);
	}

	public Oadr20bReportIntervalTypeBuilder(String intervalId, Long start, String xmlDuration, String rid,
			Long confidence, Float accuracy, OadrPayloadResourceStatusType value) {

		interval = Oadr20bFactory.createReportIntervalType(intervalId, start, xmlDuration, rid, confidence, accuracy,
				value);
	}

	public <T> Oadr20bReportIntervalTypeBuilder(String intervalId, Long start, String xmlDuration, String rid,
			Long confidence, Float accuracy, PayloadKeyTokenType tokens) {
		interval = Oadr20bFactory.createKeyTokenReportIntervalType(intervalId, start, xmlDuration, rid, confidence,
				accuracy, tokens);
	}

	public IntervalType build() {
		return interval;
	}
}
