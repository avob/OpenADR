package com.avob.openadr.model.oadr20b.dto;

public class EosMetaData extends EosExportedObject {

	private String payload;

	public EosMetaData(String venId, String pathId, String serviceName, String index, ReportCapabilityDto report,
			String payload) {
		super(venId, pathId, serviceName, index, report);
		this.setPayload(payload);
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
