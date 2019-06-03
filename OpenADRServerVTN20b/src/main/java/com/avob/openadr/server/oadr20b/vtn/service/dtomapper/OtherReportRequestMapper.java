package com.avob.openadr.server.oadr20b.vtn.service.dtomapper;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;

@Service
public class OtherReportRequestMapper extends DozerConverter<OtherReportRequest, String> {

	public OtherReportRequestMapper() {
		super(OtherReportRequest.class, String.class);
	}

	@Override
	public OtherReportRequest convertFrom(String arg0, OtherReportRequest arg1) {
		return null;
	}

	@Override
	public String convertTo(OtherReportRequest arg0, String arg1) {
		return arg0.getReportRequestId();
	}

}
