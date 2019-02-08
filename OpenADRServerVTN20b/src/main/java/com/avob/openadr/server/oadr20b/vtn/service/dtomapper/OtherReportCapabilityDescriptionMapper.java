package com.avob.openadr.server.oadr20b.vtn.service.dtomapper;

import org.dozer.DozerConverter;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;

public class OtherReportCapabilityDescriptionMapper extends DozerConverter<OtherReportCapabilityDescription, String> {

	public OtherReportCapabilityDescriptionMapper() {
		super(OtherReportCapabilityDescription.class, String.class);
	}

	@Override
	public String convertTo(OtherReportCapabilityDescription source, String destination) {
		return source.getRid();
	}

	@Override
	public OtherReportCapabilityDescription convertFrom(String source, OtherReportCapabilityDescription destination) {
		return null;
	}

}
