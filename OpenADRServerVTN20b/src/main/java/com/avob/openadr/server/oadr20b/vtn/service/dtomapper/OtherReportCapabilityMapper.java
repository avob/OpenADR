package com.avob.openadr.server.oadr20b.vtn.service.dtomapper;

import org.dozer.DozerConverter;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;

public class OtherReportCapabilityMapper extends DozerConverter<OtherReportCapability, String> {

	public OtherReportCapabilityMapper() {
		super(OtherReportCapability.class, String.class);
	}

	@Override
	public String convertTo(OtherReportCapability source, String destination) {
		return source.getReportSpecifierId();
	}

	@Override
	public OtherReportCapability convertFrom(String source, OtherReportCapability destination) {
		return null;
	}

}
